package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.BomItemDetailResponse;

import java.util.List;

public interface BomItemDetailRepositoryCustom {
    // 검색조건: 품목|품명
    List<BomItemDetailResponse> findAllByCondition(Long bomMasterId, String itemNoOrItemName);
}
