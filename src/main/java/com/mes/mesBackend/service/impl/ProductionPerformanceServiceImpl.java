package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.ProductionPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.*;

// 8-6. 생산실적 관리
@Service
@RequiredArgsConstructor
public class ProductionPerformanceServiceImpl implements ProductionPerformanceService {
    private final ProduceOrderRepository produceOrderRepository;
    private final WorkOrderDetailRepository workOrderDetailRepository;

    // 생산실적 리스트 조회, 검색조건: 조회기간 fromDate~toDate, 품목그룹 id, 품명|품번
    @Override
    public List<ProductionPerformanceResponse> getProductionPerformances(
            LocalDate fromDate,
            LocalDate toDate,
            Long itemGroupId,
            String itemNoOrItemName
    ) {
        List<ProductionPerformanceResponse> responses = produceOrderRepository.findProductionPerformanceResponseByCondition(fromDate, toDate, itemGroupId, itemNoOrItemName);

        for (ProductionPerformanceResponse r : responses) {
            r.setMaterialMixing(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), MATERIAL_MIXING));
            r.setFilling(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), FILLING));
            r.setCapAssembly(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), CAP_ASSEMBLY));
            r.setLabeling(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), LABELING));
            r.setPackaging(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), PACKAGING));
            Integer packagingProductAmount = workOrderDetailRepository.findPackagingProductAmountByProduceOrderId(r.getId()).orElse(0);
            r.setProductionAmount(packagingProductAmount);
        }
        return responses;
//        return productionPerformanceRepository.findProductionPerformanceResponsesByCondition(fromDate, toDate, itemGroupId, itemNoOrItemName);
    }

}
