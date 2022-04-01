package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.entity.QContract;
import com.mes.mesBackend.repository.custom.ContractRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepositoryCustom {
    // 수주 리스트 조회 검색조건 : 거래처, 수주기간, 화폐, 담당자

    private final JPAQueryFactory jpaQueryFactory;

    final QContract contract = QContract.contract;

    @Override
    @Transactional(readOnly = true)
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
                        isContractDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .orderBy(contract.createdDate.desc())
                .fetch();
    }

    // 수주 등록된 제조사 list 조회 api
    @Override
    public List<ClientResponse.CodeAndName> findContractClientResponse() {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ClientResponse.CodeAndName.class,
                                contract.client.id.as("id"),
                                contract.client.clientCode.as("clientCode"),
                                contract.client.clientName.as("clientName")
                        )
                )
                .from(contract)
                .where(
                        contract.deleteYn.isFalse()
                )
                .distinct()
                .fetch();
    }

    // 납기일자 남은거(오늘 이후)
    @Override
    public Optional<Long> findContractPeriodDateByTodayAmountSum() {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(contract.id.count())
                        .from(contract)
                        .where(
                                contract.deleteYn.isFalse(),
                                contract.periodDate.after(LocalDate.now().minusDays(1))
                        )
                        .fetchOne()
        );
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
    private BooleanExpression isContractDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? contract.contractDate.between(fromDate, toDate) : null;
    }

    // 화폐 조회
    private BooleanExpression isCurrencyEq(Long currencyId) {
        return currencyId != null ? contract.currency.id.eq(currencyId) : null;
    }


    private BooleanExpression isDeleteYnFalse() {
        return contract.deleteYn.isFalse();
    }
}
