package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.WorkDocument;

import java.util.List;

public interface WorkDocumentRepositoryCustom {
    // 작업표준서 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명목
    List<WorkDocument> findAllByCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName);

    // 작업표준서 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명목
//    Page<WorkDocument> findAllByCondition(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Pageable pageable);
}
