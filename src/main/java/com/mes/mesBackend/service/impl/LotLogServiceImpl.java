package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.LotLogResponse;
import com.mes.mesBackend.repository.LotLogRepository;
import com.mes.mesBackend.service.LotLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Lot log 조회
@Service
@RequiredArgsConstructor
public class LotLogServiceImpl implements LotLogService {
    private final LotLogRepository lotLogRepo;

    // 검색조건: 작업공정 id, 작업지시 id, lotMaster id
    @Override
    public List<LotLogResponse> getLotLogs(Long workProcessId, Long workOrderId, Long lotMasterId) {
        return lotLogRepo.findLotLogResponsesByCondition(workProcessId, workOrderId, lotMasterId);
    }
}
