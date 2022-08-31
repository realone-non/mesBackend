package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 8-6. 생산실적 관리
public interface ProductionPerformanceService {
    // 생산실적 리스트 조회, 검색조건: 조회기간 fromDate~toDate, 작업공정
    List<ProductionPerformanceResponse> getProductionPerformances(LocalDate fromDate, LocalDate toDate, Long inputWorkProcessId, String itemNoOrItemName) throws NotFoundException;
}
