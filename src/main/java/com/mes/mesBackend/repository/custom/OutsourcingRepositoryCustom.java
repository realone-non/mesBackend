package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.OutsourcingProductionResponse;
import com.mes.mesBackend.dto.response.OutsourcingStatusResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OutsourcingRepositoryCustom {
    //외주생산의뢰 전체 조회 검색 조건:외주처, 생산품목, 의뢰기간
    List<OutsourcingProductionResponse> findAllByCondition(Long clientId, String itemNo, String itemName, LocalDate startDate, LocalDate endDate);

    //외주생산의뢰 단일 조회
    Optional<OutsourcingProductionResponse> findRequestByIdAndDeleteYnAndUseYn(Long id);

    // 해당 품목이 외주생산의뢰에 등록되어 있는지
    boolean existsItemByOutsourcingRequest(Long itemId);

}
