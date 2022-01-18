package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.MaterialStockInspectResponse;
import com.mes.mesBackend.entity.MaterialStockInspect;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaterialStockInspectRepositoryCustom {
    //재고조사 전체 선택 / 검색 : 실사기간, 품목계정
    List<MaterialStockInspectResponse> findAllByCondition(Long requestId, LocalDate fromDate, LocalDate toDate, String itemAccount);

    //재고조사 단일 선택
    Optional<MaterialStockInspectResponse> findByIdAndDeleteYn(Long id);

    //DB재고실사 가져오기
    List<MaterialStockInspect> findInspectFromDB(Long itemAccountId);
}
