package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.PurchaseInputReturnCreateRequest;
import com.mes.mesBackend.dto.request.PurchaseInputReturnUpdateRequest;
import com.mes.mesBackend.dto.response.PurchaseInputReturnResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 9-6. 구매입고 반품 등록
public interface PurchaseInputReturnService {
    // 구매입고반품 생성
    PurchaseInputReturnResponse createPurchaseInputReturn(PurchaseInputReturnCreateRequest purchaseInputReturnRequest) throws NotFoundException, BadRequestException;
    // 구매입고반품 리스트 검색 조회, 검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate
    List<PurchaseInputReturnResponse> getPurchaseInputReturns(Long clientId, String itemNoOrItemName, LocalDate fromDate, LocalDate toDate);
    // 구매입고반품 단일조회
    PurchaseInputReturnResponse getPurchaseInputReturnResponse(Long purchaseInputReturnId) throws NotFoundException;
    // 구매입고반품 수정
    PurchaseInputReturnResponse updatePurchaseInputReturn(Long purchaseInputReturnId, PurchaseInputReturnUpdateRequest purchaseInputReturnUpdateRequest) throws NotFoundException, BadRequestException;
    // 구매입고반품 삭제
    void deletePurchaseInputReturn(Long purchaseInputReturnId) throws NotFoundException;

}
