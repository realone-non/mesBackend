package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.BadItemEnrollmentResponse;

import java.util.List;
import java.util.Optional;

public interface WorkOrderBadItemRepositoryCustom {
    Optional<BadItemEnrollmentResponse> findWorkOrderEnrollmentResponseById(Long id);
    List<BadItemEnrollmentResponse> findWorkOrderEnrollmentResponsesByWorkOrderId(Long workOrderId);
    // 해당하는 lot 의 badItem 모두
    List<Long> findBadItemIdByLotMasterId(Long lotMasterId);
    // 작업공정에 해당하는 badItemId
    List<Long> findBadItemIdByWorkOrderId(Long workProcessId);
}
