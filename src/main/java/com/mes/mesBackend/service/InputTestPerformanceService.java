package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;

import java.time.LocalDate;
import java.util.List;

// 14-3. 검사실적 조회
public interface InputTestPerformanceService {
    // 검사실적조회
    // 검색조건: 검사기간 fromDate~toDate, 품목 id, 거래처 id, 입고번호(구매입고 id)
    List<InputTestPerformanceResponse> getInputTestPerformances(
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndName,
            Long clientId,
            Long purchaseInputNo
    );
}
