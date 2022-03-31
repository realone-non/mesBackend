package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.PurchaseOrderDetailRequest;
import com.mes.mesBackend.dto.request.PurchaseOrderRequest;
import com.mes.mesBackend.dto.response.PurchaseOrderDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseOrderResponse;
import com.mes.mesBackend.dto.response.PurchaseOrderStatusResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
// 9-2. 구매발주 등록
public interface PurchaseOrderService {
    // 구매발주 생성
    PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) throws NotFoundException;
    // 구매발주 단일 조회
    PurchaseOrderResponse getPurchaseOrderResponseOrThrow(Long purchaseOrderId) throws NotFoundException;
    // 구매발주 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간, 완료포함(check)
    List<PurchaseOrderResponse> getPurchaseOrders(Long currencyId, Long userId, Long clientId, Long wareHouseId, LocalDate fromDate, LocalDate toDate, boolean orderCompletion);
    // 구매발주 수정
    PurchaseOrderResponse updatePurchaseOrder(Long purchaseOrderId, PurchaseOrderRequest purchaseOrderRequest) throws NotFoundException;
    // 구매발주 삭제
    void deletePurchaseOrder(Long purchaseOrderId) throws NotFoundException, BadRequestException;

    // ================================================================ 구매발주상세 ================================================================
    // 구매발주상세 생성
    PurchaseOrderDetailResponse createPurchaseOrderDetail(Long purchaseOrderId, PurchaseOrderDetailRequest purchaseOrderDetailRequest) throws NotFoundException, BadRequestException;
    // 구매발주상세 단일 조회
    PurchaseOrderDetailResponse getPurchaseOrderDetailResponse(Long purchaseOrderId, Long purchaseOrderDetailId) throws NotFoundException, BadRequestException;
    // 구매발주상세 전체 조회
    List<PurchaseOrderDetailResponse> getPurchaseOrderDetails(Long purchaseOrderId) throws NotFoundException;
    // 구매발주상세 수정
    PurchaseOrderDetailResponse updatePurchaseOrderDetail(Long purchaseOrderId, Long purchaseOrderDetailId, String note, boolean inputTestYn) throws NotFoundException, BadRequestException;
    // 구매발주상세 삭제
    void deletePurchaseOrderDetail(Long purchaseOrderId, Long purchaseOrderDetailId) throws NotFoundException, BadRequestException;

    // ================================================================ 9-3. 구매발주현황조회 ================================================================
    // 발주현황 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간 fromDate~toDate
    List<PurchaseOrderStatusResponse> getPurchaseOrderStatuses(Long currencyId, Long userId, Long clientId, Long wareHouseId, LocalDate fromDate, LocalDate toDate);
}
