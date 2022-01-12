package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.MeasureCalibrationResponse;
import com.mes.mesBackend.entity.QDepartment;
import com.mes.mesBackend.entity.QGaugeType;
import com.mes.mesBackend.entity.QMeasure;
import com.mes.mesBackend.entity.QMeasureCalibration;
import com.mes.mesBackend.repository.custom.MeasureCalibrationRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 17-5. 계측기 검교정 실적 등록
@RequiredArgsConstructor
public class MeasureCalibrationRepositoryImpl implements MeasureCalibrationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QMeasure measure = QMeasure.measure;
    final QMeasureCalibration measureCalibration = QMeasureCalibration.measureCalibration;
    final QDepartment department = QDepartment.department;
    final QGaugeType gaugeType = QGaugeType.gaugeType1;

    // 계측기 검교정 실적 단일 조회
    @Override
    public Optional<MeasureCalibrationResponse> findMeasureCalibrationResponseById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        MeasureCalibrationResponse.class,
                                        measureCalibration.id.as("id"),
                                        measureCalibration.measure.id.as("measureId"),
                                        measure.gaugeCode.as("gaugeCode"),
                                        measure.gaugeName.as("gaugeName"),
                                        measure.gaugeType.gaugeType.as("gaugeType"),
                                        measureCalibration.calibrationMethod.as("calibrationMethod"),
                                        measureCalibration.requestDepartment.id.as("requestDepartmentId"),
                                        measureCalibration.requestDepartment.deptName.as("requestDepartmentName"),
                                        measureCalibration.requestDate.as("requestDate"),
                                        measureCalibration.calibrationDate.as("calibrationDate"),
                                        measureCalibration.calibrationResult.as("calibrationResult"),
                                        measureCalibration.price.as("price"),
                                        measureCalibration.reportFileUrl.as("reportFileUrl"),
                                        measureCalibration.note.as("note"),
                                        measureCalibration.measure.department.deptName.as("userDepartmentName")
                                )
                        )
                        .from(measureCalibration)
                        .innerJoin(measure).on(measure.id.eq(measureCalibration.measure.id))
                        .leftJoin(gaugeType).on(gaugeType.id.eq(measure.gaugeType.id))
                        .leftJoin(department).on(department.id.eq(measureCalibration.requestDepartment.id))
                        .where(
                                measureCalibration.id.eq(id),
                                measureCalibration.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }
    // 계측기 검교정 실적 리스트 검색 조회, 검색조건: 검정처(부서 id), 측정기유형(계측기유형), 검정기간(검교정일자) fromDate~toDate
    @Override
    public List<MeasureCalibrationResponse> findMeasureCalibrationResponsesByCondition(Long departmentId, Long gaugeTypeId, LocalDate fromDate, LocalDate toDate) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                MeasureCalibrationResponse.class,
                                measureCalibration.id.as("id"),
                                measureCalibration.measure.id.as("measureId"),
                                measure.gaugeCode.as("gaugeCode"),
                                measure.gaugeName.as("gaugeName"),
                                measure.gaugeType.gaugeType.as("gaugeType"),
                                measureCalibration.calibrationMethod.as("calibrationMethod"),
                                measureCalibration.requestDepartment.id.as("requestDepartmentId"),
                                measureCalibration.requestDepartment.deptName.as("requestDepartmentName"),
                                measureCalibration.requestDate.as("requestDate"),
                                measureCalibration.calibrationDate.as("calibrationDate"),
                                measureCalibration.calibrationResult.as("calibrationResult"),
                                measureCalibration.price.as("price"),
                                measureCalibration.reportFileUrl.as("reportFileUrl"),
                                measureCalibration.note.as("note"),
                                measureCalibration.measure.department.deptName.as("userDepartmentName")
                        )
                )
                .from(measureCalibration)
                .innerJoin(measure).on(measure.id.eq(measureCalibration.measure.id))
                .leftJoin(gaugeType).on(gaugeType.id.eq(measure.gaugeType.id))
                .leftJoin(department).on(department.id.eq(measureCalibration.requestDepartment.id))
                .where(
                        isDepartmentIdEq(departmentId),
                        isGaugeTypeIdEq(gaugeTypeId),
                        isCalibrationDateBetween(fromDate, toDate),
                        measureCalibration.deleteYn.isFalse()
                )
                .fetch();
    }

//    검색조건: 검정처(부서 id)
    private BooleanExpression isDepartmentIdEq(Long departmentId) {
        return departmentId != null ? measureCalibration.requestDepartment.id.eq(departmentId) : null;
    }

    // 측정기유형(계측기유형)
    private BooleanExpression isGaugeTypeIdEq(Long gaugeTypeId) {
        return gaugeTypeId != null ? gaugeType.id.eq(gaugeTypeId) : null;
    }

    // 검정기간(검교정일자) fromDate~toDate
    private BooleanExpression isCalibrationDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? measureCalibration.calibrationDate.between(fromDate, toDate) : null;
    }


}
