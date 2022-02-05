package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.LotEquipmentConnect;
import com.mes.mesBackend.entity.QLotEquipmentConnect;
import com.mes.mesBackend.repository.custom.LotEquipmentConnectRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class LotEquipmentConnectRepositoryImpl implements LotEquipmentConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;

    // 오늘날짜, 같은 설비 기준으로 equipmentLot 조회
    @Override
    public Optional<LotEquipmentConnect> findByTodayAndEquipmentId(Long equipmentId, LocalDate now) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotEquipmentConnect)
                        .where(
                                lotEquipmentConnect.childLot.equipment.id.eq(equipmentId),
                                isTodayEq(now)
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isTodayEq(LocalDate now) {
        return lotEquipmentConnect.childLot.createdDate.eq(now.atStartOfDay());
    }
}
