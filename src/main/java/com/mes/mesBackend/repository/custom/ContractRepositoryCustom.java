package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.dto.response.DeadlineResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.entity.Contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractRepositoryCustom {
    // 수주 리스트 조회 검색조건 : 거래처, 수주기간, 화폐, 담당자
    List<Contract> findAllByCondition(String clientName, String userName, LocalDate fromDate, LocalDate toDate, Long currencyId, Boolean deadlineDateNullYn);
    // 수주 등록된 제조사 list 조회 api
    List<ClientResponse.CodeAndName> findContractClientResponse();
    // 납기일자 오늘 까지인 수주 갯수
    Optional<Long> findContractPeriodDateByTodayAmountSum();

    // 매출관련현황
    // 해당 달
    List<ItemResponse.noAndName> findSalesRelatedStatusResponseByContractItems(LocalDate fromDate, LocalDate toDate);
    // 주에 해당하는 품목 별 수주수량 갯수
    Optional<Integer> findWeekAmountByWeekDate(LocalDate fromDate, LocalDate toDate, Long itemId);

    // 마감일자 단일 조회
    Optional<DeadlineResponse> findDeadlineResponseByContractId(Long id);
    // 마감일자 리스트 조회
    List<DeadlineResponse> findDeadlineResponsesByNotDeadlineDate();

}
