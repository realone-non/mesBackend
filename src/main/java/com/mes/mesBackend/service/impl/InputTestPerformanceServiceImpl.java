package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;
import com.mes.mesBackend.dto.response.InputTestScheduleResponse;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.repository.InputTestDetailRepository;
import com.mes.mesBackend.service.InputTestPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// 14-3. 검사실적 조회
// 15-3. 검사실적 조회
// 16-4. 검사 현황 조회
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
            InputTestDivision inputTestDivision,
            TestType testType,
            Long wareHouseId
    ) {
        return inputTestDetailRepo.findInputTestPerformanceResponseByCondition(
                fromDate, toDate, itemNoAndName, clientId, purchaseInputNo, inputTestDivision, testType, wareHouseId)
                .stream().map(res -> res.division(inputTestDivision)).collect(Collectors.toList());
    }

    // 검사대기 현황 조회
    // 검색조건: 검사창고 id, 검사유형, 품명|품번, 거래처, 검사기간 fromDate~toDate
    @Override
    public List<InputTestScheduleResponse> getInputTestSchedules(
            Long wareHouseId,
            TestType testType,
            String itemNoAndName,
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            InputTestDivision inputTestDivision
    ) {
        return inputTestDetailRepo.findInputTestScheduleResponsesByCondition(
                wareHouseId,
                testType,
                itemNoAndName,
                clientId,
                fromDate,
                toDate,
                inputTestDivision
        ).stream().map(res -> res.division(inputTestDivision)).collect(Collectors.toList());
    }
}
