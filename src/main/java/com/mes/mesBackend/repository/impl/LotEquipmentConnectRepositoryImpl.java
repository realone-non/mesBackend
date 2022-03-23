package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopWorkOrderStates;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.LotConnectDivision;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
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

import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.FAMILY;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.REAL_LOT;

@RequiredArgsConstructor
public class LotEquipmentConnectRepositoryImpl implements LotEquipmentConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QEquipment equipment = QEquipment.equipment;
    final QLotLog lotLog = QLotLog.lotLog;
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QLotConnect lotConnect = QLotConnect.lotConnect;

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

    // parentLotId 로 childLot(equipmentLot) 조회
    @Override
    public List<LotMaster> findChildLotByParentLotId(Long parentLotId) {
        return jpaQueryFactory
                .select(lotEquipmentConnect.childLot)
                .from(lotEquipmentConnect)
                .where(
                        lotEquipmentConnect.parentLot.id.eq(parentLotId)
                )
                .fetch();
    }

    // 해당 작업지시로 생성된 realLot 모두 조회
    @Override
    public List<LotMaster> findChildLotByChildLotOfParentLotCreatedDateDesc(Long dummyLotId) {
        return jpaQueryFactory
                .select(lotConnect.childLot)
                .from(lotConnect)
                .leftJoin(lotEquipmentConnect).on(lotEquipmentConnect.id.eq(lotConnect.parentLot.id))
                .where(
                        lotEquipmentConnect.parentLot.id.eq(dummyLotId),
                        lotConnect.childLot.deleteYn.isFalse(),
                        lotConnect.childLot.lotMasterDivision.eq(REAL_LOT),
                        lotConnect.division.eq(FAMILY)
                )
                .orderBy(lotConnect.childLot.createdDate.desc())
                .fetch();
    }


    // realLotId 로 equipmentLot 조회, 조회조건: 제일 마지막에 생성된 equipmentLot
    @Override
    public Optional<LotEquipmentConnect> findEquipmentLotByRealLotIdOrderByCreatedDateDesc(Long realLotId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(lotConnect.parentLot)
                        .from(lotConnect)
                        .where(
                                lotConnect.childLot.id.eq(realLotId),
                                lotConnect.childLot.deleteYn.isFalse(),
                                lotConnect.parentLot.childLot.deleteYn.isFalse()
                        )
                        .orderBy(lotConnect.parentLot.createdDate.desc())
                        .limit(1)
                        .fetchOne()
        );
    }
}
