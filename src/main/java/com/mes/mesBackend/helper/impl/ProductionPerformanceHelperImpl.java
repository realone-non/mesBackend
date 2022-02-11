package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.WorkProcess;
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

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;

@Component
@RequiredArgsConstructor
public class ProductionPerformanceHelperImpl implements ProductionPerformanceHelper {
    private final ProductionPerformanceRepository productionPerformanceRepo;
    private final LotMasterRepository lotMasterRepo;
    private final WorkOrderDetailRepository workOrderDetailRepo;

    // 해당하는 공정의 맞게 ProductionPerformance 수정
    // - 제조오더에 해당하는 productionPerformance 가 없고, 작업시지의 상태값이 COMPLETION 이면 생성, 있으면 공정에 해당하는 컬럼에 값 update
    @Override
    public void updateOrInsertProductionPerformance(Long workOrderDetailId, Long lotMasterId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderDetailId);
        WorkProcess workProcess = workOrderDetail.getWorkProcess();
        WorkProcessDivision workProcessDivision = workProcess.getWorkProcessDivision();
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);

        // 있으면 update, 없으면 create
        ProductionPerformance productionPerformance = getProductionPerformanceOrCreate(workOrderDetail);
        productionPerformance.setLotMaster(lotMaster);      // lotMaster update

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

    @Override
    public ProductionPerformance getProductionPerformanceOrCreate(WorkOrderDetail workOrderDetail) {
        ProductionPerformance productionPerformance = productionPerformanceRepo.findByProduceOrderId(workOrderDetail.getProduceOrder().getId())
                .orElseGet(ProductionPerformance::new);
        // 작업지시에 해당하는 생산실적이 없고, 작업지시의 상태값이 완료 일때 새로 생성
        if (productionPerformance.getId() == null && workOrderDetail.getOrderState().equals(COMPLETION)) {
            productionPerformance.setWorkOrderDetail(workOrderDetail);
            productionPerformanceRepo.save(productionPerformance);
        }
        return productionPerformance;
    }

    private LotMaster getLotMasterOrThrow(Long id) throws NotFoundException {
        return lotMasterRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[ProductionPerformanceHelper] lotMaster does not exist. input id:" + id));
    }

    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[ProductionPerformanceHelper] workOrderDetail does not exist. input id: " + id));
    }
}
