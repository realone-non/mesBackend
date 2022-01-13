package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.EquipmentBreakdownFileResponse;
import com.mes.mesBackend.dto.response.EquipmentBreakdownResponse;
import com.mes.mesBackend.dto.response.EquipmentRepairHistoryResponse;
import com.mes.mesBackend.dto.response.EquipmentRepairPartResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.EquipmentBreakdownRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

// 17-2. 설비 고장 수리내역 등록
@RequiredArgsConstructor
public class EquipmentBreakdownRepositoryImpl implements EquipmentBreakdownRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QEquipment equipment = QEquipment.equipment;
    final QEquipmentBreakdown equipmentBreakdown = QEquipmentBreakdown.equipmentBreakdown;
    final QWorkCenter workCenter = QWorkCenter.workCenter;
    final QEquipmentBreakdownFile equipmentBreakdownFile = QEquipmentBreakdownFile.equipmentBreakdownFile;
    final QWorkLine workLine = QWorkLine.workLine;
    final QRepairItem repairItem = QRepairItem.repairItem;
    final QRepairPart repairPart = QRepairPart.repairPart1;
    final QRepairCode repairCode = QRepairCode.repairCode1;

    // 설비고장 리스트 검색 조회, 검색조건: 작업장 id, 설비유형, 작업기간 fromDate~toDate
    @Override
    public List<EquipmentBreakdownResponse> findEquipmentBreakdownResponsesByCondition(Long workCenterId, Long workLineId, LocalDate fromDate, LocalDate toDate) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                EquipmentBreakdownResponse.class,
                                equipmentBreakdown.id.as("id"),
                                equipmentBreakdown.breakDownDate.as("breakdownDate"),
                                equipment.id.as("equipmentId"),
                                equipment.equipmentCode.as("equipmentCode"),
                                equipment.equipmentName.as("equipmentName"),
                                workLine.workLineName.as("equipmentType"),
                                equipmentBreakdown.reportDate.as("reportDate"),
                                equipmentBreakdown.requestBreakType.as("requestBreakType"),
                                equipmentBreakdown.breakNote.as("breakNote"),
                                equipmentBreakdown.breakReason.as("breakReason"),
                                equipmentBreakdown.causeOfFailure.as("causeOfFailure"),
                                equipmentBreakdown.arrivalDate.as("arrivalDate"),
                                equipmentBreakdown.repairStartDate.as("repairStartDate"),
                                equipmentBreakdown.repairEndDate.as("repairEndDate"),
                                equipmentBreakdown.note.as("note"),
                                workCenter.id.as("workCenterId"),
                                workCenter.workCenterName.as("workCenterName")
                        )
                )
                .from(equipmentBreakdown)
                .leftJoin(equipment).on(equipment.id.eq(equipmentBreakdown.equipment.id))
                .leftJoin(workCenter).on(workCenter.id.eq(equipmentBreakdown.workCenter.id))
                .leftJoin(workLine).on(workLine.id.eq(equipment.workLine.id))
                .where(
                        isWorkCenterEq(workCenterId),
                        isEquipmentTypeContain(workLineId),
                        isWorkDateBetween(fromDate, toDate),
                        isEquipmentBreakdownDeleteYnFalse()
                )
                .fetch();
    }

    // 설비고장 단일조회
    @Override
    public Optional<EquipmentBreakdownResponse> findEquipmentBreakdownResponseById(Long equipmentBreakdownId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        EquipmentBreakdownResponse.class,
                                        equipmentBreakdown.id.as("id"),
                                        equipmentBreakdown.breakDownDate.as("breakdownDate"),
                                        equipment.id.as("equipmentId"),
                                        equipment.equipmentCode.as("equipmentCode"),
                                        equipment.equipmentName.as("equipmentName"),
                                        workLine.workLineName.as("equipmentType"),
                                        equipmentBreakdown.reportDate.as("reportDate"),
                                        equipmentBreakdown.requestBreakType.as("requestBreakType"),
                                        equipmentBreakdown.breakNote.as("breakNote"),
                                        equipmentBreakdown.breakReason.as("breakReason"),
                                        equipmentBreakdown.causeOfFailure.as("causeOfFailure"),
                                        equipmentBreakdown.arrivalDate.as("arrivalDate"),
                                        equipmentBreakdown.repairStartDate.as("repairStartDate"),
                                        equipmentBreakdown.repairEndDate.as("repairEndDate"),
                                        equipmentBreakdown.note.as("note"),
                                        workCenter.id.as("workCenterId"),
                                        workCenter.workCenterName.as("workCenterName")
                                )
                        )
                        .from(equipmentBreakdown)
                        .leftJoin(equipment).on(equipment.id.eq(equipmentBreakdown.equipment.id))
                        .leftJoin(workCenter).on(workCenter.id.eq(equipmentBreakdown.workCenter.id))
                        .leftJoin(workLine).on(workLine.id.eq(equipment.workLine.id))
                        .where(
                                isEquipmentBreakdownIdEq(equipmentBreakdownId),
                                isEquipmentBreakdownDeleteYnFalse()
                        )
                        .fetchOne()
        );
    }
    // 설비고장 id 로 수리전 파일들 조회
    @Override
    public List<EquipmentBreakdownFileResponse> findBeforeFileResponsesByEquipmentBreakdownId(Long equipmentBreakdownId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                EquipmentBreakdownFileResponse.class,
                                equipmentBreakdownFile.id.as("id"),
                                equipmentBreakdownFile.fileUrl.as("fileUrl")
                        )
                )
                .from(equipmentBreakdownFile)
                .innerJoin(equipmentBreakdown).on(equipmentBreakdown.id.eq(equipmentBreakdownFile.equipmentBreakdown.id))
                .where(
                        equipmentBreakdownFile.beforeFileYn.isTrue(),
                        equipmentBreakdownFile.deleteYn.isFalse(),
                        isEquipmentBreakdownIdEq(equipmentBreakdownId)
                )
                .fetch();
    }

    // 설비고장 id 로 수리후 파일들 조회
    @Override
    public List<EquipmentBreakdownFileResponse> findAfterFileResponsesByEquipmentBreakdownId(Long equipmentBreakdownId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                EquipmentBreakdownFileResponse.class,
                                equipmentBreakdownFile.id.as("id"),
                                equipmentBreakdownFile.fileUrl.as("fileUrl")
                        )
                )
                .from(equipmentBreakdownFile)
                .innerJoin(equipmentBreakdown).on(equipmentBreakdown.id.eq(equipmentBreakdownFile.equipmentBreakdown.id))
                .where(
                        equipmentBreakdownFile.afterFileYn.isTrue(),
                        equipmentBreakdownFile.deleteYn.isFalse(),
                        isEquipmentBreakdownIdEq(equipmentBreakdownId)
                )
                .fetch();
    }
    // ============================================== 17-3. 설비 수리내역 조회 ==============================================
    // 설비 수리내역 리스트 조회, 검색조건: 작업장 id, 설비유형, 수리항목, 작업기간 fromDate~toDate
    @Override
    public List<EquipmentRepairHistoryResponse> findEquipmentRepairHistoryResponseByCondition(
            Long workCenterId,
            Long workLineId,
            Long repairCodeId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                EquipmentRepairHistoryResponse.class,
                                equipmentBreakdown.id.as("equipmentBreakdownId"),
                                equipmentBreakdown.breakDownDate.as("breakdownDate"),
                                equipment.equipmentCode.as("equipmentCode"),
                                equipment.equipmentName.as("equipmentName"),
                                workLine.workLineName.as("equipmentType"),
                                workCenter.workCenterName.as("workCenterName"),
                                equipmentBreakdown.repairStartDate.as("repairStartDate"),
                                equipmentBreakdown.repairEndDate.as("repairEndDate"),
                                repairItem.repairCode.repairCode.as("repairCode"),
                                repairItem.repairCode.repairContent.as("repairContent")
                        )
                )
                .from(repairItem)
                .innerJoin(equipmentBreakdown).on(equipmentBreakdown.id.eq(repairItem.equipmentBreakdown.id))
                .leftJoin(workCenter).on(workCenter.id.eq(equipmentBreakdown.workCenter.id))
                .leftJoin(equipment).on(equipment.id.eq(equipmentBreakdown.equipment.id))
                .leftJoin(workLine).on(workLine.id.eq(equipmentBreakdown.equipment.workLine.id))
                .leftJoin(repairCode).on(repairCode.id.eq(repairItem.repairCode.id))
                .where(
                        isWorkCenterEq(workCenterId),
                        isEquipmentTypeContain(workLineId),
                        isRepairItemEq(repairCodeId),
                        isWorkDateBetween(fromDate, toDate),
                        isEquipmentBreakdownDeleteYnFalse(),
                        isRepairItemDeleteYnFalse()
                )
                .fetch();
    }

    // ============================================== 17-4. 설비 수리부품 내역 조회 ==============================================
    // 설비 수리부품 내역 조회, 검색조건: 작업장 id, 설비유형(작업라인 id), 수리항목(수리코드 id), 작업기간 fromDate~toDate
    @Override
    public List<EquipmentRepairPartResponse> findEquipmentRepairPartResopnsesByCondition(
            Long workCenterId,
            Long workLineId,
            Long repairCodeId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                EquipmentRepairPartResponse.class,
                                equipmentBreakdown.id.as("equipmentBreakdownId"),
                                equipmentBreakdown.breakDownDate.as("breakdownDate"),
                                equipment.equipmentCode.as("equipmentCode"),
                                equipment.equipmentName.as("equipmentName"),
                                workLine.workLineName.as("equipmentType"),
                                workCenter.workCenterName.as("workCenterName"),
                                equipmentBreakdown.repairStartDate.as("repairStartDate"),
                                equipmentBreakdown.repairEndDate.as("repairEndDate"),
                                repairPart.repairPart.as("repairPart"),
                                repairPart.repairPartName.as("repairPartName"),
                                repairPart.amount.as("amount"),
                                repairPart.note.as("note")
                        )
                )
                .from(repairPart)
                .innerJoin(equipmentBreakdown).on(equipmentBreakdown.id.eq(repairPart.repairItem.equipmentBreakdown.id))
                .leftJoin(equipment).on(equipment.id.eq(equipmentBreakdown.equipment.id))
                .leftJoin(workLine).on(workLine.id.eq(equipmentBreakdown.equipment.workLine.id))
                .leftJoin(workCenter).on(workCenter.id.eq(equipmentBreakdown.workCenter.id))
                .leftJoin(repairItem).on(repairItem.id.eq(repairPart.repairItem.id))
                .leftJoin(repairCode).on(repairCode.id.eq(repairItem.repairCode.id))
                .where(
                        isWorkCenterEq(workCenterId),
                        isEquipmentTypeContain(workLineId),
                        isRepairItemEq(repairCodeId),
                        isWorkDateBetween(fromDate, toDate),
                        isEquipmentBreakdownDeleteYnFalse(),
                        isRepairPartDeleteYn()
                )
                .fetch();
    }
    private BooleanExpression isRepairPartDeleteYn() {
        return repairPart.deleteYn.isFalse();
    }

    // 수리코드 조회
    private BooleanExpression isRepairItemEq(Long repairItemId) {
        return repairItemId != null ? repairItem.repairCode.id.eq(repairItemId) : null;
    }

    // 작업장 id
    private BooleanExpression isWorkCenterEq(Long workCenterId) {
        return workCenterId != null ? workCenter.id.eq(workCenterId) : null;
    }

    // 설비유형
    private BooleanExpression isEquipmentTypeContain(Long equipmentType) {
        return equipmentType != null ? workLine.id.eq(equipmentType) : null;
    }
    // 작업기간 fromDate~toDate
    private BooleanExpression isWorkDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? equipmentBreakdown.repairStartDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0))
                .or(equipmentBreakdown.repairEndDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0))) : null;
    }
    // 설비고장 삭제여부
    private BooleanExpression isEquipmentBreakdownDeleteYnFalse() {
        return equipmentBreakdown.deleteYn.isFalse();
    }
    private BooleanExpression isEquipmentBreakdownIdEq(Long equipmentBreakdownId) {
        return equipmentBreakdown.id.eq(equipmentBreakdownId);
    }
    private BooleanExpression isRepairItemDeleteYnFalse() {
        return repairItem.deleteYn.isFalse();
    }
}
