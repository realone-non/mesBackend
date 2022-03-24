package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.request.PurchaseInputRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.entity.PurchaseInput;
import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.PurchaseInputService;
import com.mes.mesBackend.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;

// 9-5. 구매입고 등록
@Service
@RequiredArgsConstructor
public class PurchaseInputServiceImpl implements PurchaseInputService {
    private final PurchaseInputRepository purchaseInputRepo;
    private final ModelMapper mapper;
    private final PurchaseRequestService purchaseRequestService;
    private final LotMasterRepository lotMasterRepo;
    private final PurchaseRequestRepository purchaseRequestRepos;
    private final LotHelper lotHelper;
    private final PurchaseOrderRepository purchaseOrderRepo;
    private final LotTypeRepository lotTypeRepository;

    // 구매입고 리스트 조회, 검색조건: 입고기간 fromDate~toDate, 입고창고, 거래처, 품명|품번
    @Override
    public List<PurchaseInputResponse> getPurchaseInputs(
            LocalDate fromDate,
            LocalDate toDate,
            Long wareHouseId,
            Long clientId,
            String itemNoOrItemName
    ) {
        List<PurchaseInputResponse> purchaseRequests =
                purchaseInputRepo.findPurchaseRequestsByCondition(fromDate, toDate, wareHouseId, clientId, itemNoOrItemName);
        for (PurchaseInputResponse purchaseRequest : purchaseRequests) {
            // 입고수량
            List<Integer> purchaserInputAmounts = purchaseInputRepo.findInputAmountByPurchaseRequestId(purchaseRequest.getId());    // 현재 입고된 수량
            int inputAmountSum = purchaserInputAmounts.stream().mapToInt(Integer::intValue).sum();  // 현재 입고된 수량
            purchaseRequest.setInputAmount(inputAmountSum);

            purchaseRequest.setInputPrice();    // 입고금액
            purchaseRequest.setVat();           // 부가세
            purchaseRequest.setAlreadyInput(purchaseRequest.getOrderAmount(), inputAmountSum);      // 미입고수량 = 발주수량 - 입고수량
            purchaseRequest.setPartInputTestItemNo(purchaseRequest.getItemName() + " (" + purchaseRequest.getInputDate() + ")");
        }
        return purchaseRequests;
    }

    // ================================ 구매입고 LOT 정보 ================================
    // 구매입고 LOT 생성
    @Override
    public PurchaseInputDetailResponse createPurchaseInputDetail(
            Long purchaseRequestId,
            PurchaseInputRequest purchaseInputRequest
    ) throws NotFoundException, BadRequestException {
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);
        PurchaseInput purchaseInput = mapper.toEntity(purchaseInputRequest, PurchaseInput.class);

        // 구매요청의 요청수량 만큼 모두 입고 완료 햇을때 구매요청의 지시상태값을 완료(COMPLETION) 으로 변경한다.
        int requestAmount = purchaseRequest.getRequestAmount();     // 발주수량
        List<Integer> purchaserInputAmounts = purchaseInputRepo.findInputAmountByPurchaseRequestId(purchaseRequest.getId());    // 현재 입고된 수량
        int inputAmountSum = purchaserInputAmounts.stream().mapToInt(Integer::intValue).sum() + purchaseInputRequest.getInputAmount();  // 현재 입고된 수량 + 입력된 입고수량

        if (inputAmountSum == requestAmount) {
            purchaseRequest.putOrderStateChangedCompletion();       // 지시상태 값 변경
            purchaseRequestRepos.save(purchaseRequest);
        } else if (inputAmountSum > requestAmount) {
            throw new BadRequestException("요청수량보다 입고수량이 클 수 없습니다." +
                    " input 입고수량: " + purchaseInput.getInputAmount()
                    + " , 요청수량 : " + requestAmount);
        } else {
            purchaseRequest.setOrdersState(ONGOING);
        }

        purchaseInput.setPurchaseRequest(purchaseRequest);
        purchaseInputRepo.save(purchaseInput);  // lot 생성이 안되면 다시 삭제

