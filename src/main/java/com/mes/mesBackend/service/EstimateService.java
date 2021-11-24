package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EstimateItemRequest;
import com.mes.mesBackend.dto.request.EstimatePiRequest;
import com.mes.mesBackend.dto.request.EstimateRequest;
import com.mes.mesBackend.dto.response.EstimateItemResponse;
import com.mes.mesBackend.dto.response.EstimatePiResponse;
import com.mes.mesBackend.dto.response.EstimateResponse;
import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.exception.BadRequestException;
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
    List<EstimateResponse> getEstimates(String clientName, LocalDate fromDate, LocalDate toDate, Long currencyId, String chargeName);
//    Page<EstimateResponse> getEstimates(String clientName, LocalDate fromDate, LocalDate toDate, Long currencyId, String chargeName, Pageable pageable);
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
    EstimateItemResponse getEstimateItem(Long estimateId, Long estimateItemId) throws NotFoundException;
    // 견적 품목 전체 조회
    List<EstimateItemResponse> getEstimateItems(Long estimateId) throws NotFoundException;
    // 견적 품목 수정
    EstimateItemResponse updateEstimateItem(Long estimateId, Long estimateItemId, EstimateItemRequest estimateItemRequest) throws NotFoundException;
    // 견적 품목 삭제
    void deleteEstimateItem(Long estimateId, Long estimateItemId) throws NotFoundException;

    // ===================================== 견적 P/I ======================================
    // 견적 P/I 생성
    EstimatePiResponse createEstimatePi(Long estimateId, EstimatePiRequest estimatePiRequest) throws NotFoundException, BadRequestException;
    // 견적 P/I 조회
    EstimatePiResponse getEstimatePi(Long estimateId) throws NotFoundException;
    // 견적 P/I 수정
    EstimatePiResponse updateEstimatePi(Long estimateId, Long estimatePiId, EstimatePiRequest estimatePiRequest) throws NotFoundException, BadRequestException;
    // 견적 P/I 삭제
    void deleteEstimatePi(Long estimateId, Long estimatePiId) throws NotFoundException, BadRequestException;
}
