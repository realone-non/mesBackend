package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.EquipmentCheckDetailResponse;
import com.mes.mesBackend.dto.response.EquipmentCheckResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.EquipmentCheckDetailRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

// 17-1. 설비점검 실적 등록
@RequiredArgsConstructor
public class EquipmentCheckDetailRepositoryImpl implements EquipmentCheckDetailRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QEquipment equipment = QEquipment.equipment;
    final QEquipmentCheckDetail equipmentCheckDetail = QEquipmentCheckDetail.equipmentCheckDetail;
    final QEquipmentMaintenance equipmentMaintenance = QEquipmentMaintenance.equipmentMaintenance;
    final QWorkLine workLine = QWorkLine.workLine;
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QWorkCenter workCenter = QWorkCenter.workCenter;

    // 설비 리스트 조회
    // 검색조건: 설비유형, 점검유형(보류), 작업기간(디테일 정보 생성날짜 기준) fromDate~toDate
    @Override
    public List<EquipmentCheckResponse> findEquipmentChecksResponseByCondition(
            Long workLineId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                EquipmentCheckResponse.class,
                                equipment.id.as("id"),
                                equipment.createdDate.as("enrollmentDate"),
                                equipment.equipmentCode.as("equipmentCode"),
                                equipment.equipmentName.as("equipmentName"),
                                workLine.workLineName.as("workCenterName"),
                                workProcess.workProcessName.as("workProcessName"),
                                workCenter.workCenterName.as("workLineName")
                        )
                )
                .from(equipment)
                .leftJoin(workLine).on(workLine.id.eq(equipment.workLine.id))
                .leftJoin(workProcess).on(workProcess.id.eq(workLine.workProcess.id))
                .leftJoin(workCenter).on(workCenter.id.eq(workLine.workCenter.id))
                .leftJoin(equipmentCheckDetail).on(equipmentCheckDetail.equipment.id.eq(equipment.id))
                .where(
                        isEquipmentTypeContain(workLineId),
                        isWorkDateBetween(fromDate, toDate),
                        isEquipmentDeleteYnFalse()
                )
                .fetch();
    }

    // 설비 단일 조회
    @Override
    public Optional<EquipmentCheckResponse> findEquipmentChecksResponseByEquipmentId(Long equipmentId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        EquipmentCheckResponse.class,
                                        equipment.id.as("id"),
                                        equipment.createdDate.as("enrollmentDate"),
                                        equipment.equipmentCode.as("equipmentCode"),
                                        equipment.equipmentName.as("equipmentName"),
                                        workLine.workLineName.as("workCenterName"),
                                        workProcess.workProcessName.as("workProcessName"),
                                        workCenter.workCenterName.as("workLineName")
                                )
                        )
                        .from(equipment)
                        .leftJoin(workLine).on(workLine.id.eq(equipment.workLine.id))
                        .leftJoin(workProcess).on(workProcess.id.eq(workLine.workProcess.id))
                        .leftJoin(workCenter).on(workCenter.id.eq(workLine.workCenter.id))
                        .where(
                                isEquipmentIdEq(equipmentId),
                                isEquipmentDeleteYnFalse()
                        )
                        .fetchOne()
        );
    }

    // ================================================ 설비점검 실적 상세 정보 ================================================
    // 상세정보 전체 조회
    @Override
    public List<EquipmentCheckDetailResponse> findEquipmentCheckDetailResponseByEquipmentId(Long equipmentId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                EquipmentCheckDetailResponse.class,
                                equipmentCheckDetail.id.as("id"),
                                equipmentMaintenance.id.as("maintenanceId"),
                                equipmentMaintenance.maintenanceName.as("maintenanceName"),
                                equipmentCheckDetail.checkContent.as("checkContent"),
                                equipmentCheckDetail.criteriaStandard.as("criteriaStandard"),
                                equipmentCheckDetail.criteriaMethod.as("criteriaMethod"),
                                equipmentCheckDetail.usl.as("usl"),
                                equipmentCheckDetail.lsl.as("lsl"),
                                equipmentCheckDetail.result.as("result"),
                                equipmentCheckDetail.registerType.as("registerType")
                        )
                )
                .from(equipmentCheckDetail)
                .leftJoin(equipment).on(equipment.id.eq(equipmentCheckDetail.equipment.id))
                .leftJoin(equipmentMaintenance).on(equipmentMaintenance.id.eq(equipmentCheckDetail.equipmentMaintenance.id))
                .where(
                        isEquipmentIdEq(equipmentId),
                        isEquipmentCheckDetailDeleteYnFalse()
                )
                .fetch();
    }

    // 상세정보 단일 조회
    @Override
    public Optional<EquipmentCheckDetailResponse> findEquipmentCheckDetailResponseByEquipmentIdAndEquipmentDetailId(Long equipmentId, Long equipmentCheckDetailId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        EquipmentCheckDetailResponse.class,
                                        equipmentCheckDetail.id.as("id"),
                                        equipmentMaintenance.id.as("maintenanceId"),
                                        equipmentMaintenance.maintenanceName.as("maintenanceName"),
                                        equipmentCheckDetail.checkContent.as("checkContent"),
                                        equipmentCheckDetail.criteriaStandard.as("criteriaStandard"),
                                        equipmentCheckDetail.criteriaMethod.as("criteriaMethod"),
                                        equipmentCheckDetail.usl.as("usl"),
                                        equipmentCheckDetail.lsl.as("lsl"),
                                        equipmentCheckDetail.result.as("result"),
                                        equipmentCheckDetail.registerType.as("registerType")
                                )
                        )
                        .from(equipmentCheckDetail)
                        .leftJoin(equipment).on(equipment.id.eq(equipmentCheckDetail.equipment.id))
                        .leftJoin(equipmentMaintenance).on(equipmentMaintenance.id.eq(equipmentCheckDetail.equipmentMaintenance.id))
                        .where(
                                isEquipmentIdEq(equipmentId),
                                isEquipmentCheckDetailIdEq(equipmentCheckDetailId),
                                isEquipmentCheckDetailDeleteYnFalse()
                        )
                        .fetchOne()
        );
    }

    // 검색조건:
    // 설비유형(작업라인 id)
    private BooleanExpression isEquipmentTypeContain(Long workLineId) {
        return workLineId != null ? equipment.workLine.id.eq(workLineId) : null;
    }
    // 작업기간(디테일 정보 생성날짜 기준) fromDate~toDate
    private BooleanExpression isWorkDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? equipmentCheckDetail.createdDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)) : null;
    }
    // equipmentId
    private BooleanExpression isEquipmentIdEq(Long equipmentId) {
        return equipment.id.eq(equipmentId);
    }
    // equipmentCheckDetailId
    private BooleanExpression isEquipmentCheckDetailIdEq(Long equipmentCheckDetailId) {
        return equipmentCheckDetail.id.eq(equipmentCheckDetailId);
    }
    // equipmentDeleteYnFalse
    private BooleanExpression isEquipmentDeleteYnFalse() {
        return equipment.deleteYn.isFalse();
    }
    // equipmentCheckDetailDeleteYnFalse
    private BooleanExpression isEquipmentCheckDetailDeleteYnFalse() {
        return equipmentCheckDetail.deleteYn.isFalse();
    }
    // TODO(점검유형 보류)
}
