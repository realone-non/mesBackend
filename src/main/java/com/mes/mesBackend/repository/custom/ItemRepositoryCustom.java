package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    // 품목그룹, 품목계정, 품번, 품명, 검색어
    Page<Item> findAllByCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, String search, Pageable pageable);
}
