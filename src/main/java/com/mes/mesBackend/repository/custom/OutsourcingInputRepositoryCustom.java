package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.OutsourcingInputResponse;
import com.mes.mesBackend.dto.response.OutsourcingProductionResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OutsourcingInputRepositoryCustom {
    //외주 입고 정보 검색 조건:외주처, 품목, 입고기간
    List<OutsourcingInputResponse> findAllByCondition(Long clientId, Long itemId, LocalDate startDate, LocalDate endDate);

    //외주생산의뢰 단일 조회
    Optional<OutsourcingInputResponse> findInputByIdAndDeleteYnAndUseYn(Long id);

    //아이템 ID조회
    Long findItemIdByInputId(Long inputId);
}
