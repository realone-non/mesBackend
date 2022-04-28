package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PopWorkOrderStates;
import com.mes.mesBackend.dto.response.WorkOrderDetailResponse;
import com.mes.mesBackend.entity.LotEquipmentConnect;
import com.mes.mesBackend.entity.LotMaster;

import java.util.List;
import java.util.Optional;

public interface LotEquipmentConnectRepositoryCustom {
    // 설비 lot id 로 조회
    Optional<LotEquipmentConnect> findByChildId(Long childLotId);
    // dummyLot 로 조회
    List<PopWorkOrderStates> findPopWorkOrderStates(Long dummyLotId);
    // parentLotId 로 가장 마지막에 생성 된 equipmentLot 조회
    List<LotMaster> findChildLotByChildLotOfParentLotCreatedDateDesc(Long dummyLotId);
    // dummyLot 로 조회
    List<WorkOrderDetailResponse> findWorkOrderDetailResponseByDummyLotId(Long dummyLotId);
    Optional<LotMaster> findEquipmentLotByIdAndStockAmount(Long equipmentId, int stockAmount);
    // 설비 lot 로 분할 된 lot 모두 조회
    List<LotMaster> findRealLotByEquipmentLot(Long equipmentLotId);
}
