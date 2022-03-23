package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PopWorkOrderStates;
import com.mes.mesBackend.entity.LotEquipmentConnect;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotEquipmentConnectRepositoryCustom {
    // 오늘날짜, 같은 설비 기준으로 equipmentLot 조회
    Optional<LotEquipmentConnect> findByTodayAndEquipmentId(Long equipmentId, LocalDate now, Long dummyLotId);
    // 설비 lot id 로 조회
    Optional<LotEquipmentConnect> findByChildId(Long childLotId);
    // dummyLot 로 조회
    List<PopWorkOrderStates> findPopWorkOrderStates(Long dummyLotId);
    // 작업공정이 원료혼합이고, 오늘 생산된 lotMaster 찾아야함
    Optional<LotEquipmentConnect> findByTodayAndWorkProcessDivision(LocalDate now, WorkProcessDivision workProcessDivision, Long workOrderId);
    // parentLotId 로 childLot(equipmentLot) 조회
    List<LotMaster> findChildLotByParentLotId(Long parentLotId);
    // parentLotId 로 가장 마지막에 생성 된 equipmentLot 조회
    List<LotMaster> findChildLotByChildLotOfParentLotCreatedDateDesc(Long dummyLotId);
    // realLotId 로 equipmentLot 조회, 조회조건: 제일 마지막에 생성된 equipmentLot
    Optional<LotEquipmentConnect> findEquipmentLotByRealLotIdOrderByCreatedDateDesc(Long realLotId);
}
