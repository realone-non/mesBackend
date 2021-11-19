package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.ItemCheck;
import com.mes.mesBackend.entity.enumeration.TestCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCheckRepositoryCustom {
    // 품목별 검사항목 페이징 조회 /  검색조건: 검사유형, 품목그룹, 품목계정
    Page<ItemCheck> findAllCondition(TestCategory testCategory, Long itemGroup, Long itemAccount, Pageable pageable);
}
