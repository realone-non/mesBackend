package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopBomDetailLotMasterResponse;
import com.mes.mesBackend.dto.response.PopLotMasterResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.LotConnectRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.EXHAUST;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.FAMILY;

@RequiredArgsConstructor
public class LotConnectRepositoryImpl implements LotConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotConnect lotConnect = QLotConnect.lotConnect;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;
    final QUnit unit = QUnit.unit;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;

//    // 부모 lotMaster 랑 같고, 자식 lotMaster 의 item 이 파라미터 itemId 와 같고, 구분값이 EXHAUST 인것 조회
    @Override
    public List<LotConnect> findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(Long itemIdOfChildLotMaster, Long lotMasterId) {
        return jpaQueryFactory
                .select(lotConnect)
                .from(lotConnect)
                .where(
                        lotConnect.childLot.item.id.eq(itemIdOfChildLotMaster),
                        lotConnect.parentLot.childLot.id.eq(lotMasterId),
                        lotConnect.division.eq(EXHAUST)
                )
                .fetch();
    }
//    @Override
//    public List<LotConnect> findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(Long itemIdOfChildLotMaster) {
//        return null;
//    }

    //        return jpaQueryFactory
//                .select(lotConnect)
//                .from(lotConnect)
//                .leftJoin(lotMaster).on(lotMaster.id.eq(lotConnect.childLot.id))
//                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
//                .where(
//                        item.id.eq(itemIdOfChildLotMaster)
//                )
//                .fetch();
//    }
//
//    // 검색조건: 부모 lotMaster, 자식 lotMaster, 구분 값 EXHAUST(소진)
    @Override
    public Optional<LotConnect> findByParentLotIdAndChildLotIdAndDivisionExhaust(Long parentLotMasterId, Long childLotMasterId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotConnect)
                        .where(
                                lotConnect.parentLot.childLot.id.eq(parentLotMasterId),
                                lotConnect.childLot.id.eq(childLotMasterId),
                                lotConnect.division.eq(EXHAUST)
                        )
                        .fetchOne()
        );
    }

//    // 부모 lotMaster 와 같은 자식 lotMasterId 모두 조회
    @Override
    public List<Long> findChildLotIdByParentLotIdAndDivisionExhaust(Long parentLotId) {
        return jpaQueryFactory
                .select(lotConnect.childLot.id)
                .from(lotConnect)
                .where(
                        lotConnect.parentLot.childLot.id.eq(parentLotId),
                        lotConnect.division.eq(EXHAUST)
                )
                .fetch();
    }
//        return jpaQueryFactory
//                .select(lotConnect.childLot.id)
//                .from(lotConnect)
//                .where(
//                        lotConnect.parentLot.id.eq(parentLotId),
//                        lotConnect.division.eq(EXHAUST)
//                )
//                .fetch();
//    }

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
                        lotConnect.parentLot.childLot.id.eq(parentLotMasterId),
                        item.id.eq(parentItemId),
                        lotConnect.division.eq(EXHAUST)
                )
                .fetch();
    }

    // equipmentLot id 에 해당하는 분할 lot 조회
    @Override
    public List<PopLotMasterResponse> findPopLotMasterResponseByEquipmentLotId(Long equipmentLotId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopLotMasterResponse.class,
                                lotConnect.childLot.id.as("lotMasterId"),
                                lotConnect.childLot.lotNo.as("lotNo"),
                                lotConnect.childLot.stockAmount.as("stockAmount")
                        )
                )
                .from(lotConnect)
                .where(
                        lotConnect.parentLot.childLot.id.eq(equipmentLotId),
                        lotConnect.division.eq(FAMILY)
                )
                .fetch();
    }

    // realLot 에 해당하는 equipmentLotId 조회, 구분 값: FAMILY
    @Override
    public Optional<LotConnect> findByChildLotIdAndDivisionFamily(Long childLotId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotConnect)
                        .where(
                                lotConnect.childLot.id.eq(childLotId),
                                lotConnect.division.eq(FAMILY)
                        )
                        .fetchOne()
        );
    }
}
