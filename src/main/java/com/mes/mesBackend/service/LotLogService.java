package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.LotLogResponse;

import java.util.List;

// Lot log 조회
public interface LotLogService {
    // 검색조건: 작업공정 id, 작업지시 id, lotMaster id
    List<LotLogResponse> getLotLogs(Long workProcessId, Long workOrderId, Long lotMasterId);
}
