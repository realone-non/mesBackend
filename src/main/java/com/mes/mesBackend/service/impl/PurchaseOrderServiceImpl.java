package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.PurchaseOrderDetailRequest;
import com.mes.mesBackend.dto.request.PurchaseOrderRequest;
import com.mes.mesBackend.dto.response.PurchaseOrderDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseOrderResponse;
import com.mes.mesBackend.dto.response.PurchaseOrderStatusResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.NumberAutomatic;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.PayTypeRepository;
import com.mes.mesBackend.repository.PurchaseInputRepository;
import com.mes.mesBackend.repository.PurchaseOrderRepository;
import com.mes.mesBackend.repository.PurchaseRequestRepository;
import com.mes.mesBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;

// 9-2. 구매발주 등록
@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepo;
    private final ModelMapper mapper;
    private final UserService userService;
    private final WareHouseService wareHouseService;
    private final CurrencyService currencyService;
    private final NumberAutomatic numberAutomatic;
    private final PurchaseRequestService purchaseRequestService;
    private final PurchaseRequestRepository purchaseRequestRepo;
    private final PurchaseInputRepository purchaseInputRepo;
    private final PayTypeRepository payTypeRepo;

    // 구매발주 생성
    @Override
    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) throws NotFoundException {
        User user = userService.getUserOrThrow(purchaseOrderRequest.getUser());
        WareHouse wareHouse = wareHouseService.getWareHouseOrThrow(purchaseOrderRequest.getWareHouse());
        Currency currency = currencyService.getCurrencyOrThrow(purchaseOrderRequest.getCurrency());
        PayType payType = purchaseOrderRequest.getPayType() != null ? getPayTypeOrThrow(purchaseOrderRequest.getPayType()) : null;

        PurchaseOrder purchaseOrder = mapper.toEntity(purchaseOrderRequest, PurchaseOrder.class);
        purchaseOrder.mapping(user, wareHouse, currency, payType);

        // 발주번호 생성
        String purchaseOrderNo = numberAutomatic.createDateTimeNo();
        purchaseOrder.setPurchaseOrderNo(purchaseOrderNo);

        purchaseOrderRepo.save(purchaseOrder);
        return getPurchaseOrderResponseOrThrow(purchaseOrder.getId());
    }

    // 구매발주 지시상태
    private OrderState findOrderState(Long purchaseOrderId) {
        // 해당하는 구매발주상세의 지시상태를 모두 가져옴
        List<OrderState> orderStates = purchaseOrderRepo.findOrderStateByPurchaseOrderId(purchaseOrderId);

        // 요소가 empty 면 SCHEDULE 반환.
        // 모든 요소들이 주어진 조건을 하나라도 만족하면 true 반환.
        return orderStates.stream().allMatch(state -> state.equals(SCHEDULE)) ? SCHEDULE : orderStates.stream().allMatch(state -> state.equals(COMPLETION)) ? COMPLETION : ONGOING;
    }

    // 구매발주의 납기일자
    private LocalDate findPeriodDate(Long purchaseOrderId) {
        // periodDate 상세정보에 등록된 제일 마지막 날짜를 가져옴
        return purchaseOrderRepo.findPeriodDateByPurchaseOrderId(purchaseOrderId);
    }

    // 구매발주 단일 조회
    @Override
    public PurchaseOrderResponse getPurchaseOrderResponseOrThrow(Long purchaseOrderId) throws NotFoundException {
        PurchaseOrderResponse purchaseOrderResponse = purchaseOrderRepo.findPurchaseOrderByIdAndDeleteYn(purchaseOrderId)
                .orElseThrow(() -> new NotFoundException("purchaseOrder does not exist. input purchaseOrderId: " + purchaseOrderId));

        purchaseOrderResponse.setOrderStateAndPeriodDateAndContractInfo(
                findOrderState(purchaseOrderResponse.getId()),
                findPeriodDate(purchaseOrderResponse.getId())
        );

        return purchaseOrderResponse;
    }

    // 구매발주 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간, 완료포함(check)
    @Override
    public List<PurchaseOrderResponse> getPurchaseOrders(
            Long currencyId,
            Long userId,
            Long clientId,
            Long wareHouseId,
            LocalDate fromDate,
            LocalDate toDate,
            boolean orderCompletion
    ) {
        List<PurchaseOrderResponse> purchaseOrders =
                purchaseOrderRepo.findAllByCondition(currencyId, userId, clientId, wareHouseId, fromDate, toDate);

        purchaseOrders.forEach(
                purchaseOrder ->
                        purchaseOrder.setOrderStateAndPeriodDateAndContractInfo(
                                findOrderState(purchaseOrder.getId()),
                                findPeriodDate(purchaseOrder.getId())
                        )
        );
        // 요청번호 추가: 구매발주에 등록 된 구매요청 1개(조건: 등록순 asc)
        purchaseOrders.forEach(
                purchaseOrder ->
                        purchaseOrder.setPurchaseRequestId(purchaseOrderRepo.findOneByPurchaseRequestId(purchaseOrder.getId())
                                .orElse(null))
        );

        // 검색조건 orderCompletion 에 대한 필터
        List<PurchaseOrderResponse> purchaseOrdersByOrderStateCompletion = new ArrayList<>();
        if (orderCompletion) {
            for (PurchaseOrderResponse purchaseOrder : purchaseOrders) {
                PurchaseOrderResponse orderStateCompletion = purchaseOrder.orderStateCondition();
                if (orderStateCompletion != null) purchaseOrdersByOrderStateCompletion.add(orderStateCompletion);
            }
            return purchaseOrdersByOrderStateCompletion;
        }
        return purchaseOrders;
    }

    // 구매발주 수정
    @Override
    public PurchaseOrderResponse updatePurchaseOrder(Long purchaseOrderId, PurchaseOrderRequest newPurchaseOrderRequest) throws NotFoundException {
        PurchaseOrder findPurchaseOrder = getPurchaseOrderOrThrow(purchaseOrderId);
        User newUser = userService.getUserOrThrow(newPurchaseOrderRequest.getUser());
        WareHouse newWareHouse = wareHouseService.getWareHouseOrThrow(newPurchaseOrderRequest.getWareHouse());
        Currency newCurrency = currencyService.getCurrencyOrThrow(newPurchaseOrderRequest.getCurrency());
        PayType newPayType = newPurchaseOrderRequest.getPayType() != null ? getPayTypeOrThrow(newPurchaseOrderRequest.getPayType()) : null;

        PurchaseOrder newPurchaseOrder = mapper.toEntity(newPurchaseOrderRequest, PurchaseOrder.class);

        findPurchaseOrder.update(newPurchaseOrder, newUser, newWareHouse, newCurrency, newPayType);
        purchaseOrderRepo.save(findPurchaseOrder);

        return getPurchaseOrderResponseOrThrow(findPurchaseOrder.getId());
    }

    // 구매발주 삭제
    @Override
    public void deletePurchaseOrder(Long purchaseOrderId) throws NotFoundException, BadRequestException {
        PurchaseOrder purchaseOrder = getPurchaseOrderOrThrow(purchaseOrderId);
        // 구매발주에 해당하는 구매요청이 존재하는지
        throwIfPurchasRequestExistsInPurchaseOrder(purchaseOrder);
        // 구매발주에서만 제거 orderState 변경 ONGOING -> SCHEDULE
//        List<PurchaseRequest> purchaseRequests = purchaseRequestRepo.findAllByPurchaseOrderAndDeleteYnFalse(purchaseOrder);
//        purchaseRequests.forEach(PurchaseRequest::deleteFromPurchaseOrderAndOrderStateChangedSchedule);
//        purchaseRequestRepo.saveAll(purchaseRequests);
        // 구매발주 삭제, client null
        purchaseOrder.delete();
        purchaseOrderRepo.save(purchaseOrder);
    }

    // 구매발주에 해당하는 구매요청이 존재하는지
    private void throwIfPurchasRequestExistsInPurchaseOrder(PurchaseOrder purchaseOrder) throws BadRequestException {
        boolean b = purchaseRequestRepo.existsByPurchaseOrderAndDeleteYnFalse(purchaseOrder);
        if (b) throw new BadRequestException("구매발주에 해당하는 구매발주상세 정보가 존재하므로 삭제할수 없습니다. 구매발주 상세정보를 삭제 후 다시 시도해주세요.");
    }

    // 구매발주 단일 조회 및 예외
    private PurchaseOrder getPurchaseOrderOrThrow(Long id) throws NotFoundException {
        return purchaseOrderRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("purchaseOrder does not exist. input purchaseOrderId: " + id));
    }

    // ================================================================ 구매발주상세 ================================================================

    // 구매발주상세 생성
    @Override
    public PurchaseOrderDetailResponse createPurchaseOrderDetail(
            Long purchaseOrderId,
            PurchaseOrderDetailRequest purchaseOrderDetailRequest
    ) throws NotFoundException, BadRequestException {
        PurchaseOrder purchaseOrder = getPurchaseOrderOrThrow(purchaseOrderId);
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseOrderDetailRequest.getPurchaseRequestId());

        // 구매요청정보가 다른 구매발주에 중복으로 등록 되어있는지에 대한 체크
        if (purchaseRequest.getPurchaseOrder() != null)
            throw new BadRequestException("해당 구매요청정보는 다른 구매발주에 등록되어 있습니다. " +
                    "input purchaseOrderId: " + purchaseOrderId + ", input purchaseRequestId: " + purchaseRequest.getId());

        // 요청수량과 발주수량은 같아야함.
        if (purchaseRequest.getRequestAmount() != purchaseOrderDetailRequest.getOrderAmount()) {
            throw new BadRequestException("요청수량과 발수수량이 같아야합니다. " +
                    "요청수량: " + purchaseRequest.getRequestAmount() + ", " +
                    "입력 발주수량: " + purchaseOrderDetailRequest.getOrderAmount()
            );
        }
        /*
        * 생성 전 체크 항목
        * if 해당 구매발주에 해당하는 구매요청이 있는지 ?
        * -> 구매발주의 거래처 생성
        * else if 해당 구매발주에 한개라도 해당하는 구매요청이 있으면 ?
        * -> 구매발주에 등록 된 거래처를 기준으로 삼아서 해당 거래처에 해당하는 구매요청만 등록가능
        * */
        List<Long> clientIds = purchaseRequestRepo.findClientIdsByPurchaseOrder(purchaseOrder.getId());

        // 구매요청에 해당하는 구매발주를 집어넣고 구매요청의 orderState 의 상태값을 SCHEDULE -> ONGOING 으로 변경
        // SCHEDULE 로 변경
        purchaseRequest.putPurchaseOrderAndOrderStateChangedOngoing(purchaseOrder);
        purchaseRequest.setOrderAmount(purchaseOrderDetailRequest.getOrderAmount());     // 발주수량 변경

        if (clientIds.isEmpty()) {
            purchaseRequestRepo.save(purchaseRequest);
            Client manufacturer = purchaseRequest.getItem().getManufacturer();
            purchaseOrder.setClient(manufacturer);
            purchaseOrderRepo.save(purchaseOrder);
        }
        else {
            Client purchaseOrderClient = purchaseOrder.getClient();
            Client purchaseRequestClient = purchaseRequest.getItem().getManufacturer();
            if (purchaseRequestClient.equals(purchaseOrderClient))
                purchaseRequestRepo.save(purchaseRequest);
            else
                throw new BadRequestException("기존에 등록한 구매발주상세 거래처와 현재 입력한 구매발주상세의 거래처가 다릅니다.");
        }
        purchaseRequestRepo.save(purchaseRequest);
        return getPurchaseOrderDetailResponse(purchaseOrder.getId(), purchaseRequest.getId());
    }

    // 구매발주상세 단일 조회
    @Override
    public PurchaseOrderDetailResponse getPurchaseOrderDetailResponse(
            Long purchaseOrderId,
            Long purchaseOrderDetailId
    ) throws NotFoundException, BadRequestException {
        PurchaseRequest purchaseRequest = getPurchaseRequestOrThrow(purchaseOrderId, purchaseOrderDetailId);
        PurchaseOrderDetailResponse response = purchaseOrderRepo.findPurchaseRequestByIdAndPurchaseOrderIdAndDeleteYnFalse(purchaseOrderId, purchaseRequest.getId())
                .orElseThrow(() -> new NotFoundException("해당 구매발주에 해당하는 구매발주상세 정보가 없습니다. input purchaseOrderId: " + purchaseOrderId
                        + ", input purchaseRequestId: " + purchaseOrderDetailId));
        // 구매요청에 해당하는 구매입고의 입고수량 모두
        int allInputAmount = purchaseInputRepo.findInputAmountByPurchaseRequestId(response.getId())
                .stream().mapToInt(Integer::intValue).sum();
        return response.putOrderPossibleAmountAndInputAmount(allInputAmount);
    }

    // 구매발주상세 전체 조회
    @Override
    public List<PurchaseOrderDetailResponse> getPurchaseOrderDetails(Long purchaseOrderId) throws NotFoundException {
        PurchaseOrder purchaseOrder = getPurchaseOrderOrThrow(purchaseOrderId);
        List<PurchaseOrderDetailResponse> responses = purchaseOrderRepo.findAllByPurchaseOrderIdAndDeleteYnFalse(purchaseOrder.getId());
        for (PurchaseOrderDetailResponse res : responses) {
            // 구매요청에 해당하는 구매입고의 입고수량 모두
            int allInputAmount = purchaseInputRepo.findInputAmountByPurchaseRequestId(res.getId())
                    .stream().mapToInt(Integer::intValue).sum();
            res.putOrderPossibleAmountAndInputAmount(allInputAmount);
        }
        return responses;
    }

    // 구매발주상세 수정 (비고 수정 가능)
    @Override
    public PurchaseOrderDetailResponse updatePurchaseOrderDetail(
            Long purchaseOrderId,
            Long purchaseOrderDetailId,
            String note
    ) throws NotFoundException, BadRequestException {
        PurchaseRequest findPurchaseRequest = getPurchaseRequestOrThrow(purchaseOrderId, purchaseOrderDetailId);
        findPurchaseRequest.setNote(note);
        purchaseRequestRepo.save(findPurchaseRequest);
        return getPurchaseOrderDetailResponse(purchaseOrderId, purchaseOrderDetailId);
    }

    // 구매발주상세 삭제
    // 상세에 해당하는 구매발주가 하나일 땐 purchaseOrder 의 client 를 null 로 변경해야함
    @Override
    public void deletePurchaseOrderDetail(Long purchaseOrderId, Long purchaseOrderDetailId) throws NotFoundException, BadRequestException {
        PurchaseRequest purchaseRequest = getPurchaseRequestOrThrow(purchaseOrderId, purchaseOrderDetailId);
        if (!purchaseRequest.getOrdersState().equals(SCHEDULE)) throw new BadRequestException("진행중이거나 완료된 정보는 삭제할수없습니다.");

        // 구매발주에서 제거, orderState 변경 ONGOING -> SCHEDULE
        purchaseRequest.deleteFromPurchaseOrderAndOrderStateChangedSchedule();
        purchaseRequestRepo.save(purchaseRequest);

        // 구매발주에 해당하는 구매요청이 하나라도 없으면 구매발주의 client 를 null 로 변경함
        PurchaseOrder purchaseOrder = getPurchaseOrderOrThrow(purchaseOrderId);
        boolean b = purchaseRequestRepo.existsPurchaseRequestByPurchaseOrder(purchaseOrderId);
        if (b) purchaseOrder.setClient(null);
        purchaseOrderRepo.save(purchaseOrder);
    }

    // 구매발주, 구매요청 id 를 기준으로 해당하는 구매요청 조회
    private PurchaseRequest getPurchaseRequestOrThrow(Long purchaseOrderId, Long purchaseOrderDetailId) throws BadRequestException, NotFoundException {
        PurchaseOrder purchaseOrder = getPurchaseOrderOrThrow(purchaseOrderId);
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseOrderDetailId);
        return purchaseRequestRepo.findByIdAndPurchaseOrderAndDeleteYnFalse(purchaseRequest.getId(), purchaseOrder)
                .orElseThrow(() -> new BadRequestException("해당 구매발주에 해당하는 구매발주상세 정보가 없습니다. input purchaseOrderId: " + purchaseOrderId
                        + ", input purchaseRequestId: " + purchaseOrderDetailId));
    }

    // 결제조건 단일 조회 및 예외
    private PayType getPayTypeOrThrow(Long id) throws NotFoundException {
        return payTypeRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("payType does not exist. input id: " + id));
    }

    // ================================================================ 9-3. 구매발주현황조회 ================================================================
    // 발주현황 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간 fromDate~toDate
    @Override
    public List<PurchaseOrderStatusResponse> getPurchaseOrderStatuses(
            Long currencyId,
            Long userId,
            Long clientId,
            Long wareHouseId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return purchaseOrderRepo.findPurchaseOrderStatusResponseAllByCondition(currencyId, userId, clientId, wareHouseId, fromDate, toDate);
    }
}
