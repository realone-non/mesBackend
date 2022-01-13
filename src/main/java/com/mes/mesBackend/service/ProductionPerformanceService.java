package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;

import java.time.LocalDate;
import java.util.List;

// 8-6. 생산실적 관리
public interface ProductionPerformanceService {
    // 생산실적 리스트 조회, 검색조건: 조회기간 fromDate~toDate, 품목그룹 id, 품명|품번
    List<ProductionPerformanceResponse> getProductionPerformances(LocalDate fromDate, LocalDate toDate, Long itemGroupId, String itemNoOrItemName);

}
