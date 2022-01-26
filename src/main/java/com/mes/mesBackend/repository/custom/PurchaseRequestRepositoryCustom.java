package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PopPurchaseRequestResponse;
import com.mes.mesBackend.dto.response.PurchaseRequestResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
            Boolean orderCompletion
    );

    // 구매발주에 해당하는 구매요청이 있는지.
    // return: clientId
    List<Long> findClientIdsByPurchaseOrder(Long purchaseOrderId);
    // 구매발주에 해당하는 구매요청의 orderAmount 모두
    List<Integer> findOrderAmountByPurchaseOrderId(Long purchaseOrderId);
    // 제조오더에 해당하는 구매요청의 requestAmount 모두
    List<Integer> findRequestAmountByProduceOrderId(Long produceOrderId);
    // pop 해당 구매발주에 해당하는 구매요청정보 list 조회
    List<PopPurchaseRequestResponse> findPopPurchaseRequestResponseByPurchaseOrderId(Long purchaseOrderId);
    // 특정 날짜에 입고예정인 품목 검색(Shortage)
    List<Map<Long, Integer>> findItemByItemAndDateForShortage(LocalDate fromDate);
}
