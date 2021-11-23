package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EstimateItemRequest;
import com.mes.mesBackend.dto.request.EstimateRequest;
import com.mes.mesBackend.dto.response.EstimateItemResponse;
import com.mes.mesBackend.dto.response.EstimateResponse;
import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EstimateService {
    // ===================================== 견적 정보 ======================================
    // 견적 생성
    EstimateResponse createEstimate(EstimateRequest estimateRequest) throws NotFoundException;
    // 견적 단일 조회
    EstimateResponse getEstimate(Long estimateId) throws NotFoundException;
    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자
    Page<EstimateResponse> getEstimates(String clientName, LocalDate fromDate, LocalDate toDate, Long currencyId, String chargeName, Pageable pageable);
    // 견적 수정
    EstimateResponse updateEstimate(Long estimateId, EstimateRequest estimateRequest) throws NotFoundException;
    // 견적 삭제
    void deleteEstimate(Long estimateId) throws NotFoundException;
    // 견적 단일 조회 및 예외
    Estimate getEstimateOrThrow(Long estimateId) throws NotFoundException;

    // ===================================== 견적 품목 정보 ======================================
    // 견적 품목 생성
    EstimateItemResponse createEstimateItem(Long estimateId, EstimateItemRequest estimateItemRequest) throws NotFoundException;
    // 견적 품목 단일 조회
    EstimateItemResponse getEstimateItem(Long estimateId, Long estimateItemId);
    // 견적 품목 페이징 조회
    List<EstimateItemResponse> getEstimateItems(Long estimateId);
    // 견적 품목 수정
    EstimateItemResponse updateEstimateItem(Long estimateId, Long estimateItemId, EstimateItemRequest estimateItemRequest);
    // 견적 품목 삭제
    void deleteEstimateItem(Long estimateId, Long estimateItemId);
}
