package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.entity.QEstimate;
import com.mes.mesBackend.repository.custom.EstimateRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class EstimateRepositoryImpl implements EstimateRepositoryCustom {
    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QEstimate estimate = QEstimate.estimate;

    @Override
    public Page<Estimate> findAllByCondition(
            String clientName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long currencyId,
            String chargeName
    ) {
        return null;
    }

    // 거래처 명 조회
    private BooleanExpression isClientNameContaining(String clientName) {
        return clientName != null ? estimate.client.clientName.contains(clientName) : null;
    }

    // 견적기간 조회
    private BooleanExpression isEstimateDate(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate != null ? estimate.estimateDate.
    }
    // 화폐로 조회
    // 담당자명 조회
}
