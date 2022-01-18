package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaterialStockInspectRequestRepositoryCustom {
    //재고실사의뢰 전체 선택 /검색: 실사기간
    List<MaterialStockInspectRequestResponse> findAllByCondition(LocalDate fromDate, LocalDate toDate);

    //재고실사의뢰 단건 조회
    Optional<MaterialStockInspectRequestResponse> findByIdAndDeleteYn(Long id);
}
