package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ProduceOrderRequest;
import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.dto.response.ProduceOrderResponse;
import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.entity.ContractItem;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.NumberAutomatic;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.service.ContractService;
import com.mes.mesBackend.service.ProduceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 6-1. 제조오더 등록
@Service
@RequiredArgsConstructor
public class ProduceOrderServiceImpl implements ProduceOrderService {

    private final NumberAutomatic numberAutomatic;
    private final ContractService contractService;
    private final ModelMapper mapper;
    private final ProduceOrderRepository produceOrderRepo;

    // 제조 오더 생성
    @Override
    public ProduceOrderResponse createProduceOrder(ProduceOrderRequest produceOrderRequest) throws NotFoundException {
        Contract contract = contractService.getContractOrThrow(produceOrderRequest.getContract());
        ContractItem contractItem =
                contractService.getContractItemOrThrow(produceOrderRequest.getContract(), produceOrderRequest.getContractItem());
        ProduceOrder produceOrder = mapper.toEntity(produceOrderRequest, ProduceOrder.class);
        produceOrder.add(contract, contractItem);
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
            InstructionStatus instructionStatus,
            String produceOrderNo,
            String contractNo,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        List<ProduceOrder> produceOrders =
                produceOrderRepo.findAllByCondition(itemGroupId, itemNoAndName, instructionStatus, produceOrderNo, contractNo, fromDate, toDate);
        return mapper.toListResponses(produceOrders, ProduceOrderResponse.class);
    }

    // 제조 오더 수정
    @Override
    public ProduceOrderResponse updateProduceOrder(Long produceOrderId, ProduceOrderRequest newProduceOrderRequest) throws NotFoundException {
        ProduceOrder findProduceOrder = getProduceOrderOrThrow(produceOrderId);
        Contract newContract = contractService.getContractOrThrow(newProduceOrderRequest.getContract());
        ContractItem newContractItem =
                contractService.getContractItemOrThrow(newProduceOrderRequest.getContract(), newProduceOrderRequest.getContractItem());
        ProduceOrder newProduceOrder = mapper.toEntity(newProduceOrderRequest, ProduceOrder.class);
        findProduceOrder.update(newProduceOrder, newContract, newContractItem);
        produceOrderRepo.save(findProduceOrder);
        return mapper.toResponse(findProduceOrder, ProduceOrderResponse.class);
    }

    // 제조 오더 삭제
    @Override
    public void deleteProduceOrder(Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = getProduceOrderOrThrow(produceOrderId);
        produceOrder.delete();
        produceOrderRepo.save(produceOrder);
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
}
