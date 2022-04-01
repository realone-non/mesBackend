package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.Contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractRepositoryCustom {
    // 수주 리스트 조회 검색조건 : 거래처, 수주기간, 화폐, 담당자
    List<Contract> findAllByCondition(String clientName, String userName, LocalDate fromDate, LocalDate toDate, Long currencyId);
    // 수주 등록된 제조사 list 조회 api
    List<ClientResponse.CodeAndName> findContractClientResponse();
    // 납기일자 오늘 까지인 수주 갯수
    Optional<Long> findContractPeriodDateByTodayAmountSum();
}
