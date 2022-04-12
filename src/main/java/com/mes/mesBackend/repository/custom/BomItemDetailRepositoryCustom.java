package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.BomItemDetailResponse;

import java.util.List;

public interface BomItemDetailRepositoryCustom {
    // 검색조건: 품목|품명
    List<BomItemDetailResponse> findAllByCondition(Long bomMasterId, String itemNoOrItemName);
    // 같은 품목정보가 등록 되어잇는지
    boolean existsByBomItemDetailItem(Long bomMasterId, Long itemId);
}
