package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;

// productionPerformance
// 작업지시의 지시수량와 작업수량이 같아서 COMPLETION 으로 변경되었을때 사용
public interface ProductionPerformanceHelper {
    // 있으면 update, 없으면 create
    // workOrderDetail 의 orderState 가 완료일 경우에만 insert
    void updateOrInsertProductionPerformance(Long workOrderDetailId, Long lotMasterId) throws NotFoundException;

    // 제조오더에 해당하는 productionPerformance 가 없고, 작업시지의 상태값이 COMPLETION 이면 생성, 있으면 공정에 해당하는 컬럼에 값 update
    // 생산실적 단일 조회 및 생성
    ProductionPerformance getProductionPerformanceOrCreate(WorkOrderDetail workOrderDetail);
}
