package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopBomDetailLotMasterResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.LotConnectRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.EXHAUST;

@RequiredArgsConstructor
public class LotConnectRepositoryImpl implements LotConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotConnect lotConnect = QLotConnect.lotConnect;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;
    final QUnit unit = QUnit.unit;

    // 부모 lotMaster 랑 같고, 자식 lotMaster 의 item 이 파라미터 itemId 와 같고, 구분값이 EXHAUST 인것 조회
    @Override
    public List<LotConnect> findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(Long itemIdOfChildLotMaster) {
        return jpaQueryFactory
                .select(lotConnect)
                .from(lotConnect)
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotConnect.childLot.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .where(
                        item.id.eq(itemIdOfChildLotMaster)
                )
                .fetch();
    }

    // 검색조건: 부모 lotMaster, 자식 lotMaster, 구분 값 EXHAUST
    @Override
    public Optional<LotConnect> findByParentLotIdAndChildLotIdAndDivisionExhaust(Long parentLotMasterId, Long childLotMasterId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotConnect)
                        .where(
                                lotConnect.parentLot.id.eq(parentLotMasterId),
                                lotConnect.childLot.id.eq(childLotMasterId),
                                lotConnect.division.eq(EXHAUST)
                        )
                        .fetchOne()
        );
    }

    // 부모 lotMaster 와 같은 자식 lotMasterId 모두 조회
    @Override
    public List<Long> findChildLotIdByParentLotIdAndDivisionExhaust(Long parentLotId) {
        return jpaQueryFactory
                .select(lotConnect.childLot.id)
                .from(lotConnect)
                .where(
                        lotConnect.parentLot.id.eq(parentLotId),
                        lotConnect.division.eq(EXHAUST)
                )
                .fetch();
    }

    // 부모 lotMaster 와 같은 자식 lotMaster 모두 조회
    @Override
    public List<PopBomDetailLotMasterResponse> findExhaustLotResponseByParentLotAndDivisionExhaust(
            Long parentLotMasterId,
            Long parentItemId
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopBomDetailLotMasterResponse.class,
                                lotMaster.id.as("lotMasterId"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.stockAmount.as("stockAmount"),
                                unit.unitCode.as("unitCodeName"),
                                unit.exhaustYn.as("exhaustYn"),
                                lotConnect.amount.as("exhaustAmount")
                        )
                )
                .from(lotConnect)
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotConnect.childLot.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .where(
                        lotConnect.parentLot.id.eq(parentLotMasterId),
                        item.id.eq(parentItemId),
                        lotConnect.division.eq(EXHAUST)
                )
                .fetch();
    }
}
