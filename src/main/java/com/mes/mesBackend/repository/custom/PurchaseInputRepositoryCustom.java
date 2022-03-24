package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.LabelPrintResponse;
import com.mes.mesBackend.dto.response.PurchaseInputDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseInputResponse;
import com.mes.mesBackend.dto.response.PurchaseStatusCheckResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// 9-5. 구매입고 등록
public interface PurchaseInputRepositoryCustom {
    // 구매입고 리스트 조회, 검색조건: 입고기간 fromDate~toDate, 입고창고, 거래처, 품명|품번
    List<PurchaseInputResponse> findPurchaseRequestsByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            Long wareHouseId,
            Long clientId,
            String itemNoOrItemName
    );

    // 구매입고 LOT 정보 단일 조회
    Optional<PurchaseInputDetailResponse> findPurchaseInputDetailByIdAndPurchaseInputId(Long purchaseRequestId, Long purchaseInputId);

    // 구매입고에 들어갈 item 조회 lot 번호 생성할때 쓰임
    Long findItemIdByPurchaseInputId(Long purchaseInputId);

    // 구매요청에 해당하는 구매발주상세의 입고수량을 전부 조회
    List<Integer> findInputAmountByPurchaseRequestId(Long purchaseRequestId);

    // 구매입고 정보에 해당하는 구매입고 LOT 전체 조회
    List<PurchaseInputDetailResponse> findPurchaseInputDetailByPurchaseRequestId(Long purchaseRequestId);

    // 구매요청에 해당하는 구매발주상세의 제일 최근 등록된 날짜 조회
    Optional<LocalDateTime> findCreatedDateByPurchaseRequestId(Long purchaseRequestId);

    //  9-4. 구매현황조회
    // 구매현황 리스트 조회 / 검색조건: 거래처 id, 품명|품목, 입고기간 fromDate~toDate
    List<PurchaseStatusCheckResponse> findPurchaseStatusCheckByCondition(
            Long clientId,
            String itemNoAndItemName,
            LocalDate fromDate,
            LocalDate toDate
    );
    // 금일기준 입고된 자재목록
    List<LabelPrintResponse> findByTodayAndPurchaseInput(LocalDate fromDate, LocalDate toDate);
}
