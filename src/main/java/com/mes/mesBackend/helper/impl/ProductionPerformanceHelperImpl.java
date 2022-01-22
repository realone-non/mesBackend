package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.ProductionPerformanceHelper;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.ProductionPerformanceRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.repository.WorkProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProductionPerformanceHelperImpl implements ProductionPerformanceHelper {
    private final ProductionPerformanceRepository productionPerformanceRepo;
    private final WorkProcessRepository workProcessRepo;
    private final LotMasterRepository lotMasterRepo;
    private final WorkOrderDetailRepository workOrderDetailRepo;

    // 해당하는 공정의 맞게 ProductionPerformance 수정
    @Override
    public void updateProductionPerformance(Long workOrderDetailId, Long lotMasterId, WorkProcessDivision workProcessDivision) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderDetailId);
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        ProductionPerformance productionPerformance = productionPerformanceRepo.findByWorkOrderDetailAndLotMasterAndDeleteYnFalse(workOrderDetail, lotMaster)
                .orElseThrow(() -> new NotFoundException("[ProductionPerformanceHelper] productionPerformance does not exist."));

        LocalDateTime now = LocalDateTime.now();
        switch (workProcessDivision) {
            case MATERIAL_INPUT: productionPerformance.setMaterialInput(now);
                break;
            case MATERIAL_MIXING: productionPerformance.setMaterialMixing(now);
                break;
            case FILLING: productionPerformance.setFilling(now);
                break;
            case CAP_ASSEMBLY: productionPerformance.setCapAssembly(now);
                break;
            case LABELING: productionPerformance.setLabeling(now);
                break;
            case PACKAGING: productionPerformance.setPackaging(now);
                break;
            case SHIPMENT: productionPerformance.setShipment(now);
                break;
        }
        productionPerformanceRepo.save(productionPerformance);
    }

    private LotMaster getLotMasterOrThrow(Long id) throws NotFoundException {
        return lotMasterRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[ProductionPerformanceHelper] lotMaster does not exist. input id:" + id));
    }

    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[ProductionPerformanceHelper] workOrderDetail does not exist. input id: " + id));
    }
//
//    private WorkProcess getWorkProcessOrThrow(Long id) throws NotFoundException {
//        return workProcessRepo.findByIdAndDeleteYnFalse(id)
//                .orElseThrow(() -> new NotFoundException("[ProductionPerformanceHelper] workProcess does not exist. input id: " + id));
//    }
//
//    private Long getWorkProcessByDivisionOrThrow(WorkProcessDivision workProcessDivision) throws NotFoundException {
//        return workProcessRepo.findIdByWorkProcessDivisionAndDeleteYnFalse(SHIPMENT)
//                .orElseThrow(() -> new NotFoundException("[ProductionPerformanceHelper] 해당하는 workProcessDivision 에 대한 workProcess 가 없음."));
//    }
}
