package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.WorkOrderStateResponse;
import com.mes.mesBackend.dto.response.WorkOrderUserResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.custom.WorkOrderStateRepositoryCustom;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;

@RequiredArgsConstructor
public class WorkOrderStateRepositoryImpl implements WorkOrderStateRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QWorkOrderState workOrderState = QWorkOrderState.workOrderState;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;
    final QContractItem contractItem = QContractItem.contractItem;
    final QContract contract = QContract.contract;
    final QItem item = QItem.item;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QWorkLine workLine = QWorkLine.workLine;
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QUser user = QUser.user;

    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    @Transactional(readOnly = true)
    @Override
    public List<WorkOrderStateResponse> findWorkOrderStateByCondition(
            Long workProcessId,
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(WorkOrderStateResponse.class,
                                workOrderDetail.id.as("id"),
                                workOrderDetail.orderNo.as("orderNo"),
                                workProcess.workProcessName.as("workProcess"),
                                workLine.workLineName.as("workLine"),
                                produceOrder.orderState.as("orderState"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemAccount.account.as("itemAccount"),
                                contract.periodDate.as("periodDate"),
                                workOrderDetail.orderAmount.as("orderAmount"),
                                workOrderDetail.productionAmount.as("productionAmount"),
                                contract.contractNo.as("contractNo")
                                )
                )
                .from(workOrderDetail)
                .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(workOrderState).on(workOrderState.workOrderDetail.id.eq(workOrderDetail.id))
                .where(
                        isWorkProcessIdEq(workProcessId),
                        isWorkLineIdEq(workLineId),
                        isProduceOrderNoContain(produceOrderNo),
                        isItemAccountIdEq(itemAccountId),
                        isOrderStateEq(orderState),
                        isWorkOrderDateTimeBetween(fromDate, toDate),
                        isContractNoContain(contractNo),
                        isDeleteYnFalse()
                )
                .distinct()
                .fetch();
    }

    // 작업지시 정보 단일 조회
    @Transactional(readOnly = true)
    @Override
    public Optional<WorkOrderStateResponse> findWorkOrderStateByIdAndWorkOrderDetail(Long workOrderDetailId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(WorkOrderStateResponse.class,
                                        workOrderDetail.id.as("id"),
                                        workOrderDetail.orderNo.as("orderNo"),
                                        workProcess.workProcessName.as("workProcess"),
                                        workLine.workLineName.as("workLine"),
                                        produceOrder.orderState.as("orderState"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        itemAccount.account.as("itemAccount"),
                                        contract.periodDate.as("periodDate"),
                                        workOrderDetail.orderAmount.as("orderAmount"),
                                        workOrderDetail.productionAmount.as("productionAmount"),
                                        contract.contractNo.as("contractNo")
                                )
                        )
                        .from(workOrderDetail)
                        .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                        .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                        .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                        .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                        .leftJoin(item).on(item.id.eq(contractItem.item.id))
                        .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                        .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                        .leftJoin(workOrderState).on(workOrderState.id.eq(workOrderDetail.id))
                        .where(
                                workOrderDetail.id.eq(workOrderDetailId),
                                isDeleteYnFalse()
                        )
                        .fetchOne());
    }

    // 작업자 투입 리스트 검색 조회, 검색조건: 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    @Transactional(readOnly = true)
    @Override
    public List<WorkOrderUserResponse> findWorkOrderUserByCondition(
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                WorkOrderUserResponse.class,
                                workOrderDetail.id.as("id"),
                                user.userCode.as("userCode"),
                                user.korName.as("korName"),
                                workLine.workLineName.as("workLine"),
                                workOrderDetail.note.as("note"),
                                contract.contractNo.as("contractNo"),
                                produceOrder.produceOrderNo.as("produceOrderNo"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(workOrderState.workOrderDateTime)
                                                .from(workOrderState)
                                                .where(
                                                        workOrderState.workOrderDetail.id.eq(workOrderDetail.id),
                                                        workOrderState.orderState.eq(ONGOING)
                                                )
                                        , "startDateTime"
                                ),
                                ExpressionUtils.as(
                                        JPAExpressions.select(workOrderState.workOrderDateTime)
                                                .from(workOrderState)
                                                .where(
                                                        workOrderState.workOrderDetail.id.eq(workOrderDetail.id),
                                                        workOrderState.orderState.eq(COMPLETION)
                                                )
                                        , "endDateTime"
                                ),
                                ExpressionUtils.as(
                                        JPAExpressions.select(workOrderState.workOrderDateTime)
                                                .from(workOrderState)
                                                .where(
                                                        workOrderState.workOrderDetail.id.eq(workOrderDetail.id),
                                                        workOrderState.orderState.eq(SCHEDULE)
                                                )
                                        , "scheduleDateTime"
                                )
                        )

                )
                .from(workOrderDetail)
                .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .innerJoin(workOrderState).on(workOrderState.workOrderDetail.id.eq(workOrderDetail.id))
                .where(
                        isWorkLineIdEq(workLineId),
                        isProduceOrderNoContain(produceOrderNo),
                        isItemAccountIdEq(itemAccountId),
                        isOrderStateEq(orderState),
                        isContractNoContain(contractNo),
                        isDeleteYnFalse()
                )
                .groupBy(workOrderDetail.id)
                .fetch();
    }

    // 작업자 투입 단일 조회
    @Transactional(readOnly = true)
    @Override
    public WorkOrderUserResponse findWorkOrderUserByIdAndDeleteYn(Long workOrderId) {
        return null;
    }

    private BooleanExpression isStartDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 진행중 시간이 종료 시간보다 후에
        if (startDateTime != null && startDateTime.isAfter(endDateTime)) {
            return workOrderState.orderState.eq(COMPLETION);
        }
        return null;
