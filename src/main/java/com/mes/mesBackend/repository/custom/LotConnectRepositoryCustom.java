package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PopBomDetailLotMasterResponse;
import com.mes.mesBackend.dto.response.PopLotMasterResponse;
import com.mes.mesBackend.entity.LotConnect;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotConnectRepositoryCustom {
    // 부모 lotMaster 랑 같고, 자식 lotMaster 의 item 이 파라미터 itemId 와 같고, 구분값이 EXHAUST 인것 조회
    List<LotConnect> findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(Long itemIdOfChildLotMaster, Long lotMasterId);
    // 검색조건: 부모 lotMaster, 자식 lotMaster, 구분 값 EXHAUST
    Optional<LotConnect> findByParentLotIdAndChildLotIdAndDivisionExhaust(Long parentLotMasterId, Long childLotMasterId);
    // 부모 lotMaster 와 같은 자식 lotMasterId 모두 조회, 구분 값 EXHAUST
    List<Long> findChildLotIdByParentLotIdAndDivisionExhaust(Long parentLotId);
    // 부모 lotMaster 와 같은 자식 lotMaster 모두 조회, 구분 값 EXHAUST
    List<PopBomDetailLotMasterResponse> findExhaustLotResponseByParentLotAndDivisionExhaust(Long parentLotMasterId, Long parentItemId);
    // equipmentLot id 에 해당하는 분할 lot 조회 구분 값: FAMILY
    List<PopLotMasterResponse> findPopLotMasterResponseByEquipmentLotId(Long equipmentLotId);
    // realLot 에 해당하는 equipmentLotId 조회, 구분 값: FAMILY
    Optional<LotConnect> findByChildLotIdAndDivisionFamily(Long childLotId);
    Optional<Long> findDummyLotIdByChildLotId(Long realLotMasterId);
    // equipmentLotId(parentLot.childLot) 로 오늘 생성, 공정 원료혼합
    Optional<LotConnect> findByParentLotOfEquipmentLotId(Long equipmentLotId, WorkProcessDivision workProcessDivision, LocalDate now, Long produceOrderId);
}
