package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Item;

import java.util.List;

public interface ItemRepositoryCustom {
    // 품목그룹, 품목계정, 품번, 품명, 검색어
    List<Item> findAllByCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, String search);
    // 품목그룹, 품목계정, 품번, 품명, 검색어
//    Page<Item> findAllByCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, String search, Pageable pageable);
}