        // lot 생성
        LotMasterRequest lotMasterRequest = new LotMasterRequest();
        lotMasterRequest.putPurchaseInputLotRequest(
                purchaseInput.getPurchaseRequest().getItem(),
                purchaseInput.getPurchaseRequest().getPurchaseOrder().getWareHouse(),
                purchaseInput,
                purchaseInput.getInputAmount(),
                purchaseInput.getInputAmount(),
//                getLotTypeOrThrow(purchaseInputRequest.getLotType()),
                purchaseInputRequest.isProcessYn()
        );

        lotHelper.createLotMaster(lotMasterRequest);

        // 구매요청에 입고일시 생성
        putInputDateToPurchaseRequest(purchaseRequest);

        return getPurchaseInputDetailResponse(purchaseRequest.getId(), purchaseInput.getId());
    }

    // 구매입고 LOT 전체 조회
    @Override
    public List<PurchaseInputDetailResponse> getPurchaseInputDetails(Long purchaseRequestId) throws NotFoundException {
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);
        return purchaseInputRepo.findPurchaseInputDetailByPurchaseRequestId(purchaseRequest.getId());
    }

    // 구메입고 LOT 단일 조회
    @Override
    public PurchaseInputDetailResponse getPurchaseInputDetailResponse(
            Long purchaseRequestId,
            Long purchaseInputId
    ) throws NotFoundException {
        return purchaseInputRepo.findPurchaseInputDetailByIdAndPurchaseInputId(purchaseRequestId, purchaseInputId)
                .orElseThrow(() -> new NotFoundException("purchaseInputDetail does not exist."));
    }

    // 구매입고 LOT 수정
    @Override
    public PurchaseInputDetailResponse updatePurchaseInputDetail(
            Long purchaseRequestId,
            Long purchaseInputId,
            PurchaseInputRequest.updateRequest purchaseInputUpdateRequest
    ) throws NotFoundException, BadRequestException {
        PurchaseInput findPurchaseInput = getPurchaseInputOrThrow(purchaseRequestId, purchaseInputId);

        // 해당하는 lot 의 재고수량, 생성수량 변경
        LotMaster lotMaster = getLotMasterOrThrow(findPurchaseInput);
        // 해당 lot 사용된거면 수정 불가능
        throwIfLotMasterCreateAmountEqualStockAmount(lotMaster);

        PurchaseInput newPurchaseInput = mapper.toEntity(purchaseInputUpdateRequest, PurchaseInput.class);
        findPurchaseInput.put(newPurchaseInput);

        lotMaster.updatePurchaseInput(findPurchaseInput.getInputAmount());
        lotMasterRepo.save(lotMaster);

        // 구매요청에 입고일시 생성
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);
        OrderState orderState = changePurchaseRequestOrderState(purchaseRequestId);
        purchaseRequest.setOrdersState(orderState);     // 상태값 변경
        putInputDateToPurchaseRequest(purchaseRequest);

        return getPurchaseInputDetailResponse(purchaseRequestId, purchaseInputId);
    }

    // 해당 lot 사용된거면 수정 불가능
    private void throwIfLotMasterCreateAmountEqualStockAmount(LotMaster lotMaster) throws BadRequestException {
        if (lotMaster.getCreatedAmount() != lotMaster.getStockAmount()) throw new BadRequestException("해당 LOT 는 사용된 LOT 이므로 삭제, 수정이 불가합니다.");
    }

    // 구매입고 LOT 삭제
    @Override
    public void deletePurchaseInputDetail(Long purchaseRequestId, Long purchaseInputId) throws NotFoundException, BadRequestException {
        // 구매입고 삭제
        PurchaseInput purchaseInput = getPurchaseInputOrThrow(purchaseRequestId, purchaseInputId);
        LotMaster lotMaster = getLotMasterOrThrow(purchaseInput);
        // 해당 LOT 사용되었으면 삭제 불가능
        throwIfLotMasterCreateAmountEqualStockAmount(lotMaster);
        purchaseInput.delete();
        // 구매입고 등록 시 생성되었던 lotMaster 삭제
        lotMaster.delete();

        purchaseInputRepo.save(purchaseInput);
        lotMasterRepo.save(lotMaster);

        // 구매요청에 입고 일시 생성
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);
        OrderState orderState = changePurchaseRequestOrderState(purchaseRequestId);
        purchaseRequest.setOrdersState(orderState);     // 상태값 변경
        putInputDateToPurchaseRequest(purchaseRequest);
    }

    // 구매요청에 상태값
    private OrderState changePurchaseRequestOrderState(Long purchaseRequestId) throws NotFoundException {
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);
        int inputAmount = purchaseInputRepo.findInputAmountByPurchaseRequestId(purchaseRequestId)
                .stream().mapToInt(Integer::intValue).sum();

        if (inputAmount == 0) return SCHEDULE;
        else if (inputAmount >= purchaseRequest.getRequestAmount()) return COMPLETION;
        else return ONGOING;
    }

    // 구매입고 LOT 단일 조회 및 예외
    private PurchaseInput getPurchaseInputOrThrow(Long purchaseRequestId, Long purchaseInputId) throws NotFoundException {      // 구매입고, 구매요청
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);    // 구매요청 조회
        return purchaseInputRepo.findByIdAndPurchaseRequestAndDeleteYnFalse(purchaseInputId, purchaseRequest)
                .orElseThrow(() -> new NotFoundException("purchaseInput does not exist. input purchaseInput id: " + purchaseInputId));
    }

    // LotMaster 단일 조회 및 예외
    private LotMaster getLotMasterOrThrow(PurchaseInput purchaseInput) throws NotFoundException {
        return lotMasterRepo.findByPurchaseInputAndDeleteYnFalse(purchaseInput)
                .orElseThrow(() -> new NotFoundException("lotMaster does not exist. input purchaseInputId: " + purchaseInput.getId()));
    }

    // 구매요청에 입고일시 생성
    // 구매요청에 해당하는 구매입고 중 제일 최근에 등록된 날짜
    private void putInputDateToPurchaseRequest(PurchaseRequest purchaseRequest) {
        LocalDateTime findInputDate = purchaseInputRepo.findCreatedDateByPurchaseRequestId(purchaseRequest.getId()).orElse(null);
        LocalDate inputDate = findInputDate != null ? LocalDate.from(findInputDate) : null;
        purchaseRequest.setInputDate(inputDate);
        purchaseRequestRepos.save(purchaseRequest);
    }

    private LotType getLotTypeOrThrow(Long id) throws NotFoundException {
        return lotTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("lotType does not exist. input id: " + id));
    }

    // ================================================= 9-4. 구매현황조회 =================================================
    // 구매현황 리스트 조회
    // 검색조건: 거래처 id, 품명|품목, 입고기간 fromDate~toDate
    @Override
    public List<PurchaseStatusCheckResponse> getPurchaseStatusChecks(
            Long clientId,
            String itemNoAndItemName,
            LocalDate fromDate,
            LocalDate toDate
    ) throws NotFoundException {
        List<PurchaseStatusCheckResponse> purchaseStatusCheckResponses = purchaseInputRepo.findPurchaseStatusCheckByCondition(clientId, itemNoAndItemName, fromDate, toDate);
        for (PurchaseStatusCheckResponse response : purchaseStatusCheckResponses) {
            PurchaseOrderStatusResponse purchaseOrderStatusResponse = purchaseOrderRepo.findPurchaseOrderStatusResponse(response.getPurchaseOrderId())
                    .orElseThrow(() -> new NotFoundException("[데이터오류] 구매입고에 대한 구매발주가 존재하지 않습니다."));
            response.setOrderPrice(purchaseOrderStatusResponse.getOrderPrice());
            response.setOrderAmount(purchaseOrderStatusResponse.getOrderAmount());
            response.setCancelAmount(purchaseOrderStatusResponse.getCancelAmount());
            response.setNote(purchaseOrderStatusResponse.getNote());
            response.setOrderState(purchaseOrderStatusResponse.getOrderState());
            response.setUserName(purchaseOrderStatusResponse.getUserName());
        }
        return purchaseStatusCheckResponses;
    }

    // 금일기준 자재입고 된 목록
    @Override
    public List<LabelPrintResponse> getTodayPurchaseInputs(LocalDate fromDate, LocalDate toDate) {
        return purchaseInputRepo.findByTodayAndPurchaseInput(fromDate, toDate);
    }
}
