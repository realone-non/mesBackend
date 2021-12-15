package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ProductionPlanResponse;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.ProductionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionPlanServiceImpl implements ProductionPlanService {

    private final WorkOrderDetailRepository workOrderDetailRepo;

    // 생산계획 수립 전체 조회, 검색조건: 작업라인, 작업예정일
    @Override
    public List<ProductionPlanResponse> getProductionPlans(Long workLineId, LocalDate fromDate, LocalDate toDate) {
        return workOrderDetailRepo.findAllProductionPlanByCondition(workLineId, fromDate, toDate);
    }

    // 생산계획 수립 단일조회
    @Override
    public ProductionPlanResponse getProductionPlan(Long workOrderId) throws NotFoundException {
        return workOrderDetailRepo.findProductionPlanByIdAndDeleteYnFalse(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + workOrderId));
    }

    // 생산계획 수립 등록(작업순번만)
    @Override
    public ProductionPlanResponse createProductionPlanOrder(Long workOrderId, int orders) throws NotFoundException {
        WorkOrderDetail workOrderDetail = workOrderDetailRepo.findByIdAndDeleteYnFalse(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + workOrderId));

        // 0으로 입력받으면 999로 저장
        int order = orders != 0 ? orders : 999;

        workOrderDetail.setOrders(order);
        WorkOrderDetail saveProductionPlan = workOrderDetailRepo.save(workOrderDetail);

        return getProductionPlan(saveProductionPlan.getId());
    }
}
