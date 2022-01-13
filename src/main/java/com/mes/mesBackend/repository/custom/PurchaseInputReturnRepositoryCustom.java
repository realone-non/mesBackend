package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PurchaseInputReturnResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 9-6. 구매입고 반품 등록
public interface PurchaseInputReturnRepositoryCustom {
    // 구매입고반품 단일조회
    Optional<PurchaseInputReturnResponse> findPurchaseInputReturnResponseById(Long purchaseInputReturnId);
    // 구매입고반품 리스트 검색 조회, 검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate
    List<PurchaseInputReturnResponse> findPurchaseInputReturnResponsesByCondition(Long clientId, String itemNoOrItemName, LocalDate fromDate, LocalDate toDate);
}
