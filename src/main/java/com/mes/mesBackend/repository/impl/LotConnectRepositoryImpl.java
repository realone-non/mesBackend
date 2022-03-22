package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.LotTrackingResponse;
import com.mes.mesBackend.dto.response.PopBomDetailLotMasterResponse;
import com.mes.mesBackend.dto.response.PopLotMasterResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.custom.LotConnectRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.GoodsType.HALF_PRODUCT;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.EXHAUST;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.FAMILY;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.REAL_LOT;

@RequiredArgsConstructor
public class LotConnectRepositoryImpl implements LotConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotConnect lotConnect = QLotConnect.lotConnect;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;
    final QUnit unit = QUnit.unit;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QLotLog lotLog = QLotLog.lotLog;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;

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

    // 제조오더에 해당되고 입력한 설비랑 같은 원료혼합 재고수량이 1 이상인 lot
    @Override
    public Optional<LotConnect> findByTodayProduceOrderAndEquipmentIdEqAndLotStockAmountOneLoe(Long produceOrderId, Long inputEquipmentId, LocalDate now) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(lotConnect)
                        .from(lotConnect)
                        .leftJoin(lotLog).on(lotLog.lotMaster.id.eq(lotConnect.parentLot.parentLot.id))
                        .where(
                                lotLog.workOrderDetail.produceOrder.id.eq(produceOrderId),  // 제조오더 같은거
                                lotConnect.childLot.inputEquipment.id.eq(inputEquipmentId), // 투입 될 충진 설비 같은거
                                lotConnect.childLot.deleteYn.isFalse(),     // 삭제 안된거
                                lotConnect.division.eq(FAMILY),             // 분할로트
                                lotConnect.childLot.lotMasterDivision.eq(REAL_LOT),         // 분할로트
                                lotConnect.errorYn.isFalse(),               // 에러난거 제외
                                lotConnect.childLot.exhaustYn.isFalse(),    // 폐기처리된거 제외
                                lotConnect.childLot.createdDate.between(now.atStartOfDay(), LocalDateTime.of(now, LocalTime.MAX).withNano(0)),   // 금일 생산된
                                lotConnect.childLot.stockAmount.goe(1)  // 재고수량 1 이상
                        )
                        .fetchOne()
        );
    }

    // 제조오더에 해당되고, 입력한 충진 설비 lot 가 고장이었는지
    @Override
    public boolean existsByProduceOrderLotConnectIsError(Long produceOrderId, Long fillingEquLotMasterId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(lotConnect)
                .leftJoin(lotLog).on(lotLog.lotMaster.id.eq(lotConnect.parentLot.parentLot.id))
                .where(
                        lotLog.workOrderDetail.produceOrder.id.eq(produceOrderId),  // 제조오더 같은거
                        lotConnect.parentLot.childLot.id.eq(fillingEquLotMasterId), // 설비 lot 번호
                        lotConnect.errorYn.isTrue() // 고장처리된거
                )
                .fetchFirst();
        return fetchOne != null;
    }

    // childLotId 로 parentLotMaster 조회, 조건: division? EXHAUST
    @Override
    public List<LotConnect> findByChildLotIdAndDivisionIsExhaust(Long childLotId) {
        return jpaQueryFactory
                .selectFrom(lotConnect)
                .where(
                        lotConnect.childLot.id.eq(childLotId),
                        lotConnect.division.eq(EXHAUST)
                )
                .fetch();
    }

    // parentLotId(lotEquipmentId) 로 조회, 조건: division? FAMILY
    @Override
    public List<LotTrackingResponse> findByParentLotAndDivisionIsFamily(Long parentLotId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                LotTrackingResponse.class,
                                lotConnect.childLot.id.as("mainLotMasterId"),
                                lotConnect.childLot.lotNo.as("mainLotNo"),
                                lotConnect.childLot.item.itemNo.as("mainItemNo"),
                                lotConnect.childLot.item.itemName.as("mainItemName"),
                                lotConnect.childLot.enrollmentType.as("mainEnrollmentType"),
                                lotConnect.childLot.createdDate.as("createdDate")
                        )
                )
                .from(lotConnect)
                .where(
                        lotConnect.parentLot.id.eq(parentLotId),
                        lotConnect.division.eq(FAMILY)
                )
                .fetch();
    }

    private BooleanExpression isInputEquipmentIdEq(Long inputEquipmentId) {
        return inputEquipmentId != null ? lotConnect.childLot.inputEquipment.id.eq(inputEquipmentId) : null;
    }


    private BooleanExpression isLotMasterIdEq(Long lotMasterId) {
        return lotMasterId != null ? lotMaster.id.eq(lotMasterId) : null;
    }
}
