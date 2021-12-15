package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WorkOrderRequest;
import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.dto.response.WorkOrderResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.NumberAutomatic;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final WorkProcessService workProcessService;
    private final WorkLineService workLineService;
    private final UserService userService;
    private final UnitService unitService;
    private final ProduceOrderService produceOrderService;
    private final ModelMapper mapper;
    private final NumberAutomatic numberAutomatic;
    private final TestProcessService testProcessService;


    // 제조오더 정보 리스트 조회
    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 착수예정일 fromDate~endDate, 지시상태
    @Override
    public List<WorkOrderProduceOrderResponse> getProduceOrders(
            Long itemGroupId,
            String itemNoAndName,
            String contractNo,
            String produceOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            InstructionStatus instructionStatus
    ) {
        return workOrderDetailRepo.findAllByCondition(itemGroupId, itemNoAndName, contractNo, produceOrderNo, fromDate, toDate, instructionStatus);
    }

    // 작업지시 생성
    @Override
    public WorkOrderResponse createWorkOrder(Long produceOrderId, WorkOrderRequest workOrderRequest) throws NotFoundException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(workOrderRequest.getWorkProcess());
        WorkLine workLine = workLineService.getWorkLineOrThrow(workOrderRequest.getWorkLine());
        User user = workOrderRequest.getUser() != null ? userService.getUserOrThrow(workOrderRequest.getUser()) : null;
        Unit unit = unitService.getUnitOrThrow(workOrderRequest.getUnit());
        TestProcess testProcess = testProcessService.getTestProcessOrThrow(workOrderRequest.getTestProcess());

        // orderAmount 가 0 이면 수주품목의 수량으로 저장.
        int orderAmount = workOrderRequest.getOrderAmount() != 0 ? workOrderRequest.getOrderAmount() : produceOrder.getContractItem().getAmount();

        WorkOrderDetail workOrderDetail = mapper.toEntity(workOrderRequest, WorkOrderDetail.class);
        String workOrderNo = numberAutomatic.createDateTimeNo();

        workOrderDetail.add(workProcess, workLine, user, unit, testProcess, produceOrder);
        workOrderDetail.setOrderNo(workOrderNo);
        workOrderDetail.setOrderAmount(orderAmount);

        workOrderDetailRepo.save(workOrderDetail);
        return mapper.toResponse(workOrderDetail, WorkOrderResponse.class);
    }

    // 작업지시 단일조회
    @Override
    public WorkOrderResponse getWorkOrder(Long produceOrderId, Long workOrderId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId, produceOrderId);
        return mapper.toResponse(workOrderDetail, WorkOrderResponse.class);
    }

    // 작업지시 리스트 조회
    @Override
    public List<WorkOrderResponse> getWorkOrders(Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        List<WorkOrderDetail> workOrderDetails = workOrderDetailRepo.findAllByProduceOrderAndDeleteYnFalse(produceOrder);
        return mapper.toListResponses(workOrderDetails, WorkOrderResponse.class);
    }

    // 작업지시 수정
    @Override
    public WorkOrderResponse updateWorkOrder(Long produceOrderId, Long workOrderId, WorkOrderRequest newWorkOrderRequest) throws NotFoundException {
        WorkOrderDetail findWorkOrderDetail = getWorkOrderDetailOrThrow(workOrderId, produceOrderId);
        WorkProcess newWorkProcess = workProcessService.getWorkProcessOrThrow(newWorkOrderRequest.getWorkProcess());
        WorkLine newWorkLine = workLineService.getWorkLineOrThrow(newWorkOrderRequest.getWorkLine());
        User newUser = newWorkOrderRequest.getUser() != null ? userService.getUserOrThrow(newWorkOrderRequest.getUser()) : null;
        Unit newUnit = unitService.getUnitOrThrow(newWorkOrderRequest.getUnit());
        TestProcess testProcess = testProcessService.getTestProcessOrThrow(newWorkOrderRequest.getTestProcess());
        WorkOrderDetail newWorkOrderDetail = mapper.toEntity(newWorkOrderRequest, WorkOrderDetail.class);
        findWorkOrderDetail.update(newWorkOrderDetail, newWorkProcess, newWorkLine, newUser, testProcess, newUnit);
        workOrderDetailRepo.save(findWorkOrderDetail);
        return mapper.toResponse(findWorkOrderDetail, WorkOrderResponse.class);
    }

    // 작업지시 삭제
    @Override
    public void deleteWorkOrder(Long produceOrderId, Long workOrderId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId, produceOrderId);
        workOrderDetail.delete();
        workOrderDetailRepo.save(workOrderDetail);
    }

    // 작업지시 단일 조회 및 예외
    @Override
    public WorkOrderDetail getWorkOrderDetailOrThrow(Long id, Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        return workOrderDetailRepo.findByIdAndProduceOrderAndDeleteYnFalse(id, produceOrder)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + id));
    }
}
