package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.PurchaseRequestRequest;
import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.dto.response.ProduceRequestBomDetail;
import com.mes.mesBackend.dto.response.PurchaseRequestResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.ModifiedLog;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.ModifiedLogHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.PurchaseRequestRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.ItemService;
import com.mes.mesBackend.service.ProduceOrderService;
import com.mes.mesBackend.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.GoodsType.*;
import static com.mes.mesBackend.entity.enumeration.ModifiedDivision.PURCHASE_REQUEST;
import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;

// 9-1. 구매요청 등록
@Service
@RequiredArgsConstructor
public class PurchaseRequestServiceImpl implements PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepo;
    private final ProduceOrderService produceOrderService;
    private final ModelMapper mapper;
    private final ItemService itemService;
    private final ProduceOrderRepository produceOrderRepo;
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ModifiedLogHelper modifiedLogHelper;

    // 구매요청 생성
    @Override
    public PurchaseRequestResponse createPurchaseRequest(PurchaseRequestRequest purchaseRequestRequest, String userCode) throws NotFoundException, BadRequestException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(purchaseRequestRequest.getProduceOrder());

        // 총 구매요청수량이 수주수량을 초과하면 예외
//        throwIfPurchaseRequestAmountGreaterThanContractItemAmount(
//                produceOrder.getId(),
//                purchaseRequestRequest.getRequestAmount(),
//                produceOrder.getContractItem().getAmount()
//        );

        /*
        * 구매요청 품목정보는 produceOrder 의 contractItem 의 item 을 찾아서
        * item 에 해당하는 bomMaster 의 데이터에 해당되는
        * bomMasterDetail 의 item 만 등록 할 수 있다.
        * produceOrder.contract.Item: 완재품
        * bomMasterDetail: 원자재, 부자재 등등
         * */
        List<Long> findItemIds = purchaseRequestRepo.findItemIdByContractItemId(produceOrder.getContractItem().getItem().getId());

        // 입력받은 itemId 가 findItemIds 에 해당되는지 체크
