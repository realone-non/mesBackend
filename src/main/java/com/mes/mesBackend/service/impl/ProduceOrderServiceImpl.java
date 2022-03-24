package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ProduceOrderRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.dto.response.ProduceOrderResponse;
import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.entity.ContractItem;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.NumberAutomatic;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ContractRepository;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.PurchaseRequestRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.ContractService;
import com.mes.mesBackend.service.ProduceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;

// 6-1. 제조오더 등록
@Service
@RequiredArgsConstructor
public class ProduceOrderServiceImpl implements ProduceOrderService {

    private final NumberAutomatic numberAutomatic;
    private final ContractService contractService;
    private final ModelMapper mapper;
    private final ProduceOrderRepository produceOrderRepo;
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final ContractRepository contractRepository;

    // 제조 오더 생성
    @Override
    public ProduceOrderResponse createProduceOrder(ProduceOrderRequest produceOrderRequest) throws NotFoundException {
        Contract contract = contractService.getContractOrThrow(produceOrderRequest.getContract());
        ContractItem contractItem =
                contractService.getContractItemOrThrow(produceOrderRequest.getContract(), produceOrderRequest.getContractItem());
        ProduceOrder produceOrder = mapper.toEntity(produceOrderRequest, ProduceOrder.class);
        produceOrder.created(contract, contractItem);
        String produceOrderNo = numberAutomatic.createDateTimeNo();

        produceOrder.setProduceOrderNo(produceOrderNo);
        produceOrderRepo.save(produceOrder);
        return mapper.toResponse(produceOrder, ProduceOrderResponse.class);
    }

    // 제조 오더 단일 조회
    @Override
    public ProduceOrderResponse getProduceOrder(Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = getProduceOrderOrThrow(produceOrderId);
        return mapper.toResponse(produceOrder, ProduceOrderResponse.class);
    }

    // 제조 오더 리스트 조회, 검색조건 : 품목그룹 id, 품명|품번, 지시상태, 제조오더번호, 수주번호, 착수예정일 fromDate~toDate, 자재납기일자(보류)
    @Override
    public List<ProduceOrderResponse> getProduceOrders(
            Long itemGroupId,
            String itemNoAndName,
            OrderState orderState,
            String produceOrderNo,
            String contractNo,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        List<ProduceOrder> produceOrders =
                produceOrderRepo.findAllByCondition(itemGroupId, itemNoAndName, orderState, produceOrderNo, contractNo, fromDate, toDate);
        return mapper.toListResponses(produceOrders, ProduceOrderResponse.class);
    }

    // 제조 오더 수정
    @Override
    public ProduceOrderResponse updateProduceOrder(Long produceOrderId, ProduceOrderRequest newProduceOrderRequest) throws NotFoundException, BadRequestException {
        ProduceOrder findProduceOrder = getProduceOrderOrThrow(produceOrderId);
        // 제조오더에 해당하는 작업지시가 한개라도 진행중이면 수주품목 필드 수정 불가
        throwIfWorkOrderStateNotItemInfoUpdate(findProduceOrder.getId(), findProduceOrder.getContractItem().getId(), newProduceOrderRequest.getContractItem());
        // 제조오더에 해당하는 구매요청이 한개라도 진행중이거나 완료일 경우엔 수주품목 필드 수정 불가
        throwIfPurchsaeRequestOrderStateNotItemInfoUpdate(findProduceOrder.getId(), findProduceOrder.getContractItem().getId(), newProduceOrderRequest.getContractItem());

        Contract newContract = contractService.getContractOrThrow(newProduceOrderRequest.getContract());
        ContractItem newContractItem =
                contractService.getContractItemOrThrow(newProduceOrderRequest.getContract(), newProduceOrderRequest.getContractItem());
        ProduceOrder newProduceOrder = mapper.toEntity(newProduceOrderRequest, ProduceOrder.class);
        findProduceOrder.update(newProduceOrder, newContract, newContractItem);
        produceOrderRepo.save(findProduceOrder);
        return mapper.toResponse(findProduceOrder, ProduceOrderResponse.class);
    }

