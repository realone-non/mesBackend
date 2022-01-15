package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ItemLogResponse;

import java.time.LocalDate;
import java.util.List;

public interface ItemLogRepositoryCustom {
    //일자별 품목 변동 사항 전체 조회 / 검색조건 : 수불일자, 창고, 생성기간
    List<ItemLogResponse> findAllCondition(Long warehouseId, LocalDate startDate, LocalDate endDate);

    //일자별 품목 변동 사항 단일 조회
    ItemLogResponse findByItemIdAndwareHouseId(Long itemId, Long warehouseId);
}
