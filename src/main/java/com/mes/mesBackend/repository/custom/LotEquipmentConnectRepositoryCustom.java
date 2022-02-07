package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PopWorkOrderStates;
import com.mes.mesBackend.entity.LotEquipmentConnect;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotEquipmentConnectRepositoryCustom {
    // 오늘날짜, 같은 설비 기준으로 equipmentLot 조회
    Optional<LotEquipmentConnect> findByTodayAndEquipmentId(Long equipmentId, LocalDate now);
    // 설비 lot id 로 조회
    Optional<LotEquipmentConnect> findByChildId(Long childLotId);
    // dummyLot 로 조회
    List<PopWorkOrderStates> findPopWorkOrderStates(Long dummyLotId);
}
