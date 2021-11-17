package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.BomMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BomMasterRepositoryCustom {
//    검색조건: 품목계정, 품목그룹, 품번|품명
    Page<BomMaster> findAllByCondition(Long itemAccountId, Long itemGroupId, String itemNoOrName, Pageable pageable);
}
