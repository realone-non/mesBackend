package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.entity.QEstimate;
import com.mes.mesBackend.repository.custom.EstimateRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class EstimateRepositoryImpl implements EstimateRepositoryCustom {
    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자

    private final JPAQueryFactory jpaQueryFactory;

    final QEstimate estimate = QEstimate.estimate;

    @Override
    @Transactional(readOnly = true)
    public List<Estimate> findAllByCondition(
            String clientName,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            String chargeName
    ) {
        return jpaQueryFactory
                .selectFrom(estimate)
                .where(
                        isClientNameContaining(clientName),
                        isEstimateDate(fromDate, toDate),
                        isCurrencyEq(currencyId),
                        isChargeNameContaining(chargeName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 거래처 명 조회
    private BooleanExpression isClientNameContaining(String clientName) {
        return clientName != null ? estimate.client.clientName.contains(clientName) : null;
    }

    // 견적기간 조회
    private BooleanExpression isEstimateDate(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? estimate.estimateDate.between(fromDate, toDate) : null;
    }

    // 화폐로 조회
    private BooleanExpression isCurrencyEq(Long currencyId) {
        return currencyId != null ? estimate.currency.id.eq(currencyId) : null;
    }

    // 담당자명 조회
    private BooleanExpression isChargeNameContaining(String chargeName) {
        return chargeName != null ? estimate.user.korName.contains(chargeName) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return estimate.deleteYn.isFalse();
    }

//    @Override
//    public Page<Estimate> findAllByCondition(
//            String clientName,
//            LocalDate fromDate,
//            LocalDate toDate,
//            Long currencyId,
//            String chargeName,
//            Pageable pageable
//    ) {
//        QueryResults<Estimate> results = jpaQueryFactory
//                .selectFrom(estimate)
//                .where(
//                        isClientNameContaining(clientName),
//                        isEstimateDate(fromDate, toDate),
//                        isCurrencyEq(currencyId),
//                        isChargeNameContaining(chargeName),
//                        isDeleteYnFalse()
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//    }
}
