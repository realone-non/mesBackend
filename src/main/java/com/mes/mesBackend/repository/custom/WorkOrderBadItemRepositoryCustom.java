package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.BadItemEnrollmentResponse;
import com.mes.mesBackend.dto.response.PopBadItemTypeResponse;
import com.mes.mesBackend.dto.response.PopTestBadItemResponse;
import com.mes.mesBackend.dto.response.WorkOrderDetailBadItemResponse;
import com.mes.mesBackend.entity.BadItem;
import com.mes.mesBackend.entity.WorkOrderBadItem;

import java.util.List;
import java.util.Optional;

public interface WorkOrderBadItemRepositoryCustom {
    // 해당하는 lot 의 badItem 모두
    List<Long> findBadItemIdByLotMasterId(Long lotMasterId);
    // 작업공정에 해당하는 badItemId
    List<Long> findBadItemIdByWorkOrderId(Long workProcessId);
    // pop 작업공정에 해당하는 badItemType 모두
    List<PopBadItemTypeResponse> findPopBadItemTypeByWorkProcessId(Long workProcessId);
    // lot 에 해당하는거 모두 조회
    List<PopTestBadItemResponse> findPopTestBadItemResponseByLotMasterId(Long lotMasterId);
    // 불량항목 전체 조회
    List<BadItem> findBadItemByCondition(Long workProcessId);
    // equipmentLot 에 해당하는 불량수량 모두
    List<Integer> findBadItemAmountByEquipmentLotMaster(Long equipmentLotId);
    // 공정에 해당하는 첫번째 불량유형
    Optional<BadItem> findByWorkOrderIdLimitOne(Long workOrderId);
    // dummyLot 에 해당되는 불량수량 모두
    List<Integer> findBadItemAmountByDummyLotMaster(Long dummyLotId);
    // dummyLot 에 해당하는 불량유형 별 불량수량
    List<BadItemEnrollmentResponse> findByDummyLotIdGroupByBadItemType(Long dummyLotId);
    List<WorkOrderBadItem> findByWorkOrderDetailIdAndBadItemId(Long workOrderDetailId, Long badItemId);
    // 설비 lot 에 해당하는 등록된 불량 전체 조회
    List<WorkOrderDetailBadItemResponse> findWorkOrderDetailBadItemResponseByEquipmentLotId(Long equipmentId);
    // 불량유형이 불량등록 정보에 존재하는지
    boolean existByBadItemAndDeleteYnFalse(Long badItemId);
}
