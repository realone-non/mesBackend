package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;

// productionPerformance
public interface ProductionPerformanceHelper {
    // 해당하는 공정의 맞게 ProductionPerformance 수정
    void updateProductionPerformance(Long workOrderDetailId, Long lotMasterId, WorkProcessDivision workProcessDivision) throws NotFoundException;
}
