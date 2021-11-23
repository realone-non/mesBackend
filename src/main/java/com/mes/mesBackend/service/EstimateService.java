package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EstimateRequest;
import com.mes.mesBackend.dto.response.EstimateResponse;
import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EstimateService {
    // 견적 생성
    EstimateResponse createEstimate(EstimateRequest estimateRequest) throws NotFoundException;
    // 견적 단일 조회
    EstimateResponse getEstimate(Long id) throws NotFoundException;
    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자
    Page<EstimateResponse> getEstimates(String clientName, LocalDateTime startDate, LocalDateTime endDate, Long currencyId, String chargeName, Pageable pageable);
    // 견적 수정
    EstimateResponse updateEstimate(Long id, EstimateRequest estimateRequest) throws NotFoundException;
    // 견적 삭제
    void deleteEstimate(Long id) throws NotFoundException;
    // 견적 단일 조회 및 예외
    Estimate getEstimateOrThrow(Long id) throws NotFoundException;
}
