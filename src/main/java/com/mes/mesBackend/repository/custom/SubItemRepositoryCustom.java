package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.SubItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubItemRepositoryCustom {
    // 대체품 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    List<SubItem> findAllCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName);

    // 대체품 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
//    Page<SubItem> findAllCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Pageable pageable);
}
