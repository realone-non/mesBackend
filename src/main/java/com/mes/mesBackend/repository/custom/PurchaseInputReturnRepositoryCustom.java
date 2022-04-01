package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.dto.response.PurchaseInputReturnResponse;
import com.mes.mesBackend.entity.LotMaster;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 9-6. 구매입고 반품 등록
public interface PurchaseInputReturnRepositoryCustom {
    // 구매입고반품 단일조회
    Optional<PurchaseInputReturnResponse> findPurchaseInputReturnResponseById(Long purchaseInputReturnId);
    // 구매입고반품 리스트 검색 조회, 검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate
    List<PurchaseInputReturnResponse> findPurchaseInputReturnResponsesByCondition(Long clientId, String itemNoOrItemName, LocalDate fromDate, LocalDate toDate);
    // LotMasterId, 분류로 구매입고반품 찾기
    PurchaseInputReturnResponse findPurchaseInputReturnByCondition(Long lotMasterId, boolean returnDivistion);
    // 구매입고반품 가능한 lotMatser 조회
    List<LotMasterResponse.stockAmountAndBadItemAmount> findPurchaseInputReturnPossbleLotMasters();
}