    // 제조오더에 해당하는 구매요청이 한개라도 진행중이거나 완료일 경우엔 수주품목 필드 수정 불가
    private void throwIfPurchsaeRequestOrderStateNotItemInfoUpdate(Long purchaseOrderId, Long findContractItemId, Long newContractItemId) throws BadRequestException {
        boolean b = purchaseRequestRepository.findOrderStateByPurchaseOrder(purchaseOrderId).stream().noneMatch(m -> m.equals(SCHEDULE));
        if (b && !findContractItemId.equals(newContractItemId)) {
            throw new BadRequestException("제조오더에 해당하는 구매입고가 진행중일 경우엔 품목정보를 수정할수없습니다.");
        }
    }

    // 제조오더에 해당하는 작업지시가 한개라도 진행중이면 수주품목 필드 수정 불가
    private void throwIfWorkOrderStateNotItemInfoUpdate(Long produceOrderId, Long findContractItemId, Long newContractItemId) throws BadRequestException {
        boolean b = workOrderDetailRepository.findOrderStatesByProduceOrderId(produceOrderId).stream().noneMatch(m -> m.equals(SCHEDULE));
        if (b && !findContractItemId.equals(newContractItemId)) {
            throw new BadRequestException("제조오더에 해당하는 작업지시 중 하나라도 진행중이나 완료일 경우에는 품목정보를 수정할수없습니다.");
        }
    }

    // 제조 오더 삭제
    @Override
    public void deleteProduceOrder(Long produceOrderId) throws NotFoundException, BadRequestException {
        ProduceOrder produceOrder = getProduceOrderOrThrow(produceOrderId);
        // 제조오더에 해당하는 작업지시가 존재할경우 삭제 불가능
        throwIfWorkOrderStateNotDelete(produceOrder);
        // 제조오더에 해당하는 구매요청이 존재할경우 삭제 불가능
        throwIfPurchaseRequestStateNotDelete(produceOrder);
        produceOrder.delete();
        produceOrderRepo.save(produceOrder);
    }

    // 제조오더에 해당하는 작업지시가 존재할경우 삭제 불가능
    private void throwIfWorkOrderStateNotDelete(ProduceOrder produceOrder) throws BadRequestException {
        boolean b = workOrderDetailRepository.existsByProduceOrderAndDeleteYnFalse(produceOrder);
        if (b) throw new BadRequestException("해당 제조오더로 등록 된 작업지시 정보가 존재할경우 삭제가 불가능합니다. 작업지시 정보 삭제 후 다시 시도해주세요.");
    }

    // 제조오더에 해당하는 구매요청이 존재할경우 삭제 불가능
    private void throwIfPurchaseRequestStateNotDelete(ProduceOrder produceOrder) throws BadRequestException {
        boolean b = purchaseRequestRepository.existsByProduceOrderAndDeleteYnFalse(produceOrder);
        if (b) throw new BadRequestException("해당 제조오더로 등록 된 구매요청이 존재할경우 삭제가 불가능합니다. 구매요청 정보를 삭제 후 다시 시도해주세요.");
    }

    // 제조 오더 단일 조회 및 예외
    @Override
    public ProduceOrder getProduceOrderOrThrow(Long id) throws NotFoundException {
        return produceOrderRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("produceOrder does not exist. id : " + id));
    }

    // 제조 오더 품목 디테일 리스트 조회
    @Override
    public List<ProduceOrderDetailResponse> getProduceOrderDetails(Long produceOrderId) throws NotFoundException {
        ProduceOrderResponse produceOrder = getProduceOrder(produceOrderId);
        ContractItem contractItem =
                contractService.getContractItemOrThrow(produceOrder.getContract().getId(), produceOrder.getContractItem().getId());
        Long itemId = contractItem.getItem().getId();

        List<ProduceOrderDetailResponse> orderDetails = produceOrderRepo.findAllProduceOrderDetail(itemId);
        orderDetails.forEach(orderDetail ->
                orderDetail.setReservationAmount(orderDetail.getBomAmount(), contractItem.getAmount()));

        return orderDetails;
    }

    // 수주 등록된 제조사 list 조회 api
    @Override
    public List<ClientResponse.CodeAndName> getContractClients() {
        return contractRepository.findContractClientResponse();
    }
}
