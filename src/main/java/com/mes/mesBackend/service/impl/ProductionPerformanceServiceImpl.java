package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;
import com.mes.mesBackend.repository.ProductionPerformanceRepository;
import com.mes.mesBackend.service.ProductionPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 8-6. 생산실적 관리
@Service
@RequiredArgsConstructor
public class ProductionPerformanceServiceImpl implements ProductionPerformanceService {
    private final ProductionPerformanceRepository productionPerformanceRepository;
    // 생산실적 리스트 조회, 검색조건: 조회기간 fromDate~toDate, 품목그룹 id, 품명|품번
    @Override
    public List<ProductionPerformanceResponse> getProductionPerformances(
            LocalDate fromDate,
            LocalDate toDate,
            Long itemGroupId,
            String itemNoOrItemName
    ) {
        return productionPerformanceRepository.findProductionPerformanceResponsesByCondition(fromDate, toDate, itemGroupId, itemNoOrItemName);
    }
}
