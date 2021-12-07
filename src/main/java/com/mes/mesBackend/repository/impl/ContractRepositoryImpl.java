package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.entity.QContract;
import com.mes.mesBackend.repository.custom.ContractRepositoryCustom;
import com.mes.mesBackend.repository.custom.EstimateRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepositoryCustom {
    // 수주 리스트 조회 검색조건 : 거래처, 수주기간, 화폐, 담당자

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QContract contract = QContract.contract;

    @Override
    public List<Contract> findAllByCondition(
            String clientName,
            String userName,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId
    ) {
        return jpaQueryFactory
                .selectFrom(contract)
                .where(
                        isClientNameContaining(clientName),
                        isUserNameContaining(userName),
                        isCurrencyEq(currencyId),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 거래처명 조회
    private BooleanExpression isClientNameContaining(String clientName) {
        return clientName != null ? contract.client.clientName.contains(clientName) : null;
    }

    // 담당자 조회
    private BooleanExpression isUserNameContaining(String userName) {
        return userName != null ? contract.user.korName.contains(userName) : null;
    }

    // 수주기간 조회
//    private BooleanExpression isContractDateBetween(LocalDate fromDate, LocalDate toDate) {
//        return fromDate != null ? contract.contractDate.
//    }

    // 화폐 조회
    private BooleanExpression isCurrencyEq(Long currencyId) {
        return currencyId != null ? contract.currency.id.eq(currencyId) : null;
    }


    private BooleanExpression isDeleteYnFalse() {
        return contract.deleteYn.isFalse();
    }
}
