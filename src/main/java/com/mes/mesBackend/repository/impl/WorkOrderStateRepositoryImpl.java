package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.WorkOrderStateResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.custom.WorkOrderStateRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WorkOrderStateRepositoryImpl implements WorkOrderStateRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QWorkOrderState workOrderState = QWorkOrderState.workOrderState;
    QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;
    final QContractItem contractItem = QContractItem.contractItem;
    final QContract contract = QContract.contract;
    final QItem item = QItem.item;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QWorkLine workLine = QWorkLine.workLine;
    final QWorkProcess workProcess = QWorkProcess.workProcess;

    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
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
                                produceOrder.instructionStatus.as("instructionStatus"),
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
                .fetch();
    }

    // 작업지시 정보 단일 조회
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
                                        produceOrder.instructionStatus.as("instructionStatus"),
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
