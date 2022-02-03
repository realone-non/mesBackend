package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.LotConnect;

import java.util.List;

public interface LotConnectRepositoryCustom {
    // 부모 lotMaster 랑 같고, 자식 lotMaster 의 item 이 파라미터 itemId 와 같고, 구분값이 EXHAUST 인것 조회
    List<LotConnect> findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(Long itemIdOfChildLotMaster);
}
