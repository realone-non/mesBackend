package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.PopWorkOrderDetailResponse;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// pop
public interface PopService {
    // 작업지시 정보 리스트 api, 조건: 작업자, 작업공정
    List<PopWorkOrderResponse> getPopWorkOrders(Long workProcessId, Long userId);

    // 작업지시 상세 정보
    // 위에 해당 작업지시로 bomItemDetail 항목들 가져오기(품번, 품명, 계정, bom 수량, 예약수량)
    List<PopWorkOrderDetailResponse> getPopWorkOrderDetails(Long lotMasterId, Long workOrderId) throws NotFoundException;
}
