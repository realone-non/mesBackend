package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PurchaseOrderDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseOrderResponse;
import com.mes.mesBackend.entity.enumeration.OrderState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepositoryCustom {
    // 구매발주 단일조회
    Optional<PurchaseOrderResponse> findPurchaseOrderByIdAndDeleteYn(Long id);

    // 구매발주 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간
    List<PurchaseOrderResponse> findAllByCondition(
            Long currencyId,
            Long userId,
            Long clientId,
            Long wareHouseId,
            LocalDate fromDate,
            LocalDate toDate
    );

    // 구매발주 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간, 완료포함(check)
    List<OrderState> findOrderStateByPurchaseOrderId(Long purchaseOrderId);

    // 발주등록에 해당하는 상세정보의 최근 납기일자를 가져옴
    LocalDate findPeriodDateByPurchaseOrderId(Long purchaseOrderId);

    // 발주등록상세 단일 조회
    Optional<PurchaseOrderDetailResponse> findPurchaseRequestByIdAndPurchaseOrderIdAndDeleteYnFalse(Long purchaseOrderId, Long purchaseRequestId);

    // 발주등록상세 리스트 전체조회
    List<PurchaseOrderDetailResponse> findAllByPurchaseOrderIdAndDeleteYnFalse(Long purchaseOrderId);
}
