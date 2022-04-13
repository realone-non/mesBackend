package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.BadItemEnrollmentService;
import com.mes.mesBackend.service.BadItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.EQUIPMENT_LOT;
import static com.mes.mesBackend.helper.Constants.DECIMAL_POINT_2;
import static com.mes.mesBackend.helper.Constants.PERCENT;

// 8-5. 불량 등록
@Service
@RequiredArgsConstructor
public class BadItemEnrollmentServiceImpl implements BadItemEnrollmentService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final LotLogRepository lotLogRepository;
    private final BadItemService badItemService;
    private final WorkOrderBadItemRepository workOrderBadItemRepo;
    private final LotMasterRepository lotMasterRepo;
    private final LotLogHelper lotLogHelper;
    private final LotEquipmentConnectRepository lotEquipmentConnectRepository;
    private final LotConnectRepository lotConnectRepository;

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
        // 작업지시의 지시상태가 COMPLETION, 공정구분이 출하, 자재입고 제외
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
            Long workOrderId = response.getWorkOrderId();
            Long workProcessId = response.getWorkProcessId();
            LotLog lotLog = lotLogRepository.findLotLogByWorkOrderIdAndWorkProcessId(workOrderId, workProcessId)
                    .orElseThrow(() -> new NotFoundException("[데이터오류] 공정 완료된 작업지시가 LotLog 에 등록되지 않았습니다."));
            Long dummyLotId = lotLog.getLotMaster().getId();
            BadItemWorkOrderResponse.subDto subDto = lotMasterRepo.findLotMaterByDummyLotIdAndWorkProcessId(dummyLotId, workProcessId)
                    .orElseThrow(() -> new NotFoundException("[데이터오류] lotLog 에 등록된 lotMaster(id: " + dummyLotId + ") 가 lotEquipmentConnect parentLot 로 등록되지 않았습니다."));
            response.set(subDto);
        }

        if (itemNoAndItemName != null)
            return workOrderResponses.stream().filter(f -> f.getItemNo().contains(itemNoAndItemName) || f.getItemName().contains(itemNoAndItemName)).collect(Collectors.toList());
        else
            return workOrderResponses;
    }

    // 작업지시 별 작업완료 상세 리스트
    @Override
    public List<WorkOrderDetailResponse> getWorkOrderDetails(Long workOrderId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        LotLog lotLog = lotLogHelper.getLotLogByWorkOrderDetailOrThrow(workOrderDetail.getId());
        LotMaster dummyLot = lotLog.getLotMaster();
        List<WorkOrderDetailResponse> responses = lotEquipmentConnectRepository.findWorkOrderDetailResponseByDummyLotId(dummyLot.getId());
        responses.forEach(f -> f.setWorkOrderId(workOrderDetail.getId()));
        return responses;
    }

    // 작업완료 상세 리스트 별 불량정보 조회
    @Override
    public List<WorkOrderDetailBadItemResponse> getBadItemEnrollments(Long workOrderId, Long equipmentLotId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        LotMaster equipmentLot = getLotMatserOrThrow(equipmentLotId);
        return workOrderBadItemRepo.findWorkOrderDetailBadItemResponseByEquipmentLotId(equipmentLot.getId());
    }

    // 작업완료 상세 리스트 별 불량정보 생성
    @Override
    public WorkOrderDetailBadItemResponse createBadItemEnrollment(Long workOrderId, Long equipmentLotId, Long badItemId, int badItemAmount) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        LotMaster equipmentLot = getLotMatserOrThrow(equipmentLotId);
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        LotMaster dummyLot = lotEquipmentConnect.getParentLot();
        BadItem badItem = badItemService.getBadItemOrThrow(badItemId);

        // 입력받은 불량이 해당하는 작업공정의 불량유형이랑 일치하는지 여부
        throwIfBadItemIdAnyMatchWorkProcess(workOrderDetail.getWorkProcess().getId(), badItemId);
        // 총 불량수량과 이미 등록되어있는 불량수량 합계가 생성수량을 초과하는지 체크
        throwBadAmountCheck(equipmentLot.getId(), badItemAmount, equipmentLot.getCreatedAmount());
        // 하나의 lotMaster 에 중복 불량유형이 있는지 체크
        throwIfBadItemIdInLotMaster(equipmentLot.getId(), badItem.getId());
        // realLots 의 inputAmount 가 하나라도 0 이 아닌지 체크(다른공정에서 사용되었으면 에외)
        throwIfRealLotInputAmountCheck(dummyLot.getId());


        for (int i = 1; i <= badItemAmount; i++) {
            LotMaster notStockAmountEquipmentLot = lotEquipmentConnectRepository.findEquipmentLotByIdAndStockAmount(equipmentLotId, 0)
                    .orElse(null);
            if (notStockAmountEquipmentLot != null) {
                notStockAmountEquipmentLot.setStockAmount(notStockAmountEquipmentLot.getStockAmount() - 1);
                lotMasterRepo.save(notStockAmountEquipmentLot);
            }

            if (notStockAmountEquipmentLot == null) {
                LotMaster realLot = lotConnectRepository.findByStockAmountAndCreatedDateDesc(equipmentLot.getId(), 0, false)
                        .orElseThrow(() -> new BadRequestException("불량 수량 반영할 분할 LOT 가 존재하지 않습니다."));
                realLot.setStockAmount(realLot.getStockAmount() - 1);                   // 분할 LOT 재고수량 변경
                lotMasterRepo.save(realLot);
            }
        }

        // workOrderBadItem 생성
        WorkOrderBadItem workOrderBadItem = new WorkOrderBadItem();
        workOrderBadItem.popCreate(badItem, workOrderDetail, equipmentLot, badItemAmount, EQUIPMENT_LOT);
        workOrderBadItemRepo.save(workOrderBadItem);

        // equipmentLot 불량수량 변경
        int equipmentLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        equipmentLot.setBadItemAmount(equipmentLotBadItemSum);
        lotMasterRepo.save(equipmentLot);

        // dummyLot 불량수량 변경
        int dummyLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByDummyLotMaster(dummyLot.getId()).stream().mapToInt(Integer::intValue).sum();
        dummyLot.setBadItemAmount(dummyLotBadItemSum);
        lotMasterRepo.save(dummyLot);

        WorkOrderDetailBadItemResponse response = new WorkOrderDetailBadItemResponse();
        return response.put(workOrderBadItem);
    }

    // 작업완료 상세 리스트 별 불량정보 수정
    @Override
    public WorkOrderDetailBadItemResponse updateBadItemEnrollment(Long workOrderId, Long equipmentLotId, Long badItemEnrollmentId, int badItemAmount) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        LotMaster equipmentLot = getLotMatserOrThrow(equipmentLotId);
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        LotMaster dummyLot = lotEquipmentConnect.getParentLot();
        WorkOrderBadItem workOrderBadItem = getWorkOrderBadItemOrThrow(badItemEnrollmentId);
        int beforeBadItemAmount = workOrderBadItem.getBadItemAmount();

        // realLots 의 inputAmount 가 하나라도 0 이 아닌지 체크(다른공정에서 사용되었으면 에외)
        throwIfRealLotInputAmountCheck(dummyLot.getId());

        int badItemDifference = beforeBadItemAmount - badItemAmount;        // 입력한 불량수량 - 수정 전 불량수량
        if (badItemAmount > beforeBadItemAmount) {      // 수정할 수량이 더 크면
            for (int i = 1; i <= badItemDifference * -1; i++) {
                LotMaster notStockAmountEquipmentLot = lotEquipmentConnectRepository.findEquipmentLotByIdAndStockAmount(equipmentLotId, 0)
                        .orElse(null);
                if (notStockAmountEquipmentLot != null) {
                    notStockAmountEquipmentLot.setStockAmount(notStockAmountEquipmentLot.getStockAmount() - 1);
                    lotMasterRepo.save(notStockAmountEquipmentLot);
                }

                if (notStockAmountEquipmentLot == null) {
                    LotMaster realLot = lotConnectRepository.findByStockAmountAndCreatedDateDesc(equipmentLot.getId(), 0, false)
                            .orElseThrow(() -> new BadRequestException("불량 수량 반영할 분할 LOT 가 존재하지 않습니다."));
                    realLot.setStockAmount(realLot.getStockAmount() - 1);                   // 분할 LOT 재고수량 변경
                    lotMasterRepo.save(realLot);
                }
            }
        } else { // 수정할 수량이 기존 불량수량보다 작다면
            for (int i = 1; i <= badItemDifference; i++) {
                LotMaster realLot = lotConnectRepository.findByStockAmountAndCreatedDateDesc(equipmentLot.getId(), null, true)
                        .orElse(null);

                if (realLot != null) {
                    realLot.setStockAmount(realLot.getStockAmount() + 1);
                    lotMasterRepo.save(realLot);
                } else {
                    equipmentLot.setStockAmount(equipmentLot.getStockAmount() + 1);
                    lotMasterRepo.save(equipmentLot);
                }
            }
        }

        // 불량수량 변경
        workOrderBadItem.setBadItemAmount(badItemAmount);
        workOrderBadItemRepo.save(workOrderBadItem);

        // equipmentLot 불량수량 변경
        int equipmentLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        equipmentLot.setBadItemAmount(equipmentLotBadItemSum);
        lotMasterRepo.save(equipmentLot);

        // dummyLot 불량수량 변경
        int dummyLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByDummyLotMaster(dummyLot.getId()).stream().mapToInt(Integer::intValue).sum();
        dummyLot.setBadItemAmount(dummyLotBadItemSum);
        lotMasterRepo.save(dummyLot);

        WorkOrderDetailBadItemResponse response = new WorkOrderDetailBadItemResponse();
        return response.put(workOrderBadItem);
    }

    // 작업완료 상세 리스트 별 불량정보 삭제
    @Override
    public void deleteBadItemEnrollment(Long workOrderId, Long equipmentLotId, Long badItemEnrollmentId) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        LotMaster equipmentLot = getLotMatserOrThrow(equipmentLotId);
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        LotMaster dummyLot = lotEquipmentConnect.getParentLot();
        WorkOrderBadItem workOrderBadItem = getWorkOrderBadItemOrThrow(badItemEnrollmentId);
        int badItemAmount = workOrderBadItem.getBadItemAmount();

        // realLots 의 inputAmount 가 하나라도 0 이 아닌지 체크(다른공정에서 사용되었으면 에외)
        throwIfRealLotInputAmountCheck(dummyLot.getId());

        // realLot 재고수량 반영
        for (int i = 1; i <= badItemAmount; i++) {
            LotMaster realLot = lotConnectRepository.findByStockAmountAndCreatedDateDesc(equipmentLot.getId(), null, true)
                    .orElse(null);

            if (realLot != null) {
                realLot.setStockAmount(realLot.getStockAmount() + 1);
                lotMasterRepo.save(realLot);
            } else {
                equipmentLot.setStockAmount(equipmentLot.getStockAmount() + 1);
                lotMasterRepo.save(equipmentLot);
            }
        }

        // 불량 삭제
        workOrderBadItem.delete();
        workOrderBadItemRepo.save(workOrderBadItem);

        // equipmentLot 불량수량 변경
        int equipmentLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        equipmentLot.setBadItemAmount(equipmentLotBadItemSum);
        lotMasterRepo.save(equipmentLot);

        // dummyLot 불량수량 변경
        int dummyLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByDummyLotMaster(dummyLot.getId()).stream().mapToInt(Integer::intValue).sum();
        dummyLot.setBadItemAmount(dummyLotBadItemSum);
        lotMasterRepo.save(dummyLot);
    }


    // ====================================== 작업지시 불량률 조회 =================================
    // 작업지시 불량률 정보 리스트 조회(지시상태 완료, 진행중만 조회)
    // 현재 완료, 진행중인 작업지시만 조회, 검색조건: 공정 id, 작업지시 번호, 품번|품명, 작업자 id, 작업기간 fromDate~toDate
    @Override
    public List<WorkOrderBadItemStatusResponse> getWorkOrderBadItems(
            Long workProcessId,
            String workOrderNo,
            String itemNoAndItemName,
            Long userId,
            LocalDate fromDate,
            LocalDate toDate
    ) throws NotFoundException {
        List<WorkOrderBadItemStatusResponse> responses =
                workOrderDetailRepo.findWorkOrderBadItemStatusResponseByCondition(workProcessId, workOrderNo, itemNoAndItemName, userId, fromDate, toDate);
        for (WorkOrderBadItemStatusResponse response : responses) {
            Long workOrder = response.getWorkOrderId();
            Long workProcess = response.getWorkProcessId();
            // dummyLot 를 찾기위한 Lotlog
            LotLog lotLog = lotLogRepository.findLotLogByWorkOrderIdAndWorkProcessId(workOrder, workProcess)
                    .orElseThrow(() -> new NotFoundException("[데이터오류] 공정 완료된 작업지시가 LotLog 에 등록되지 않았습니다."));
            // dummyLot
            Long dummyLotId = lotLog.getLotMaster().getId();
            BadItemWorkOrderResponse.subDto subDto = lotMasterRepo.findLotMaterByDummyLotIdAndWorkProcessId(dummyLotId, workProcess)
                    .orElseThrow(() -> new NotFoundException("[데이터오류] lotLog 에 등록된 lotMaster(id: " + dummyLotId + ") 가 lotEquipmentConnect parentLot 로 등록되지 않았습니다."));
            // 품목정보 및 불량률 계산
            response.setRateCalculationAndItem(subDto);
        }

        // itemNoAndItemName 검색 필터링
        if (itemNoAndItemName != null) {
            return responses.stream().filter(f -> f.getItemNo().contains(itemNoAndItemName) || f.getItemName().contains(itemNoAndItemName)).collect(Collectors.toList());
        } else
            return responses;
    }

    // 작업지시 상세 불량률 조회
    @Override
    public List<WorkOrderBadItemStatusDetailResponse> getWorkOrderBadItemDetails(Long workOrderId) throws NotFoundException {
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderId);
        // dummyLot 를 찾기위한 Lotlog
        LotLog lotLog = lotLogRepository.findLotLogByWorkOrderIdAndWorkProcessId(workOrder.getId(), workOrder.getWorkProcess().getId())
                .orElseThrow(() -> new NotFoundException("[데이터오류] 공정 완료된 작업지시가 LotLog 에 등록되지 않았습니다."));
        LotMaster dummyLot = lotLog.getLotMaster();
        List<WorkOrderBadItemStatusDetailResponse> responses = workOrderBadItemRepo.findByDummyLotIdGroupByBadItemType(dummyLot.getId());

        return responses.stream().map(m ->
                m.setRateCalculation(dummyLot.getCreatedAmount(), dummyLot.getBadItemAmount())
        ).collect(Collectors.toList());
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

    // 작업지시 단일 조회 및 예외
    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workOrder does not exist. input id: " + id));
    }

    // 입력받은 lotMaster 는 같은 불량유형이 존재하면 안됨.
    private void throwIfBadItemIdInLotMaster(Long lotMasterId, Long badItemId) throws BadRequestException {
        List<Long> findBadItemIdByLotMasterId = workOrderBadItemRepo.findBadItemIdByLotMasterId(lotMasterId);
        boolean badItemIdAnyMatchByLotMaster = findBadItemIdByLotMasterId.stream().anyMatch(id -> id.equals(badItemId));
        if (badItemIdAnyMatchByLotMaster) {
            throw new BadRequestException("하나의 로트는 같은 불량유형을 두개이상 등록 할 수 없습니다.");
        }
    }

    // lotEquipmentConnect 단일 조회 및 예외
    private LotEquipmentConnect getLotEquipmentConnectByChildLotOrThrow(Long childLotId) throws NotFoundException {
        return lotEquipmentConnectRepository.findByChildId(childLotId)
                .orElseThrow(() -> new NotFoundException("해당하는 설비 lot 가 존재하지 않습니다."));
    }

    // lotMaster 단일 조회 및 예외
    private LotMaster getLotMatserOrThrow(Long id) throws NotFoundException {
        return lotMasterRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("lotMaster does not exist."));
    }

    private void throwBadAmountCheck(Long equipmentLotId, int badItemAmount, int equipmentCreateAmount) throws BadRequestException {
        int equipmentLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLotId).stream().mapToInt(Integer::intValue).sum();
        if (equipmentLotBadItemSum + badItemAmount > equipmentCreateAmount) {
            throw new BadRequestException("총 불량수량과 입력한 불량수량의 합계가 equipmentLot 의 생성수량을 초과할수 없습니다. " +
                    "생성수량: " + equipmentCreateAmount + ", " +
                    "현재 등록된 불량수량: " + equipmentLotBadItemSum + ", " +
                    "입력한 불량수량: " + badItemAmount + ", " +
                    "입력 가능불량수량: " + (equipmentCreateAmount - equipmentLotBadItemSum)
            );
        }
    }

    private void throwIfRealLotInputAmountCheck(Long dummyLotId) throws BadRequestException {
        List<LotMaster> checkRealLots = lotEquipmentConnectRepository.findChildLotByChildLotOfParentLotCreatedDateDesc(dummyLotId);
        boolean noneMatch = checkRealLots.stream().allMatch(n -> n.getInputAmount() == 0);
        if (!noneMatch) throw new BadRequestException("공정이 진행중인 LOT 이므로 생성, 수정, 삭제를 할 수 없습니다.");
    }
}
