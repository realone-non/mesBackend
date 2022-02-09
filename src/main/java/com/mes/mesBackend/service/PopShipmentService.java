package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.PopShipmentResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface PopShipmentService {
    // 출하 정보 목록 조회
    List<PopShipmentResponse> getPopShipments(LocalDate fromDate, LocalDate toDate, String clientName, Boolean completionYn);
    // 출하상태 COMPLETION 으로 변경
    void updateShipmentStateCompletion(Long shipmentId) throws NotFoundException;
}
