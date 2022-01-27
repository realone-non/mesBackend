package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.BadItemEnrollmentResponse;
import com.mes.mesBackend.dto.response.BadItemWorkOrderResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.LotLogRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.WorkOrderBadItemRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.BadItemEnrollmentService;
import com.mes.mesBackend.service.BadItemService;
import com.mes.mesBackend.service.LotMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 8-5. 불량 등록
@Service
@RequiredArgsConstructor
public class BadItemEnrollmentServiceImpl implements BadItemEnrollmentService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final LotLogRepository lotLogRepository;
    private final BadItemService badItemService;
    private final LotMasterService lotMasterService;
    private final WorkOrderBadItemRepository workOrderBadItemRepo;
    private final LotMasterRepository lotMasterRepo;

    // 작업지시 정보 리스트 조회, 검색조건: 작업장 id, 작업라인 id, 품목그룹 id, 제조오더번호, JOB NO, 작업기간 fromDate~toDate, 품번|품목
    @Override
    public List<BadItemWorkOrderResponse> getWorkOrders(
            Long workCenterId,
            Long workLineId,
            Long itemGroupId,
            String produceOrderNo,
            String workOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndItemName
    ) throws NotFoundException {
        // 작업지시의 지시상태가 COMPLETION 인것만 조회
        List<BadItemWorkOrderResponse> workOrderResponses = workOrderDetailRepo.findBadItemWorkOrderResponseByCondition(
                workCenterId,
                workLineId,
                itemGroupId,
                produceOrderNo,
                workOrderNo,
                fromDate,
                toDate,
                itemNoAndItemName
        );
        for (BadItemWorkOrderResponse response : workOrderResponses) {
            LotLog lotLog = lotLogRepository.findLotLogByWorkOrderIdAndWorkProcessId(response.getWorkOrderId(), response.getWorkProcessId())
                    .orElseThrow(() -> new NotFoundException("공정 완료된 작업지시가 LotLog 에 등록되지 않았습니다."));
            response.setBadAmount(lotLog.getLotMaster().getBadItemAmount());        // lotMaster 의 불량수량
            response.setProductionAmount(lotLog.getLotMaster().getCreatedAmount()); // lotMaster 의 생성수량
        }
        return workOrderResponses;
    }

    // 불량유형 정보 생성
    @Override
    public BadItemEnrollmentResponse createBadItemEnrollment(
            Long workOrderId,
            Long badItemId,
            Long lotMasterId,
            int badItemAmount
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        BadItem badItem = badItemService.getBadItemOrThrow(badItemId);
        LotMaster lotMaster = lotMasterService.getLotMasterOrThrow(lotMasterId);

        // 입력받은 badItemAmount 가 해당 lotMaster 의 생성수량보다 크면 안됨
        throwIfBadItemAmountGreaterThanCreatedAmountLotMaster(lotMaster.getBadItemAmount() + badItemAmount, lotMaster.getCreatedAmount());
        // 입력받은 lotMaster 는 같은 불량유형이 존재하면 안됨.
        throwIfBadItemIdInLotMaster(lotMasterId, badItemId);
        // lotLog 에서 해당하는 작업지시에 입력받은 lotMaster 가 있는지 여부
        throwIfLotMasterId(workOrderId, lotMasterId);

        LotLog lotLog = lotLogRepository.findByWorkOrderDetailAndLotMaster(workOrderDetail, lotMaster)
                .orElseThrow(() -> new NotFoundException("lotLog does not exist"));

        // 입력받은 불량이 해당하는 작업공정의 불량유형이랑 일치하는지 여부
        throwIfBadItemIdAnyMatchWorkProcess(lotLog.getWorkProcess().getId(), badItemId);

        WorkOrderBadItem workOrderBadItem = new WorkOrderBadItem();
        workOrderBadItem.add(lotLog, badItem, badItemAmount);

        lotMaster.setBadItemAmount(lotMaster.getBadItemAmount() + badItemAmount);   // 불량수량 변경
        lotMaster.setStockAmount(lotMaster.getStockAmount() - badItemAmount);   // 재고수량 변경
        workOrderBadItemRepo.save(workOrderBadItem);
        lotMasterRepo.save(lotMaster);

        return getBadItemEnrollmentResponse(workOrderBadItem.getId());
    }

    // 불량유형 정보 전체 조회
    @Override
    public List<BadItemEnrollmentResponse> getBadItemEnrollments(Long workOrderId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        return workOrderBadItemRepo.findWorkOrderEnrollmentResponsesByWorkOrderId(workOrderDetail.getId());
    }

    // 불량유형 정보 수정 (불량수량)
    @Override
    public BadItemEnrollmentResponse updateBadItemEnrollment(
            Long workOrderId,
            Long badItemEnrollmentId,
            int badItemAmount
    ) throws NotFoundException, BadRequestException {
        getWorkOrderDetailOrThrow(workOrderId);
        WorkOrderBadItem findWorkOrderBadItem = getWorkOrderBadItemOrThrow(badItemEnrollmentId);
        LotMaster lotMaster = findWorkOrderBadItem.getLotLog().getLotMaster();

        int beforeBadItemAmount = (lotMaster.getBadItemAmount() - findWorkOrderBadItem.getBadItemAmount());
        int beforeStockAmount = lotMaster.getStockAmount() + findWorkOrderBadItem.getBadItemAmount();
        // 입력받은 불량 수량이 lotMaster 의 생성수량보다 큰지 체크
        throwIfBadItemAmountGreaterThanCreatedAmountLotMaster(beforeBadItemAmount + badItemAmount, lotMaster.getCreatedAmount());

        findWorkOrderBadItem.setBadItemAmount(badItemAmount);
        lotMaster.setBadItemAmount(beforeBadItemAmount + badItemAmount);    // 불량수량 변경
        lotMaster.setStockAmount(beforeStockAmount - badItemAmount);  // 재고수량 변경

        workOrderBadItemRepo.save(findWorkOrderBadItem);
        lotMasterRepo.save(lotMaster);

        return getBadItemEnrollmentResponse(findWorkOrderBadItem.getId());
    }

    // 불량유형 정보 삭제
    @Override
    public void deleteBadItemEnrollment(Long workOrderId, Long badItemEnrollmentId) throws NotFoundException {
        getWorkOrderDetailOrThrow(workOrderId);
        WorkOrderBadItem findWorkOrderBadItem = getWorkOrderBadItemOrThrow(badItemEnrollmentId);
        findWorkOrderBadItem.delete();
        int beforeBadItemAmount = findWorkOrderBadItem.getBadItemAmount();

        LotMaster lotMaster = findWorkOrderBadItem.getLotLog().getLotMaster();
        lotMaster.setBadItemAmount(lotMaster.getBadItemAmount() - beforeBadItemAmount); // 불량수량 변경
        lotMaster.setStockAmount(lotMaster.getStockAmount() + beforeBadItemAmount);     // 재고수량 변경
        workOrderBadItemRepo.save(findWorkOrderBadItem);
        lotMasterRepo.save(lotMaster);
    }

    // 불량유형 단일 조회 및 예외
    private BadItemEnrollmentResponse getBadItemEnrollmentResponse(Long id) throws NotFoundException {
        return workOrderBadItemRepo.findWorkOrderEnrollmentResponseById(id)
                .orElseThrow(() -> new NotFoundException("workOrderBadItem does not exist. input id: " + id));
    }

    // 작업지시 불량정보 단일 조회 및 예외
    private WorkOrderBadItem getWorkOrderBadItemOrThrow(Long id) throws NotFoundException {
        return workOrderBadItemRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workOrderBadItem does not exist. input id: " + id));
    }

    // 입력받은 불량이 해당하는 작업공정의 불량유형이랑 일치하는지 여부
    private void throwIfBadItemIdAnyMatchWorkProcess(Long workProcessId, Long badItemId) throws BadRequestException {
        List<Long> findBadItemByWorkOrderId = workOrderBadItemRepo.findBadItemIdByWorkOrderId(workProcessId);
        boolean badItemIdAnyMatchWorkProcess = findBadItemByWorkOrderId.stream().anyMatch(id -> id.equals(badItemId));
        if (!badItemIdAnyMatchWorkProcess) {
            throw new BadRequestException("입력한 불량정보가 lot 가 생성된 작업공정에 해당하는 불량유형이 아님.");
        }
    }

    // lotLog 에서 해당하는 작업지시에 입력받은 lotMaster 가 있는지 여부
    private void throwIfLotMasterId(Long workOrderId, Long lotMasterId) throws BadRequestException {
        List<Long> lotMasterIds = lotLogRepository.findLotMasterIdByWorkOrderId(workOrderId);
        boolean lotMasterIdCheck = lotMasterIds.stream().anyMatch(id -> id.equals(lotMasterId));
        if (!lotMasterIdCheck){
            throw new BadRequestException("해당하는 작업지시에 대한 lotMaster 가 존재하지 않음.");
        }
    }

    // 작업지시 단일 조회 및 예외
    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workOrder does not exist. input id: " + id));
    }

    // 입력받은 badItemAmount 가 해당 lotMaster 의 생성수량보다 크면 안됨
    private void throwIfBadItemAmountGreaterThanCreatedAmountLotMaster(int badItemAmount, int createdAmount) throws BadRequestException {
        if (badItemAmount > createdAmount) {
            throw new BadRequestException("badItemAmount cannot be greater than createdAmount of lotMaster. " +
                    "input badItemAmount: " + badItemAmount + ", " +
                    "createdAmount of lotMaster: " + createdAmount);
        }
    }

    // 입력받은 lotMaster 는 같은 불량유형이 존재하면 안됨.
    private void throwIfBadItemIdInLotMaster(Long lotMasterId, Long badItemId) throws BadRequestException {
        List<Long> findBadItemIdByLotMasterId = workOrderBadItemRepo.findBadItemIdByLotMasterId(lotMasterId);
        boolean badItemIdAnyMatchByLotMaster = findBadItemIdByLotMasterId.stream().anyMatch(id -> id.equals(badItemId));
        if (badItemIdAnyMatchByLotMaster) {
            throw new BadRequestException("하나의 로트는 같은 불량유형을 두개이상 등록 할 수 없음.");
        }
    }
}
