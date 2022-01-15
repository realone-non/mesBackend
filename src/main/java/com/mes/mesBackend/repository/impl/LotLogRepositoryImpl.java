package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.LotLogRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LotLogRepositoryImpl implements LotLogRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotLog lotLog = QLotLog.lotLog;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QLotMaster lotMaster = QLotMaster.lotMaster;

    // 작업지시 id 로 createdDate 가 제일 최근인 workProcess 를 가져옴
    @Override
    public Optional<LotLog> findWorkProcessNameByWorkOrderId(Long workOrderId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(lotLog)
                .where(
                        lotLog.workOrderDetail.id.eq(workOrderId)
                )
                .orderBy(lotLog.createdDate.desc())
                .fetchFirst()
        );
    }

    // 작업지시에 해당하는 모든 불량수량 가져옴
    @Override
    public List<Integer> findBadItemAmountByWorkOrderId(Long workOrderId) {
        return jpaQueryFactory
                .select(lotMaster.badItemAmount)
                .from(lotLog)
                .innerJoin(workOrderDetail).on(workOrderDetail.id.eq(lotLog.workOrderDetail.id))
                .innerJoin(lotMaster).on(lotMaster.id.eq(lotLog.lotMaster.id))
                .where(
                        workOrderDetail.id.eq(workOrderId)
                )
                .fetch();
    }

    // 작업지시에 해당하는 모든 생성수량 가져옴
    @Override
    public List<Integer> findCreatedAmountByWorkOrderId(Long workOrderId) {
        return jpaQueryFactory
                .select(lotMaster.createdAmount)
                .from(lotLog)
                .innerJoin(workOrderDetail).on(workOrderDetail.id.eq(lotLog.workOrderDetail.id))
                .innerJoin(lotMaster).on(lotMaster.id.eq(lotLog.lotMaster.id))
                .where(
                        workOrderDetail.id.eq(workOrderId)
                )
                .fetch();
    }

    // 작업지시에 해당하는 lotMaster Id 모두 가져옴
    @Override
    public List<Long> findLotMasterIdByWorkOrderId(Long workOrderId) {
        return jpaQueryFactory
                .select(lotMaster.id)
                .from(lotLog)
                .innerJoin(lotMaster).on(lotMaster.id.eq(lotLog.lotMaster.id))
                .innerJoin(workOrderDetail).on(workOrderDetail.id.eq(lotLog.workOrderDetail.id))
                .where(
                        workOrderDetail.id.eq(workOrderId)
                )
                .fetch();
    }
}
