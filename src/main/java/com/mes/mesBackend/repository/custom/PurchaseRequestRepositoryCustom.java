package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PurchaseRequestResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchaseRequestRepositoryCustom {
    List<Long> findItemIdByContractItemId(Long itemId);
    Optional<PurchaseRequestResponse> findByIdAndOrderStateSchedule(Long id);
    // 구매요청 리스트 조회, 검색조건: 요청기간, 제조오더번호, 품목그룹, 품번|품명, 제조사 품번, 완료포함(check)
    List<PurchaseRequestResponse> findAllByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            String produceOrderNo,
            Long itemGroupId,
            String itemNoAndName,
            String manufacturerPartNo,
            boolean orderCompletion
    );
}
