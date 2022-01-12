package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.WorkOrderStateDetailResponse;
import com.mes.mesBackend.dto.response.WorkOrderStateResponse;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 8-1. 작지상태 확인
public interface WorkOrderStateService {
    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    List<WorkOrderStateResponse> getWorkOrderStates(
            Long workProcessId,
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    );
    // 작업지시 단일 조회
    WorkOrderStateResponse getWorkOrderState(Long workOrderId) throws NotFoundException;
    // 작업지시 상태 이력 정보 리스트 조회
    WorkOrderStateDetailResponse getWorkOrderStateDetail(Long workOrderId);
    // 작업지시 상태 이력 변경
//    void createWorkOrderStateDetail(WorkOrderDetail workOrderDetail);
}