//        Item item = getItemAndCheckItemId(purchaseRequestRequest.getItemId(), findItemIds);

        Item item = itemService.getItemOrThrow(purchaseRequestRequest.getItemId());

        PurchaseRequest purchaseRequest = mapper.toEntity(purchaseRequestRequest, PurchaseRequest.class);

        purchaseRequest.mapping(produceOrder, item);
        // 구매요청 등록의 첫 등록은 지시상태 schedule 로 등록됨.
        purchaseRequest.setOrdersState(SCHEDULE);

        PurchaseRequest save = purchaseRequestRepo.save(purchaseRequest);

        modifiedLogHelper.createInsertLog(userCode, PURCHASE_REQUEST, purchaseRequest);
        return getPurchaseRequestResponseOrThrow(save.getId());
    }

    // 구매요청 리스트 조회, 검색조건: 요청기간, 제조오더번호, 품목그룹, 품번|품명, 제조사 품번, 완료포함(check)
    @Override
    public List<PurchaseRequestResponse> getPurchaseRequests(
            LocalDate fromDate,
            LocalDate toDate,
            String produceOrderNo,
            Long itemGroupId,
            String itemNoAndName,
            String manufacturerPartNo,
            Boolean orderCompletion
    ) {
        List<PurchaseRequestResponse> responses = purchaseRequestRepo.findAllByCondition(fromDate, toDate, produceOrderNo, itemGroupId, itemNoAndName, manufacturerPartNo, orderCompletion);
        for (PurchaseRequestResponse r : responses) {
            ModifiedLog modifiedLog = modifiedLogHelper.getModifiedLog(PURCHASE_REQUEST, r.getId());
            ModifiedLog insertLog = modifiedLogHelper.getInsertLog(PURCHASE_REQUEST, r.getId());
            if (modifiedLog != null) r.modifiedLog(modifiedLog);
            if (insertLog != null) r.insertLog(insertLog);
        }
        return responses;
    }

    // 구매요청 수정
    @Override
    public PurchaseRequestResponse updatePurchaseRequest(Long id, PurchaseRequestRequest newPurchaseRequestRequest, String userCode) throws NotFoundException, BadRequestException {
        PurchaseRequest findPurchaseRequest = getPurchaseRequestOrThrow(id);
        ProduceOrder newProduceOrder = produceOrderService.getProduceOrderOrThrow(newPurchaseRequestRequest.getProduceOrder());

        // 구매발주에 등록 된 구매요청은 수정 불가능
        throwIfPurchaseRequestInPurchaseOrder(findPurchaseRequest);
        // 구매요청이 ONGOING, COMPLETION 이면 수정 불가능
        throwIfPurchaseRequestOrderStateNotSchedule(findPurchaseRequest.getOrdersState());

//        // 총 구매요청수량이 수주수량을 초과하면 예외
//        throwIfPurchaseRequestAmountGreaterThanContractItemAmount(
//                newProduceOrder.getId(),
//                newPurchaseRequestRequest.getRequestAmount(),
//                newProduceOrder.getContractItem().getAmount()
//        );

//        List<Long> findItemIds = purchaseRequestRepo.findItemIdByContractItemId(newProduceOrder.getContractItem().getItem().getId());

//        Item newItem = getItemAndCheckItemId(newPurchaseRequestRequest.getItemId(), findItemIds);
        Item newItem = itemService.getItemOrThrow(newPurchaseRequestRequest.getItemId());

        PurchaseRequest newPurchaseRequest = mapper.toEntity(newPurchaseRequestRequest, PurchaseRequest.class);

        findPurchaseRequest.update(newPurchaseRequest, newProduceOrder, newItem);
        purchaseRequestRepo.save(findPurchaseRequest);
        modifiedLogHelper.createModifiedLog(userCode, PURCHASE_REQUEST, findPurchaseRequest);   // 업데이트 로그 생성
        return getPurchaseRequestResponseOrThrow(findPurchaseRequest.getId());
    }

    // 구매요청 삭제
    @Override
    public void deletePurchaseRequest(Long id) throws NotFoundException, BadRequestException {
        PurchaseRequest purchaseRequest = getPurchaseRequestOrThrow(id);
        // 구매발주에 등록 된 구매요청은 수정 불가능
        throwIfPurchaseRequestInPurchaseOrder(purchaseRequest);
        // 구매요청이 ONGOING, COMPLETION 이면 삭제 불가능
        throwIfPurchaseRequestOrderStateNotSchedule(purchaseRequest.getOrdersState());
        purchaseRequest.delete();
        purchaseRequestRepo.save(purchaseRequest);
    }

    // 구매요청 단일 조회 및 예외
    @Override
    public PurchaseRequest getPurchaseRequestOrThrow(Long id) throws NotFoundException {
        return purchaseRequestRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("purchaseRequest does not exist. input purchaseRequest id: " + id));
    }

    private void throwIfPurchaseRequestAmountGreaterThanContractItemAmount(Long produceOrderId, int requestAmount, int contractItemAmount) throws BadRequestException {
        // 총 구매요청수량이 수주수량을 초과하면 예외
        List<Integer> orderAmounts = purchaseRequestRepo.findRequestAmountByProduceOrderId(produceOrderId);
        int orderAmountSum = orderAmounts.stream().mapToInt(Integer::intValue).sum();
        if (orderAmountSum+requestAmount > contractItemAmount) {
            throw new BadRequestException("total purchaseRequestAmount cannot be greater than contractItemAmount. " +
                    "input requestAmount: " + requestAmount + ", " +
                    "total requestAmount: " + orderAmountSum + ", " +
                    "contractItemAmount: " + contractItemAmount);
        }
    }

    // 구매발주에 등록 된 구매요청은 수정이나 삭제 불가능
    private void throwIfPurchaseRequestInPurchaseOrder(PurchaseRequest purchaseRequest) throws BadRequestException {
        if (purchaseRequest.getPurchaseOrder() != null)
            throw new BadRequestException("발주에 등록 된 구매요청은 수정, 삭제가 불가합니다. 발주에서 구매요청 삭제 후 다시 시도해 주세요.");
    }

    // 구매요청의 orderState 가 ONGOING, COMPLETION 이면 수정이나 삭제 불가능
    private void throwIfPurchaseRequestOrderStateNotSchedule(OrderState orderState) throws BadRequestException {
        if (!orderState.equals(SCHEDULE)) throw new BadRequestException("진행중이거나 완료가 된 구매요청은 수정이나 삭제를 할 수 없습니다.");
    }

    // 입력받은 itemId 가 findItemIds 에 해당되는지 체크 후 맞다면 item 반환
    private Item getItemAndCheckItemId(Long itemId, List<Long> findItemIds) throws BadRequestException, NotFoundException {
        boolean checkItemId = findItemIds.stream().noneMatch(id -> id.equals(itemId));
        if(checkItemId) {
            throw new BadRequestException("입력된 item 이 해당하는 bomDetail 에 등록 되지 않은 item 입니다. 등록된 itemIds: " + findItemIds + ", input itemId: " + itemId);
        }
        return itemService.getItemOrThrow(itemId);
    }

    // 구매요청 단일조회
    @Override
    public PurchaseRequestResponse getPurchaseRequestResponseOrThrow(Long id) throws NotFoundException {
        return purchaseRequestRepo.findByIdAndOrderStateSchedule(id)
                .orElseThrow(() -> new NotFoundException("purchaseRequest does not exist. input purchaseRequest id : " + id));
    }

    // 수주품목에 해당하는 원부자재
    @Override
    public List<ProduceRequestBomDetail> getProduceOrderBomDetails(Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = getProduceOrderOrThrow(produceOrderId);
        Item item = produceOrder.getContractItem().getItem();

        List<ProduceOrderDetailResponse> produceDetails = produceOrderRepo.findAllProduceOrderDetail(item.getId());
        List<ProduceRequestBomDetail> responses = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        for (ProduceOrderDetailResponse res : produceDetails) {

            if (res.getGoodsType().equals(RAW_MATERIAL) || res.getGoodsType().equals(SUB_MATERIAL)) {
                Item item1 = itemService.getItemOrThrow(res.getItemId());
                items.add(item1);
            } else {
                items.addAll(workOrderDetailRepo.findBomDetailItemByBomMasterItem(res.getItemId(), null));
            }
        }

        for (Item item1 : items) {
            ProduceRequestBomDetail detail = new ProduceRequestBomDetail();
            responses.add(detail.converter(item1));
        }
        return responses.stream().filter(f -> !f.getGoodsType().equals(HALF_PRODUCT)).collect(Collectors.toList());
    }

    // 제조 오더 단일 조회 및 예외
    private ProduceOrder getProduceOrderOrThrow(Long id) throws NotFoundException {
        return produceOrderRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("produceOrder does not exist. id : " + id));
    }
}
