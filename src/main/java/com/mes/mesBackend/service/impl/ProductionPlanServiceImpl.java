package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ProductionPlanResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.ProductionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.PACKAGING;

// 6-3. 생산계획 수립
@Service
@RequiredArgsConstructor
public class ProductionPlanServiceImpl implements ProductionPlanService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ItemRepository itemRepository;

    // 생산계획 수립 전체 조회, 검색조건: 작업라인, 작업예정일
    @Override
    public List<ProductionPlanResponse> getProductionPlans(Long workLineId, LocalDate fromDate, LocalDate toDate) throws NotFoundException {
        List<ProductionPlanResponse> productionPlans = workOrderDetailRepo.findAllProductionPlanByCondition(workLineId, fromDate, toDate);

        for (ProductionPlanResponse response : productionPlans) {
            Item item = response.getWorkProcessDivision().equals(PACKAGING) ? getItemOrNull(response.getItemId())
                    : workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(response.getItemId(), response.getWorkProcessId(), null)
                    .orElse(null);
            response.setCostTime();
            if (item != null) response.setItems(item);
        }
        return productionPlans;
    }

    // 품목 조회 및 없으면 null
    private Item getItemOrNull(Long id) {
        return itemRepository.findByIdAndDeleteYnFalse(id).orElse(null);
    }

    // 생산계획 수립 단일조회
    @Override
    public ProductionPlanResponse getProductionPlan(Long workOrderId) throws NotFoundException {
        ProductionPlanResponse productionPlanResponse = workOrderDetailRepo.findProductionPlanByIdAndDeleteYnFalse(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + workOrderId));
        productionPlanResponse.setCostTime();
        return productionPlanResponse;
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

    // 품목 단일 조회 및 예외
    private Item getItemOrThrow(Long id) throws NotFoundException {
        return itemRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("item does not exist. input id: " + id));
    }
}
