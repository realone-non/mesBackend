package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopWorkOrderStates;
import com.mes.mesBackend.entity.*;
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
    final QWorkOrderUserLog workOrderUserLog = QWorkOrderUserLog.workOrderUserLog;
    final QUser user = QUser.user;

    // 오늘날짜, 같은 설비 기준으로 equipmentLot 조회
    @Override
    public Optional<LotEquipmentConnect> findByTodayAndEquipmentId(Long equipmentId, LocalDate now) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotEquipmentConnect)
                        .leftJoin(lotMaster).on(lotMaster.id.eq(lotEquipmentConnect.childLot.id))
                        .leftJoin(equipment).on(equipment.id.eq(lotMaster.equipment.id))
                        .where(
                                equipment.id.eq(equipmentId),
                                lotMaster.createdDate.between(now.atStartOfDay(), LocalDateTime.of(now, LocalTime.MAX).withNano(0))
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
                                lotEquipmentConnect.modifiedDate.as("updateDateTime")
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
}
