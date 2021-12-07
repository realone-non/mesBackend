package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Contract;

import java.time.LocalDate;
import java.util.List;

public interface ContractRepositoryCustom {
    // 수주 리스트 조회 검색조건 : 거래처, 수주기간, 화폐, 담당자
    List<Contract> findAllByCondition(String clientName, String userName, LocalDate fromDate, LocalDate toDate, Long currencyId);
}
