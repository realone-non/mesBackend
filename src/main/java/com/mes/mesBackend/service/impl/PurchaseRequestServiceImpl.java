package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.PurchaseRequestRequest;
import com.mes.mesBackend.dto.response.PurchaseRequestResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.PurchaseRequestRepository;
import com.mes.mesBackend.service.ItemService;
import com.mes.mesBackend.service.ProduceOrderService;
import com.mes.mesBackend.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;

// 9-1. 구매요청 등록
@Service
@RequiredArgsConstructor
public class PurchaseRequestServiceImpl implements PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepo;
    private final ProduceOrderService produceOrderService;
    private final ModelMapper mapper;
    private final ItemService itemService;

    // 구매요청 생성
    @Override
    public PurchaseRequestResponse createPurchaseRequest(PurchaseRequestRequest purchaseRequestRequest) throws NotFoundException, BadRequestException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(purchaseRequestRequest.getProduceOrder());
        /*
        * 구매요청 품목정보는 produceOrder 의 contractItem 의 item 을 찾아서
        * item 에 해당하는 bomMaster 의 데이터에 해당되는
        * bomMasterDetail 의 item 만 등록 할 수 있다.
        * produceOrder.contract.Item: 완재품
        * bomMasterDetail: 원자재, 부자재 등등
         * */
        List<Long> findItemIds = purchaseRequestRepo.findItemIdByContractItemId(produceOrder.getContractItem().getId());

        // 입력받은 itemId 가 findItemIds 에 해당되는지 체크
        Item item = getItemAndCheckItemId(purchaseRequestRequest.getItemId(), findItemIds);

        PurchaseRequest purchaseRequest = mapper.toEntity(purchaseRequestRequest, PurchaseRequest.class);

        purchaseRequest.mapping(produceOrder, item);
        // 구매요청 등록의 첫 등록은 지시상태 schedule 로 등록됨.
        purchaseRequest.setOrdersState(SCHEDULE);

        PurchaseRequest save = purchaseRequestRepo.save(purchaseRequest);
        return getPurchaseRequestResponseOrThrow(save.getId());
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

    // 구매요청 리스트 조회, 검색조건: 요청기간, 제조오더번호, 품목그룹, 품번|품명, 제조사 품번, 완료포함(check)
    @Override
    public List<PurchaseRequestResponse> getPurchaseRequests(
            LocalDate fromDate,
            LocalDate toDate,
            String produceOrderNo,
            Long itemGroupId,
            String itemNoAndName,
            String manufacturerPartNo,
            boolean orderCompletion
    ) {
        return purchaseRequestRepo.findAllByCondition(fromDate, toDate, produceOrderNo, itemGroupId, itemNoAndName, manufacturerPartNo, orderCompletion);
    }

    // 구매요청 수정
    @Override
    public PurchaseRequestResponse updatePurchaseRequest(Long id, PurchaseRequestRequest newPurchaseRequestRequest) throws NotFoundException, BadRequestException {
        PurchaseRequest findPurchaseRequest = getPurchaseRequestOrThrow(id);
        ProduceOrder newProduceOrder = produceOrderService.getProduceOrderOrThrow(newPurchaseRequestRequest.getProduceOrder());

        List<Long> findItemIds = purchaseRequestRepo.findItemIdByContractItemId(newProduceOrder.getContractItem().getId());

        Item newItem = getItemAndCheckItemId(newPurchaseRequestRequest.getItemId(), findItemIds);

        PurchaseRequest newPurchaseRequest = mapper.toEntity(newPurchaseRequestRequest, PurchaseRequest.class);

        findPurchaseRequest.update(newPurchaseRequest, newProduceOrder, newItem);
        purchaseRequestRepo.save(findPurchaseRequest);
        return getPurchaseRequestResponseOrThrow(findPurchaseRequest.getId());
    }

    // 구매요청 삭제
    @Override
    public void deletePurchaseRequest(Long id) throws NotFoundException {
        PurchaseRequest purchaseRequest = getPurchaseRequestOrThrow(id);
        purchaseRequest.delete();
        purchaseRequestRepo.save(purchaseRequest);
    }

    // 구매요청 단일 조회 및 예외
    @Override
    public PurchaseRequest getPurchaseRequestOrThrow(Long id) throws NotFoundException {
        return purchaseRequestRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("purchaseRequest does not exist. input purchaseRequest id: " + id));
    }
}
