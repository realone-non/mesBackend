package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.BomItemDetail;
import com.mes.mesBackend.entity.BomMaster;

import java.util.List;

public interface BomItemDetailRepositoryCustom {
    // 검색조건: 품목|품명
    List<BomItemDetail> findAllByCondition(BomMaster bomMaster, String itemNoOrItemName);
}
