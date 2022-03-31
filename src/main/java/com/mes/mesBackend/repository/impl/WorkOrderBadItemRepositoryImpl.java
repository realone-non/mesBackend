package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.WorkOrderBadItemRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.EQUIPMENT_LOT;

@RequiredArgsConstructor
public class WorkOrderBadItemRepositoryImpl implements WorkOrderBadItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QWorkOrderBadItem workOrderBadItem = QWorkOrderBadItem.workOrderBadItem;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QBadItem badItem = QBadItem.badItem;
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;

    // 해당하는 lot 의 badItem 모두
    @Override
    public List<Long> findBadItemIdByLotMasterId(Long lotMasterId) {
        return jpaQueryFactory
                .select(badItem.id)
                .from(workOrderBadItem)
                .innerJoin(badItem).on(badItem.id.eq(workOrderBadItem.badItem.id))
                .leftJoin(lotMaster).on(lotMaster.id.eq(workOrderBadItem.lotMaster.id))
                .where(
                        workOrderBadItem.lotMaster.id.eq(lotMasterId),
                        workOrderBadItem.division.eq(EQUIPMENT_LOT),
                        workOrderBadItem.deleteYn.isFalse()
                )
                .fetch();
    }

    // 작업공정에 해당하는 badItemId
    @Override
    public List<Long> findBadItemIdByWorkOrderId(Long workProcessId) {
        return jpaQueryFactory
                .select(badItem.id)
                .from(badItem)
                .innerJoin(workProcess).on(workProcess.id.eq(badItem.workProcess.id))
                .where(badItem.workProcess.id.eq(workProcessId))
                .fetch();

    }

    // pop 작업공정에 해당하는 badItemType 모두
    @Override
    public List<PopBadItemTypeResponse> findPopBadItemTypeByWorkProcessId(Long workProcessId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopBadItemTypeResponse.class,
                                badItem.id.as("badItemTypeId"),
                                badItem.badItemName.as("badItemTypeName")
                        )
                )
                .from(badItem)
                .where(
                        badItem.workProcess.id.eq(workProcessId),
                        badItem.deleteYn.isFalse()
                )
                .fetch();
    }

    // lot 에 해당하는거 모두 조회
    @Override
    public List<PopTestBadItemResponse> findPopTestBadItemResponseByLotMasterId(Long lotMasterId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopTestBadItemResponse.class,
                                workOrderBadItem.id.as("enrollmentBadItemId"),
                                workOrderBadItem.badItem.id.as("badItemTypeId"),
                                workOrderBadItem.badItem.badItemName.as("badItemTypeName"),
                                workOrderBadItem.badItemAmount.as("badItemAmount")
                        )
                )
                .from(workOrderBadItem)
                .where(
                        workOrderBadItem.lotMaster.id.eq(lotMasterId),
                        workOrderBadItem.deleteYn.isFalse(),
                        workOrderBadItem.division.eq(EQUIPMENT_LOT)
                )
                .fetch();
    }

    // 불량항목 전체 조회
    @Override
    public List<BadItem> findBadItemByCondition(Long workProcessId) {
        return jpaQueryFactory
                .selectFrom(badItem)
                .leftJoin(workProcess).on(workProcess.id.eq(badItem.workProcess.id))
                .where(
                        isWorkProcessIdEq(workProcessId),
                        isBadItemDeleteYnFalse()
                )
                .orderBy(workProcess.orders.asc(), badItem.orders.asc())      // 공정 순번 별 정렬 , 불량 순번 별 정렬
                .fetch();
    }

    // equipmentLot 에 해당하는 불량수량 모두
    @Override
    public List<Integer> findBadItemAmountByEquipmentLotMaster(Long equipmentLotId) {
        return jpaQueryFactory
                .select(workOrderBadItem.badItemAmount)
                .from(workOrderBadItem)
                .where(
                        workOrderBadItem.lotMaster.id.eq(equipmentLotId),
                        workOrderBadItem.deleteYn.isFalse()
                )
                .fetch();
    }

    // dummyLot 에 해당되는 불량수량 모두
    @Override
    public List<Integer> findBadItemAmountByDummyLotMaster(Long dummyLotId) {
        return jpaQueryFactory
                .select(workOrderBadItem.badItemAmount)
                .from(workOrderBadItem)
                .leftJoin(lotMaster).on(lotMaster.id.eq(workOrderBadItem.lotMaster.id))
                .leftJoin(lotEquipmentConnect).on(lotEquipmentConnect.childLot.id.eq(lotMaster.id))
                .where(
                        lotEquipmentConnect.parentLot.id.eq(dummyLotId),
                        workOrderBadItem.deleteYn.isFalse()
                )
                .fetch();
    }

    // 공정에 해당하는 첫번째 불량유형
    @Override
    public Optional<BadItem> findByWorkOrderIdLimitOne(Long workProcessId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(badItem)
                        .where(
                                badItem.workProcess.id.eq(workProcessId),
                                badItem.deleteYn.isFalse()
                        )
                        .fetchFirst()
        );
    }

    // dummyLot 에 해당하는 불량유형 별 불량수량
    @Override
    public List<WorkOrderBadItemStatusDetailResponse> findByDummyLotIdGroupByBadItemType(Long dummyLotId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                WorkOrderBadItemStatusDetailResponse.class,
                                badItem.id.as("badItemId"),
                                badItem.badItemName.as("badItemName"),
                                workOrderBadItem.badItemAmount.sum().as("badItemAmount")
                        )
                )
                .from(workOrderBadItem)
                .leftJoin(lotEquipmentConnect).on(lotEquipmentConnect.childLot.id.eq(workOrderBadItem.lotMaster.id))
                .leftJoin(badItem).on(badItem.id.eq(workOrderBadItem.badItem.id))
                .groupBy(lotEquipmentConnect.parentLot.id)
                .groupBy(workOrderBadItem.badItem.id)
                .where(
                        lotEquipmentConnect.parentLot.id.eq(dummyLotId),
                        workOrderBadItem.deleteYn.isFalse()
                )
                .fetch();
    }

    @Override
    public List<WorkOrderBadItem> findByWorkOrderDetailIdAndBadItemId(Long workOrderDetailId, Long badItemId) {
        return jpaQueryFactory
                .selectFrom(workOrderBadItem)
                .where(
                        workOrderBadItem.workOrderDetail.id.eq(workOrderDetailId),
                        workOrderBadItem.badItem.id.eq(badItemId),
                        workOrderBadItem.deleteYn.isFalse()
                )
                .fetch();
    }

    // 설비 lot 에 해당하는 등록된 불량 전체 조회
    @Override
    public List<WorkOrderDetailBadItemResponse> findWorkOrderDetailBadItemResponseByEquipmentLotId(Long equipmentId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                WorkOrderDetailBadItemResponse.class,
                                workOrderBadItem.id.as("enrollmentBadItemId"),
                                workOrderBadItem.badItem.id.as("badItemTypeId"),
                                workOrderBadItem.badItem.badItemName.as("badItemTypeName"),
                                workOrderBadItem.badItemAmount.as("badItemAmount")
                        )
                )
                .from(workOrderBadItem)
                .where(
                        workOrderBadItem.lotMaster.id.eq(equipmentId),
                        workOrderBadItem.deleteYn.isFalse(),
                        workOrderBadItem.division.eq(EQUIPMENT_LOT)
                )
                .fetch();
    }

    // 불량유형이 불량등록 정보에 존재하는지
    @Override
    public boolean existByBadItemAndDeleteYnFalse(Long badItemId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(workOrderBadItem)
                .where(
                        workOrderBadItem.badItem.id.eq(badItemId),
                        workOrderBadItem.deleteYn.isFalse()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression isWorkProcessIdEq(Long workProcessId) {
        return workProcessId != null ? workProcess.id.eq(workProcessId) : null;
    }

    private BooleanExpression isBadItemDeleteYnFalse() {
        return badItem.deleteYn.isFalse();
    }
}
