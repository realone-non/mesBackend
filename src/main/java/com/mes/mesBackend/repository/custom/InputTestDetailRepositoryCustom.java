package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.InputTestDetailResponse;
import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;
import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;

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
            boolean inputTestDivision
    );
    // 검사상세정보 단일조회
    Optional<InputTestDetailResponse> findDetailByInputTestRequestIdAndInputTestDetailIdAndDeleteYnFalse(Long inputTestRequestId, Long inputTestDetailId, boolean inputTestDivision);
    // 검사상세정보 전체 조회
    List<InputTestDetailResponse> findDetailsByInputTestRequestIdAndDeleteYnFalse(Long inputTestRequestId);
    // 검사요청정보에 해당하는 검사정보의 모든 검사수량
    List<Integer> findTestAmountByInputTestRequestId(Long inputTestRequestId);
    // 검사요청정보에 해당하는 검사정보의 총 부적합수량
    List<Integer> findBadItemAmountByInputTestRequestId(Long inputTestRequestId);
    // 검사요청정보에 해당하는 검사정보의 총 양품수량
    List<Integer> findStockAmountByInputTestRequestId(Long inputTestRequestId);

    // ================================================= 14-3. 검사실적조회 =================================================
    // 검사실적조회
    // 검색조건: 검사기간 fromDate~toDate, 품명|품목, 거래처 id, 입고번호(구매입고 id)
    List<InputTestPerformanceResponse> findInputTestPerformanceResponseByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndName,
            Long clientId,
            Long purchaseInputNo
    );
}
