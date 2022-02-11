package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;
import com.mes.mesBackend.dto.response.InputTestScheduleResponse;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;

import java.time.LocalDate;
import java.util.List;

// 14-3. 검사실적 조회
// 15-3. 검사실적 조회
// 16-4. 검사 현황 조회
public interface InputTestPerformanceService {
    // 검사실적조회
    // 검색조건: 검사기간 fromDate~toDate, 품목 id, 거래처 id, 입고번호(구매입고 id)
    List<InputTestPerformanceResponse> getInputTestPerformances(
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndName,
            Long clientId,
            Long purchaseInputNo,
            InputTestDivision inputTestDivision,
            InspectionType inspectionType,
            Long wareHouseId
    );
    // 검사대기 현황 조회
    // 검색조건: 검사창고 id, 검사유형, 품명|품번, 거래처, 검사기간 fromDate~toDate
    List<InputTestScheduleResponse> getInputTestSchedules(
            Long wareHouseId,
            InspectionType inspectionType,
            String itemNoAndName,
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            InputTestDivision inputTestDivision
    );
}
