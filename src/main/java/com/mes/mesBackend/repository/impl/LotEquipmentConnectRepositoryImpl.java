package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.LotEquipmentConnect;
import com.mes.mesBackend.entity.QEquipment;
import com.mes.mesBackend.entity.QLotEquipmentConnect;
import com.mes.mesBackend.entity.QLotMaster;
import com.mes.mesBackend.repository.custom.LotEquipmentConnectRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@RequiredArgsConstructor
public class LotEquipmentConnectRepositoryImpl implements LotEquipmentConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QEquipment equipment = QEquipment.equipment;

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
}