//        if (startDateTime.isAfter(endDateTime)) {
//            return null;
//        }
//        return workOrderState.orderState.eq(COMPLETION);
    }


    // workOrderDetail 의 workOrderState에 값이 있는것만 조회
    private BooleanExpression isWorkOrderState(Long workOrderDetailId) {
        return workOrderState.workOrderDetail.id.eq(workOrderDetailId);
    }

    // 작업장
    private BooleanExpression isWorkProcessIdEq(Long workProcessId) {
        return workProcessId != null ? workProcess.id.eq(workProcessId) : null;
    }
    // 작업라인
    private BooleanExpression isWorkLineIdEq(Long workLineId) {
        return workLineId != null ? workLine.id.eq(workLineId) :  null;
    }
    // 제조오더번호
    private BooleanExpression isProduceOrderNoContain(String produceOrderNo) {
        return produceOrderNo != null ? produceOrder.produceOrderNo.contains(produceOrderNo) : null;
    }
    // 품목계정
    private BooleanExpression isItemAccountIdEq(Long itemAccountId) {
        return itemAccountId != null ? itemAccount.id.eq(itemAccountId) : null;
    }
    // 지시상태
    private BooleanExpression isOrderStateEq(OrderState orderState) {
        return orderState != null ? workOrderDetail.orderState.eq(orderState) : null;
    }
    // 작업기간
    private BooleanExpression isWorkOrderDateTimeBetween(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime localDateTimeFromDate = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime localDateTimeToDate = toDate != null ? toDate.atStartOfDay() : null;
        return fromDate != null ? workOrderState.workOrderDateTime.between(localDateTimeFromDate, localDateTimeToDate) : null;
    }
    // 수주번호
    private BooleanExpression isContractNoContain(String contractNo) {
        return contractNo != null ? contract.contractNo.contains(contractNo) : null;
    }
    private BooleanExpression isDeleteYnFalse() {
        return workOrderDetail.deleteYn.isFalse();
    }
}
