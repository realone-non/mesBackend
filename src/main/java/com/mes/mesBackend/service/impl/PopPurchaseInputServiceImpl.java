package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.response.PopPurchaseOrderResponse;
import com.mes.mesBackend.dto.response.PopPurchaseRequestResponse;
import com.mes.mesBackend.entity.PurchaseInput;
import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.PurchaseInputRepository;
import com.mes.mesBackend.repository.PurchaseOrderRepository;
import com.mes.mesBackend.repository.PurchaseRequestRepository;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PopPurchaseInputService;
import com.mongodb.MongoNamespace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PURCHASE_INPUT;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.REAL_LOT;
import static com.mes.mesBackend.entity.enumeration.OrderState.*;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.MATERIAL_INPUT;

@Service
@RequiredArgsConstructor
public class PopPurchaseInputServiceImpl implements PopPurchaseInputService {
    private final PurchaseOrderRepository purchaseOrderRepo;
    private final PurchaseInputRepository purchaseInputRepo;
    private final PurchaseRequestRepository purchaseRequestRepo;
    private final LotMasterService lotMasterService;

    // 구매발주 등록이 완료 된 구매발주 리스트 GET
    @Override
    public List<PopPurchaseOrderResponse> getPurchaseOrders(String clientName, OrderState orderState) {
        List<PopPurchaseOrderResponse> response = purchaseOrderRepo.findPopPurchaseOrderResponses(clientName);
        List<PopPurchaseOrderResponse> conditionResponse = new ArrayList<>();

        for (PopPurchaseOrderResponse res : response) {
            OrderState purchaseOrderState = findOrderState(res.getPurchaseOrderId());
            res.setOrderState(purchaseOrderState);
        }

        if (orderState != null) {
            if (orderState.equals(COMPLETION)) {
                for (PopPurchaseOrderResponse res : response) {
                    if (res.getOrderState().equals(COMPLETION)) {
                        conditionResponse.add(res);
                    }
                }
            } else if (orderState.equals(ONGOING)) {
                for (PopPurchaseOrderResponse res : response) {
                    if (res.getOrderState().equals(ONGOING)) {
                        conditionResponse.add(res);
                    }
                }
            } else if (orderState.equals(SCHEDULE)) {
                for (PopPurchaseOrderResponse res : response) {
                    if (res.getOrderState().equals(SCHEDULE)) {
                        conditionResponse.add(res);
                    }
                }
            }
            return conditionResponse;
        }
        return response;
    }

    private OrderState findOrderState(Long purchaseOrderId) {
        // 해당하는 구매발주상세의 지시상태를 모두 가져옴
        List<OrderState> orderStates = purchaseOrderRepo.findOrderStateByPurchaseOrderId(purchaseOrderId);
        return orderStates.stream().allMatch(state -> state.equals(SCHEDULE)) ? SCHEDULE
                : orderStates.stream().allMatch(state -> state.equals(COMPLETION)) ? COMPLETION
                : ONGOING;
    }

    // 구매발주에 등록 된 구매요청 리스트 GET
    @Override
    public List<PopPurchaseRequestResponse> getPurchaseRequests(Long id) {
        List<PopPurchaseRequestResponse> response = purchaseRequestRepo.findPopPurchaseRequestResponseByPurchaseOrderId(id);
        for (PopPurchaseRequestResponse res : response) {
            int allInputAmount = purchaseInputRepo.findInputAmountByPurchaseRequestId(res.getPurchaseRequestId())
                    .stream().mapToInt(Integer::intValue).sum();    // 현재 입고된 수량
            res.setPurchaseInputAmount(allInputAmount);       // 입고수량
        }
        return response;
    }

    // 구매요청에 대한 구매입고(request: 수량) POST
    @Override
    public Long createPurchaseInput(Long purchaseRequestId, int inputAmount) throws NotFoundException, BadRequestException {
        PurchaseRequest purchaseRequest = getPurchaseRequestOrThrow(purchaseRequestId);

        if (inputAmount == 0) {
            throw new BadRequestException("입고수량이 0일 수 없습니다.");
        }

        int purchaseInputAmount = purchaseInputRepo.findInputAmountByPurchaseRequestId(purchaseRequest.getId())
                .stream().mapToInt(Integer::intValue).sum();        // 현재 입고된 수량

        int allInputAmount = purchaseInputAmount + inputAmount;   // 현재 입고된 수량 + 입력된 입고수량

        // orderState 변경: 구매요청의 발주수량 만큼 모두 입고 완료 햇을때 구매요청의 지시상태값을 완료(COMPLETION) 으로 변경한다.
        purchaseRequest.setOrdersState(ONGOING);    // 입고되면 ONGOING 으로
        if (allInputAmount >= purchaseRequest.getOrderAmount()) purchaseRequest.putOrderStateChangedCompletion();

        PurchaseInput purchaseInput = new PurchaseInput();
        purchaseInput.setPurchaseRequest(purchaseRequest);
        purchaseInput.setInputAmount(inputAmount);
        purchaseInputRepo.save(purchaseInput);

        putInputDateToPurchaseRequest(purchaseRequest);
        purchaseRequestRepo.save(purchaseRequest);

        // lot 생성
        LotMasterRequest lotMasterRequest = new LotMasterRequest();
        lotMasterRequest.setPurchaseInputId(purchaseInput.getId()); // 구매입고
        lotMasterRequest.setItem(purchaseRequest.getItem());        // 품목
        lotMasterRequest.setLotTypeId(purchaseRequest.getItem().getLotType().getId());      // Lot 유형
        lotMasterRequest.setEnrollmentType(PURCHASE_INPUT); // 등록유형
        lotMasterRequest.setCreatedAmount(inputAmount);     // lotMaster 생성수량
        lotMasterRequest.setStockAmount(inputAmount);       // lotMaster 재고수량
        lotMasterRequest.setWareHouse(purchaseInput.getPurchaseRequest().getPurchaseOrder().getWareHouse());        // 구매발주의 입고창고
        lotMasterRequest.setWorkProcessDivision(MATERIAL_INPUT);
        lotMasterRequest.setLotMasterDivision(REAL_LOT);

        String lotMaster = lotMasterService.createLotMaster(lotMasterRequest).getLotNo();  // lotMaster 생성

        if (lotMaster == null) {
            purchaseInputRepo.deleteById(purchaseInput.getId());
            throw new BadRequestException("lot 번호가 생성되지 않았습니다.");
        }
        return purchaseInput.getId();
    }

    // 구매요청 단일 조회 및 예외
    private PurchaseRequest getPurchaseRequestOrThrow(Long id) throws NotFoundException {
        return purchaseRequestRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("purchaseRequest does not exist. input purchaseRequest id: " + id));
    }

    // 구매요청에 입고일시 생성
    // 구매요청에 해당하는 구매입고 중 제일 최근에 등록된 날짜
    private void putInputDateToPurchaseRequest(PurchaseRequest purchaseRequest) {
        LocalDateTime findInputDate = purchaseInputRepo.findCreatedDateByPurchaseRequestId(purchaseRequest.getId()).orElse(null);
        LocalDate inputDate = findInputDate != null ? LocalDate.from(findInputDate) : null;
        purchaseRequest.setInputDate(inputDate);
        purchaseRequestRepo.save(purchaseRequest);
    }
}
