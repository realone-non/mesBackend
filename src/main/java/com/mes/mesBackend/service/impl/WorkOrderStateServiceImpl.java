package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.WorkOrderStateDetailResponse;
import com.mes.mesBackend.dto.response.WorkOrderStateResponse;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.WorkOrderState;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.repository.WorkOrderStateRepository;
import com.mes.mesBackend.service.WorkOrderStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 8-1. 작지상태 확인
@Service
@RequiredArgsConstructor
public class WorkOrderStateServiceImpl implements WorkOrderStateService {
    private final WorkOrderStateRepository workOrderStateRepo;
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ModelMapper mapper;

    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    @Override
    public List<WorkOrderStateResponse> getWorkOrderStates(
            Long workProcessId,
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    ) {
        return workOrderStateRepo.findWorkOrderStateByCondition(workProcessId, workLineId, produceOrderNo, itemAccountId, orderState, fromDate, toDate, contractNo);
    }

    // 작업지시 단일 조회
    @Override
    public WorkOrderStateResponse getWorkOrderState(Long workOrderId) throws NotFoundException {
        return workOrderStateRepo.findWorkOrderStateByIdAndWorkOrderDetail(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderState does not exist. input workOrderId: " + workOrderId));
    }

    // 작업지시 상태 이력 정보 리스트 조회
    @Override
    public List<WorkOrderStateDetailResponse> getWorkOrderStateDetails(Long workOrderId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = workOrderDetailRepo.findByIdAndDeleteYnFalse(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrder does not exist. input workOrderId: " + workOrderId));

        List<WorkOrderState> workOrderStates = workOrderStateRepo.findAllByWorkOrderDetail(workOrderDetail);
        return mapper.toListResponses(workOrderStates, WorkOrderStateDetailResponse.class);

    }
    // 작업지시 상태 이력 변경
    @Override
    public void createWorkOrderStateDetail(WorkOrderDetail workOrderDetail) {
        if (workOrderDetail.getOrderState() != null) {
            WorkOrderState workOrderState = new WorkOrderState();
            workOrderState.setWorkOrderDetail(workOrderDetail);
            workOrderState.setWorkOrderDateTime(LocalDateTime.now());
            workOrderState.setOrderState(workOrderDetail.getOrderState());
            workOrderStateRepo.save(workOrderState);
        }
    }
}
