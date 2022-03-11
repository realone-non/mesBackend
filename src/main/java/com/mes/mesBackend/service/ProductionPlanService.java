package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.ProductionPlanResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 6-3. 생산계획 수립
public interface ProductionPlanService {
    // 생산계획 수립 전체 조회, 검색조건: 작업라인, 착수예정일
    List<ProductionPlanResponse> getProductionPlans(Long workLineId, LocalDate fromDate, LocalDate toDate) throws NotFoundException;
    // 생산계획 수립 단일조회
    ProductionPlanResponse getProductionPlan(Long workOrderId) throws NotFoundException;
    // 생산계획 수립 등록(작업순번만)
    ProductionPlanResponse createProductionPlanOrder(Long workOrderId, int orders) throws NotFoundException;
}
