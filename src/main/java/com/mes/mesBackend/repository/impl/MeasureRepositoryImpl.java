package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Measure;
import com.mes.mesBackend.entity.QMeasure;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.custom.MeasureRepositoryCustom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MeasureRepositoryImpl implements MeasureRepositoryCustom {
    // 계측기 페이징 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)

    @Autowired
    JPAQueryFactory jpaQueryFactory;
    @Autowired
    ModelMapper mapper;

    final QMeasure measure = QMeasure.measure;

    @Override
    public Page<Measure> findAllByCondition(Long gaugeTypeId, Long month, Pageable pageable) {
        QueryResults<Measure> results = jpaQueryFactory
                .selectFrom(measure)
                .where(
                        isGaugeTypeEq(gaugeTypeId),
                        isCalibrationDateEq(month),
                        isDeleteYnFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // Gauge 유형으로 검색
    private BooleanExpression isGaugeTypeEq(Long gaugeTypeId) {
        return gaugeTypeId != null ? measure.gaugeType.id.eq(gaugeTypeId) : null;
    }

    // 월로 검색
    private BooleanExpression isCalibrationDateEq(Long month) {
        return month != null ? measure.calibrationNextDate.month().eq(Math.toIntExact(month)) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return measure.deleteYn.isFalse();
    }
}
