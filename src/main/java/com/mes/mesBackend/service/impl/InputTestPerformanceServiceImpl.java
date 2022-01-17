package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;
import com.mes.mesBackend.repository.InputTestDetailRepository;
import com.mes.mesBackend.service.InputTestPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 14-3. 검사실적 조회
// 15-3. 검사실적 조회
@Service
@RequiredArgsConstructor
public class InputTestPerformanceServiceImpl implements InputTestPerformanceService {
    private final InputTestDetailRepository inputTestDetailRepo;

    // 검사실적조회
    // 검색조건: 검사기간 fromDate~toDate, 품명|품목, 거래처 id, 입고번호(구매입고 id)
    @Override
    public List<InputTestPerformanceResponse> getInputTestPerformances(
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndName,
            Long clientId,
            Long purchaseInputNo,
            boolean inputTestDivision
    ) {
        return inputTestDetailRepo.findInputTestPerformanceResponseByCondition(fromDate, toDate, itemNoAndName, clientId, purchaseInputNo, inputTestDivision);
    }
}
