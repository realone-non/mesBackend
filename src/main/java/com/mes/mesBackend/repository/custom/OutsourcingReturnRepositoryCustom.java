package com.mes.mesBackend.repository.custom;
import com.mes.mesBackend.dto.response.OutsourcingReturnResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface OutsourcingReturnRepositoryCustom {
    //외주반품 전체 조회 검색 조건:외주처, 품목, 반품기간
    List<OutsourcingReturnResponse> findAllByCondition(Long clientId, String itemNoAndItemName, LocalDate startDate, LocalDate endDate);
    //외주반품 단일 조회
    Optional<OutsourcingReturnResponse> findReturnByIdAndDeleteYnAndUseYn(Long id);
}