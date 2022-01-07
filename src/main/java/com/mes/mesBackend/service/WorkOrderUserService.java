package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.WorkOrderUserResponse;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 8-2. 작업자 투입 수정
public interface WorkOrderUserService {
    // 작업자 투입 리스트 검색 조회, 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    List<WorkOrderUserResponse> getWorkOrderUsers(
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    );
    // 작업자 투입 수정  작업자, 시작일시, 종료일시 수정가능
    WorkOrderUserResponse updateWorkOrderUser(Long workOrderDetailId, Long userId, LocalDateTime startDate, LocalDateTime endDate) throws NotFoundException, BadRequestException;
    // 작업자 투입 단일 조회
    WorkOrderUserResponse getWorkOrderUserResponseOrThrow(Long workOrderId) throws NotFoundException;
}
