package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.BadItemEnrollmentResponse;
import com.mes.mesBackend.dto.response.PopBadItemTypeResponse;
import com.mes.mesBackend.dto.response.PopTestBadItemResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.repository.custom.WorkOrderBadItemRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.EQUIPMENT_LOT;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.REAL_LOT;

@RequiredArgsConstructor
public class WorkOrderBadItemRepositoryImpl implements WorkOrderBadItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QWorkOrderBadItem workOrderBadItem = QWorkOrderBadItem.workOrderBadItem;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QBadItem badItem = QBadItem.badItem;
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QLotLog lotLog = QLotLog.lotLog;

    @Override
    public Optional<BadItemEnrollmentResponse> findWorkOrderEnrollmentResponseById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                            Projections.fields(
                                    BadItemEnrollmentResponse.class,
                                    workOrderBadItem.id.as("badItemId"),
                                    workProcess.workProcessName.as("workProcessName"),
                                    badItem.badItemName.as("badItemName"),
                                    workOrderBadItem.badItemAmount.as("badItemAmount"),
                                    lotMaster.lotNo.as("lotNo")
                            )
                        )
                        .from(workOrderBadItem)
//                        .leftJoin(lotLog).on(lotLog.id.eq(workOrderBadItem.lotLog.id))
                        .leftJoin(badItem).on(badItem.id.eq(workOrderBadItem.badItem.id))
                        .leftJoin(workProcess).on(workProcess.id.eq(badItem.workProcess.id))
                        .leftJoin(lotMaster).on(lotMaster.id.eq(lotLog.lotMaster.id))
                        .where(
                                workOrderBadItem.id.eq(id),
                                workOrderBadItem.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<BadItemEnrollmentResponse> findWorkOrderEnrollmentResponsesByWorkOrderId(Long workOrderId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                BadItemEnrollmentResponse.class,
                                workOrderBadItem.id.as("badItemId"),
                                workProcess.workProcessName.as("workProcessName"),
                                badItem.badItemName.as("badItemName"),
                                workOrderBadItem.badItemAmount.as("badItemAmount"),
                                lotMaster.lotNo.as("lotNo")
                        )
                )
                .from(workOrderBadItem)
//                .leftJoin(lotLog).on(lotLog.id.eq(workOrderBadItem.lotLog.id))
                .leftJoin(workOrderDetail).on(workOrderDetail.id.eq(lotLog.workOrderDetail.id))
                .leftJoin(badItem).on(badItem.id.eq(workOrderBadItem.badItem.id))
                .leftJoin(workProcess).on(workProcess.id.eq(badItem.workProcess.id))
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotLog.lotMaster.id))
                .where(
                        workOrderDetail.id.eq(workOrderId),
                        workOrderBadItem.deleteYn.isFalse()
                )
                .fetch();
    }
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
                                workOrderBadItem.id.as("badItemId"),
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
}
