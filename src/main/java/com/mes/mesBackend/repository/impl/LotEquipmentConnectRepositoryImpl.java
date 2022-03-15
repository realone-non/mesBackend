package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopWorkOrderStates;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.custom.LotEquipmentConnectRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LotEquipmentConnectRepositoryImpl implements LotEquipmentConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QEquipment equipment = QEquipment.equipment;
    final QLotLog lotLog = QLotLog.lotLog;
    final QWorkProcess workProcess = QWorkProcess.workProcess;

    // 오늘날짜, 같은 설비 기준으로 equipmentLot 조회
    @Override
    public Optional<LotEquipmentConnect> findByTodayAndEquipmentId(Long equipmentId, LocalDate now, Long dummyLotId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotEquipmentConnect)
                        .leftJoin(lotMaster).on(lotMaster.id.eq(lotEquipmentConnect.childLot.id))
                        .leftJoin(equipment).on(equipment.id.eq(lotMaster.equipment.id))
                        .where(
                                equipment.id.eq(equipmentId),
                                lotMaster.createdDate.between(now.atStartOfDay(), LocalDateTime.of(now, LocalTime.MAX).withNano(0)),
                                lotEquipmentConnect.parentLot.id.eq(dummyLotId)
                        )
                        .fetchOne()
        );
    }

    // 설비 lot id 로 조회
    @Override
    public Optional<LotEquipmentConnect> findByChildId(Long childLotId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotEquipmentConnect)
                        .leftJoin(lotMaster).on(lotMaster.id.eq(lotEquipmentConnect.childLot.id))
                        .where(lotMaster.id.eq(childLotId))
                        .limit(1)
                        .fetchOne()
        );
    }

    // dummyLot 로 조회
    @Override
    public List<PopWorkOrderStates> findPopWorkOrderStates(Long dummyLotId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopWorkOrderStates.class,
                                lotEquipmentConnect.childLot.id.as("lotMasterId"),
                                equipment.equipmentName.as("equipmentName"),
                                lotEquipmentConnect.processStatus.as("processStatus"),
                                lotEquipmentConnect.modifiedDate.as("updateDateTime"),
                                lotEquipmentConnect.childLot.createdAmount.as("createdAmount")
                        )
                )
                .from(lotEquipmentConnect)
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotEquipmentConnect.childLot.id))
                .leftJoin(equipment).on(equipment.id.eq(lotMaster.equipment.id))
                .where(
                        lotEquipmentConnect.parentLot.id.eq(dummyLotId)
                )
                .fetch();
    }

    // 작업공정이 원료혼합이고, 오늘 생산된 lotMaster, 작업지시가 같음 찾아야함
    @Override
    public Optional<LotEquipmentConnect> findByTodayAndWorkProcessDivision(
            LocalDate now,
            WorkProcessDivision workProcessDivision,
            Long workOrderId
    ) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(lotEquipmentConnect)
                        .from(lotEquipmentConnect)
                        .leftJoin(lotMaster).on(lotMaster.id.eq(lotEquipmentConnect.childLot.id))
                        .leftJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                        .leftJoin(lotLog).on(lotLog.lotMaster.id.eq(lotEquipmentConnect.parentLot.id))
                        .where(
                                lotMaster.createdDate.between(now.atStartOfDay(), LocalDateTime.of(now, LocalTime.MAX).withNano(0)),
                                workProcess.workProcessDivision.eq(workProcessDivision),
                                lotMaster.deleteYn.isFalse(),
                                lotLog.workOrderDetail.id.eq(workOrderId),
                                lotLog.workOrderDetail.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }
}
