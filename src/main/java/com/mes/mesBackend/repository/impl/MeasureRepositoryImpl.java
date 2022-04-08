package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Measure;
import com.mes.mesBackend.entity.QMeasure;
import com.mes.mesBackend.repository.custom.MeasureRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class MeasureRepositoryImpl implements MeasureRepositoryCustom {
    // 계측기 전체 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)

    private final JPAQueryFactory jpaQueryFactory;

    final QMeasure measure = QMeasure.measure;

    @Override
    @Transactional(readOnly = true)
    public List<Measure> findAllByCondition(Long gaugeTypeId, Integer calibrationCycle) {
        return jpaQueryFactory
                .selectFrom(measure)
                .where(
                        isGaugeTypeEq(gaugeTypeId),
                        isCalibrationCycleEq(calibrationCycle),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // Gauge 유형으로 검색
    private BooleanExpression isGaugeTypeEq(Long gaugeTypeId) {
        return gaugeTypeId != null ? measure.gaugeType.id.eq(gaugeTypeId) : null;
    }

    // 검교정주기 검색
    private BooleanExpression isCalibrationCycleEq(Integer calibrationCycle) {
        return calibrationCycle != null ? measure.calibrationCycle.eq(calibrationCycle) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return measure.deleteYn.isFalse();
    }

// 계측기 페이징 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
//    @Override
//    public Page<Measure> findAllByCondition(Long gaugeTypeId, Long month, Pageable pageable) {
//        QueryResults<Measure> results = jpaQueryFactory
//                .selectFrom(measure)
//                .where(
//                        isGaugeTypeEq(gaugeTypeId),
//                        isCalibrationDateEq(month),
//                        isDeleteYnFalse()
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//    }
}
