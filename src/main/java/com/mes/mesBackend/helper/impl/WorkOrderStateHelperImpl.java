package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.ProductionPerformanceRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;
import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;

// 작업지시 orderState 변경
@Component
@RequiredArgsConstructor
public class WorkOrderStateHelperImpl implements WorkOrderStateHelper {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ProduceOrderRepository produceOrderRepo;
    private final ProductionPerformanceRepository productionPerformanceRepo;
    private final LotMasterRepository lotMasterRepo;


    // 작업지시 orderState 변경
    /*
     * - orderState 별로 workOrderDetail 의 scheduleDate, startDate, endDate 값 update
     * - 제조오더에 해당하는 젤 마지막 등록 작업지시의 orderState 값으로 제조오더 orderState 값 update
     * - 제조오더에 해당하는 productionPerformance 가 없고, 작업시지의 상태값이 COMPLETION 이면 생성, 있으면 공정에 해당하는 컬럼에 값 update
     * */
    @Override
    public void updateOrderState(Long workOrderDetailId, OrderState orderState, Long lotMasterId) throws NotFoundException {
        // 작업지시: orderState 따라 작업지시 날짜값 update
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderDetailId);
        workOrder.changeOrderStateDate();
        workOrderDetailRepo.save(workOrder);

        // 제조오더: 작업지시 상태값 따라서 제조오더 상태값 update
        ProduceOrder produceOrder = getProduceOrderOrThrow(workOrder.getProduceOrder().getId());
        produceOrder.setOrderState(getWorkOrderStateDesc(produceOrder.getId()));
        produceOrderRepo.save(produceOrder);

        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        // 생산실적: 작업지시에 해당하는 생산실적 없으면 새로 생성, 있으면 공정에 해당하는 컬럼에 update
        ProductionPerformance productionPerformance = getProductionPerformanceOrCreate(workOrder);
        productionPerformance.updateProcessDateTime(workOrder.getWorkProcess(), workOrder.getOrderState());
        productionPerformance.setLotMaster(lotMaster);
        productionPerformance.setWorkOrderDetail(workOrder);
        productionPerformanceRepo.save(productionPerformance);

    }

    // lot 단일 조회 및 예외
    private LotMaster getLotMasterOrThrow(Long id) {
        return lotMasterRepo.findByIdAndDeleteYnFalse(id).orElse(null);
    }

    // 제조오더에 해당하는 가장 최근 등록된 작업지시의 orderState
    private OrderState getWorkOrderStateDesc(Long produceOrderId) {
        return workOrderDetailRepo.findOrderStatesByProduceOrderId(produceOrderId)
                .orElse(SCHEDULE);
    }

    // 작업지시 단일 조회 및 예외
    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[workOrderStateHelperError] workOrderDetail does not exist. workOrderDetailId: " + id));
    }

    // 제조오더 단일 조회 및 예외
    private ProduceOrder getProduceOrderOrThrow(Long id) throws NotFoundException {
        return produceOrderRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[workOrderStateHelperError] produceOrder does not exist. produceOrderId: " + id));
    }

    // 생산실적 단일 조회 및 생성
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
}
