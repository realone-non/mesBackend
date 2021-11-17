package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.SubItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubItemRepositoryCustom {
    // 대체품 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    Page<SubItem> findAllCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Pageable pageable);
}
