package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.InputTestDetailResponse;
import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;
import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;
import com.mes.mesBackend.dto.response.InputTestScheduleResponse;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.TestType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 14-2. 검사 등록
public interface InputTestDetailRepositoryCustom {
    // 검사요청정보 리스트 조회
    // 검색조건: 창고 id, 품명|품목, 완료여부, 입고번호, 품목그룹 id, LOT 유형 id, 요청기간 from~toDate, 제조사 id
    List<InputTestRequestInfoResponse> findInputTestRequestInfoResponseByCondition(
            Long warehouseId,
            String itemNoAndName,
            Boolean completionYn,
            Long purchaseInputNo,
            Long itemGroupId,
            Long lotTypeId,
            LocalDate fromDate,
            LocalDate toDate,
            Long manufactureId,
            InputTestDivision inputTestDivision,
            TestType testType
    );
    // 검사상세정보 단일조회
    Optional<InputTestDetailResponse> findDetailByInputTestRequestIdAndInputTestDetailIdAndDeleteYnFalse(Long inputTestRequestId, Long inputTestDetailId, InputTestDivision inputTestDivision);
    // 검사상세정보 전체 조회
    List<InputTestDetailResponse> findDetailsByInputTestRequestIdAndDeleteYnFalse(Long inputTestRequestId);
    // 검사요청정보에 해당하는 검사정보의 모든 검사수량
    List<Integer> findTestAmountByInputTestRequestId(Long inputTestRequestId);
    // 검사요청정보에 해당하는 검사정보의 총 부적합수량
    List<Integer> findBadItemAmountByInputTestRequestId(Long inputTestRequestId);
    // 검사요청정보에 해당하는 검사정보의 총 양품수량
    List<Integer> findStockAmountByInputTestRequestId(Long inputTestRequestId);

    // ================================================= 14-3. 검사실적조회 =================================================
    // 14-3. 검사실적조회
    // 15-3. 검사실적 조회
    // 검색조건: 검사기간 fromDate~toDate, 품명|품목, 거래처 id, 입고번호(구매입고 id)
    List<InputTestPerformanceResponse> findInputTestPerformanceResponseByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndName,
            Long clientId,
            Long purchaseInputNo,
            InputTestDivision inputTestDivision,
            TestType testType,
            Long wareHouseId
    );
    // 14-4. 검사대기현황, 15-4. 검사대기현황
    // 검색조건: 검사창고 id, 검사유형, 품명|품번, 거래처, 검사기간 fromDate~toDate
    List<InputTestScheduleResponse> findInputTestScheduleResponsesByCondition(
            Long wareHouseId,
            TestType testType,
            String itemNoAndName,
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            InputTestDivision inputTestDivision
    );
}
