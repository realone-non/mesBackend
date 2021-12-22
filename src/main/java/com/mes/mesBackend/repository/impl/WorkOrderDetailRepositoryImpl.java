package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ProductionPlanResponse;
import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.custom.WorkOrderDetailRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WorkOrderDetailRepositoryImpl implements WorkOrderDetailRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QContractItem contractItem = QContractItem.contractItem;
    final QContract contract = QContract.contract;
    final QItem item = QItem.item;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QClient client = QClient.client;
    final QWorkLine workLine = QWorkLine.workLine;


    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 작업공정 id, 착수예정일 fromDate~endDate, 지시상태
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderProduceOrderResponse> findAllByCondition(
            Long itemGroupId,
            String itemNoAndName,
            String contractNo,
            String produceOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            OrderState orderState
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(WorkOrderProduceOrderResponse.class,
                                produceOrder.id.as("id"),
                                produceOrder.produceOrderNo.as("produceOrderNo"),
                                produceOrder.contractItem.item.itemNo.as("itemNo"),
                                produceOrder.contractItem.item.itemName.as("itemName"),
                                produceOrder.expectedStartedDate.as("expectedStartedDate"),
                                produceOrder.contractItem.amount.as("orderAmount"),
                                produceOrder.contractItem.item.unit.unitCodeName.as("unitCodeName"),
                                produceOrder.orderState.as("orderState"),
                                produceOrder.contractItem.contractType.as("contractType"),
                                produceOrder.contract.client.clientName.as("contractClient"),
                                produceOrder.contract.contractNo.as("contractNo"),
                                produceOrder.contract.periodDate.as("periodDate"),
                                produceOrder.contract.note.as("note")
                                )
                )
                .from(produceOrder)
                .where(
                        isItemGroupEq(itemGroupId),
                        isItemNoAndItemNameContain(itemNoAndName),
                        isContractNoContain(contractNo),
                        isProduceOrderNoContain(produceOrderNo),
                        isExpectedCompletedDateBetween(fromDate, toDate),
                        isInstructionStatusEq(orderState),
                        produceOrder.deleteYn.isFalse()
                )
                .fetch();
    }

    // 생산계획 수립 조회
    // 생산계획 수립 전체 조회, 검색조건: 작업라인, 작업예정일
    @Override
    public List<ProductionPlanResponse> findAllProductionPlanByCondition(Long workLineId, LocalDate fromDate, LocalDate toDate) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ProductionPlanResponse.class,
                                workOrderDetail.id.as("id"),
                                workOrderDetail.orderNo.as("orderNo"),
                                workOrderDetail.orders.as("orders"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemAccount.account.as("itemAccount"),
                                workLine.workLineName.as("workLine"),
                                workOrderDetail.expectedWorkDate.as("expectedWorkDate"),
                                workOrderDetail.expectedWorkTime.as("expectedWorkTime"),
                                workOrderDetail.readyTime.as("readyTime"),
                                workOrderDetail.uph.as("uph"),
                                workOrderDetail.costTime.as("costTime"),
                                workOrderDetail.orderAmount.as("orderAmount"),
                                contract.contractNo.as("contractNo"),
                                contractItem.periodDate.as("periodDate"),
                                client.clientName.as("cName"),
                                produceOrder.orderState.as("orderState"),
                                workOrderDetail.productionAmount.as("productionAmount")
                                )
                )
                .from(workOrderDetail)
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .leftJoin(client).on(client.id.eq(contract.client.id))
                .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                .where(
                        isWorkLineIdEq(workLineId),
                        isExpectedWorkDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 생산계획 수립 단일 조회
    @Override
    public Optional<ProductionPlanResponse> findProductionPlanByIdAndDeleteYnFalse(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .select(
                        Projections.fields(
                                ProductionPlanResponse.class,
                                workOrderDetail.id.as("id"),
                                workOrderDetail.orderNo.as("orderNo"),
                                workOrderDetail.orders.as("orders"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemAccount.account.as("itemAccount"),
                                workLine.workLineName.as("workLine"),
                                workOrderDetail.expectedWorkDate.as("expectedWorkDate"),
                                workOrderDetail.expectedWorkTime.as("expectedWorkTime"),
                                workOrderDetail.readyTime.as("readyTime"),
                                workOrderDetail.uph.as("uph"),
                                workOrderDetail.costTime.as("costTime"),
                                workOrderDetail.orderAmount.as("orderAmount"),
                                contract.contractNo.as("contractNo"),
                                contractItem.periodDate.as("periodDate"),
                                client.clientName.as("cName"),
                                produceOrder.orderState.as("orderState"),
                                workOrderDetail.productionAmount.as("productionAmount")
                        )
                )
                .from(workOrderDetail)
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .leftJoin(client).on(client.id.eq(contract.client.id))
                .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                .where(
                        workOrderDetail.id.eq(id),
                        isDeleteYnFalse()
                )
                .fetchOne());
    }

    // 품목그룹
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? produceOrder.contractItem.item.itemGroup.id.eq(itemGroupId) : null;
    }

    // 품명|품명
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? produceOrder.contractItem.item.itemNo.contains(itemNoAndName)
                .or(produceOrder.contractItem.item.itemName.contains(itemNoAndName)) : null;
    }

    // 수주번호
    private BooleanExpression isContractNoContain(String contractNo) {
        return contractNo != null ? produceOrder.contract.contractNo.contains(contractNo) : null;
    }

    // 제조오더번호
    private BooleanExpression isProduceOrderNoContain(String produceOrderNo) {
        return produceOrderNo != null ? produceOrder.produceOrderNo.contains(produceOrderNo) : null;
    }

    // 착수예정일 fromDate~toDate
    private BooleanExpression isExpectedCompletedDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? produceOrder.expectedStartedDate.between(fromDate, toDate) :  null;
    }
    // 지시상태
    private BooleanExpression isInstructionStatusEq(OrderState orderState) {
        return orderState != null ? produceOrder.orderState.eq(orderState) : null;
    }

    // 작업라인
    private BooleanExpression isWorkLineIdEq(Long workLineId) {
        return workLineId != null ? workLine.id.eq(workLineId) :  null;
    }

    // 작업예정일
    private BooleanExpression isExpectedWorkDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? workOrderDetail.expectedWorkDate.between(fromDate, toDate) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return workOrderDetail.deleteYn.isFalse();
    }
}
