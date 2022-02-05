package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.BadItemEnrollmentResponse;
import com.mes.mesBackend.dto.response.PopBadItemTypeResponse;
import com.mes.mesBackend.dto.response.PopTestBadItemResponse;

import java.util.List;
import java.util.Optional;

public interface WorkOrderBadItemRepositoryCustom {
    Optional<BadItemEnrollmentResponse> findWorkOrderEnrollmentResponseById(Long id);
    List<BadItemEnrollmentResponse> findWorkOrderEnrollmentResponsesByWorkOrderId(Long workOrderId);
    // 해당하는 lot 의 badItem 모두
    List<Long> findBadItemIdByLotMasterId(Long lotMasterId);
    // 작업공정에 해당하는 badItemId
    List<Long> findBadItemIdByWorkOrderId(Long workProcessId);
    // pop 작업공정에 해당하는 badItemType 모두
    List<PopBadItemTypeResponse> findPopBadItemTypeByWorkProcessId(Long workProcessId);
    // lot 에 해당하는거 모두 조회
    List<PopTestBadItemResponse> findPopTestBadItemResponseByLotMasterId(Long lotMasterId);
}
