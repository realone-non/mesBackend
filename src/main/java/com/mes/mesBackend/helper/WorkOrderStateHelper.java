package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;

// 작업지시 orderState 변경
public interface WorkOrderStateHelper {
    // 작업지시 orderState 변경
    /*
    * - orderState 별로 workOrderDetail 의 scheduleDate, startDate, endDate 값 update
    * - 제조오더에 해당하는 젤 마지막 등록 작업지시의 orderState 값으로 제조오더 orderState 값 update
    * - 제조오더에 해당하는 productionPerformance 가 없을 시 생성, 있으면 공정에 해당하는 컬럼에 값 update
    * */
    void updateOrderState(Long workOrderDetailId, OrderState orderState, Long lotMasterId) throws NotFoundException;
    // 생산실적 단일 조회 및 생성
    ProductionPerformance getProductionPerformanceOrCreate(WorkOrderDetail workOrderDetail);
}
