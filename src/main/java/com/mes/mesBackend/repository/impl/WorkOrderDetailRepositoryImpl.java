package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.custom.WorkOrderDetailRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.*;

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
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QUser user = QUser.user;
    final QBomItemDetail qBomItemDetail = QBomItemDetail.bomItemDetail;
    final QBomMaster bomMaster = QBomMaster.bomMaster;
    final QItemGroup itemGroup = QItemGroup.itemGroup;


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
                                produceOrder.note.as("note")
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
                .orderBy(produceOrder.createdDate.desc())
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
//                                item.itemNo.as("itemNo"),
//                                item.itemName.as("itemName"),
//                                itemAccount.account.as("itemAccount"),
                                workLine.workLineName.as("workLine"),
                                workOrderDetail.expectedWorkDate.as("expectedWorkDate"),
                                workOrderDetail.expectedWorkTime.as("expectedWorkTime"),
                                workOrderDetail.readyTime.as("readyTime"),
                                workOrderDetail.uph.as("uph"),
                                workOrderDetail.orderAmount.as("orderAmount"),
                                contract.contractNo.as("contractNo"),
                                contractItem.contract.periodDate.as("periodDate"),
                                client.clientName.as("cName"),
//                                produceOrder.orderState.as("orderState"),
                                workOrderDetail.productionAmount.as("productionAmount"),
                                workOrderDetail.startDate.as("startDateTime"),
                                workOrderDetail.endDate.as("endDateTime"),
                                workLine.workLineName.as("workLineName"),
                                workOrderDetail.workProcess.workProcessDivision.as("workProcessDivision"),
                                item.id.as("itemId"),
                                workOrderDetail.workProcess.id.as("workProcessId"),
                                workOrderDetail.orderState.as("orderState")
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
                .orderBy(workOrderDetail.createdDate.desc(), workOrderDetail.workProcess.orders.asc())
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
                                workOrderDetail.orderAmount.as("orderAmount"),
                                contract.contractNo.as("contractNo"),
                                contractItem.contract.periodDate.as("periodDate"),
                                client.clientName.as("cName"),
                                produceOrder.orderState.as("orderState"),
                                workOrderDetail.productionAmount.as("productionAmount"),
                                workOrderDetail.startDate.as("startDateTime"),
                                workOrderDetail.endDate.as("endDateTime"),
                                workLine.workLineName.as("workLineName")
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

    // 제조오더에 해당하는 모든 작업지시 가져옴
    @Override
    public List<OrderState> findOrderStatesByProduceOrderId(Long produceOrderId) {
        return jpaQueryFactory
                .select(workOrderDetail.orderState)
                .from(workOrderDetail)
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .where(
                        workOrderDetail.produceOrder.id.eq(produceOrderId),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // ==================================== 6-2. 작업지시 등록 ====================================
    // 작업지시 리스트 조회
    @Override
    public List<WorkOrderResponse> findWorkOrderResponseByProduceOrderIdAndDeleteYnFalse(Long produceOrderId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                WorkOrderResponse.class,
                                workOrderDetail.id.as("id"),
                                workOrderDetail.orderNo.as("orderNo"),
                                workProcess.id.as("workProcessId"),
                                workProcess.workProcessName.as("workProcessName"),
                                workLine.id.as("workLineId"),
                                workLine.workLineName.as("workLineName"),
                                workOrderDetail.orderAmount.as("orderAmount"),
                                workOrderDetail.user.id.as("userId"),
                                user.korName.as("userKorName"),
                                workOrderDetail.readyTime.as("readyTime"),
                                workOrderDetail.uph.as("uph"),
                                workOrderDetail.expectedWorkDate.as("expectedWorkDate"),
                                workOrderDetail.expectedWorkTime.as("expectedWorkTime"),
                                workOrderDetail.orderState.as("orderState"),
                                workOrderDetail.lastProcessYn.as("lastProcessYn"),
                                workOrderDetail.productionAmount.as("productionAmount"),
                                workOrderDetail.inputUser.as("inputUser"),
                                workOrderDetail.note.as("note"),
                                workOrderDetail.startDate.as("startDateTime"),
                                workOrderDetail.endDate.as("endDateTime"),
                                item.id.as("produceOrderItemId"),
                                workProcess.workProcessDivision.as("workProcessDivision")
                        )
                )
                .from(workOrderDetail)
                .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .where(
                        isDeleteYnFalse(),
                        workOrderDetail.produceOrder.id.eq(produceOrderId)
                )
                .orderBy(workProcess.orders.asc())
                .fetch();
    }

    // 작업지시 단일 조회
    @Override
    public Optional<WorkOrderResponse> findWorkOrderResponseByProduceOrderIdAndWorkOrderId(
            Long produceOrderId,
            Long workOrderId
    ) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        WorkOrderResponse.class,
                                        workOrderDetail.id.as("id"),
                                        workOrderDetail.orderNo.as("orderNo"),
                                        workProcess.id.as("workProcessId"),
                                        workProcess.workProcessName.as("workProcessName"),
                                        workLine.id.as("workLineId"),
                                        workLine.workLineName.as("workLineName"),
                                        workOrderDetail.orderAmount.as("orderAmount"),
                                        workOrderDetail.user.id.as("userId"),
                                        user.korName.as("userKorName"),
                                        workOrderDetail.readyTime.as("readyTime"),
                                        workOrderDetail.uph.as("uph"),
                                        workOrderDetail.expectedWorkDate.as("expectedWorkDate"),
                                        workOrderDetail.expectedWorkTime.as("expectedWorkTime"),
                                        workOrderDetail.orderState.as("orderState"),
                                        workOrderDetail.lastProcessYn.as("lastProcessYn"),
                                        workOrderDetail.productionAmount.as("productionAmount"),
                                        workOrderDetail.inputUser.as("inputUser"),
                                        workOrderDetail.note.as("note"),
                                        workOrderDetail.startDate.as("startDateTime"),
                                        workOrderDetail.endDate.as("endDateTime"),
                                        item.id.as("produceOrderItemId"),
                                        workProcess.workProcessDivision.as("workProcessDivision")
                                )
                        )
                        .from(workOrderDetail)
                        .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                        .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                        .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                        .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                        .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                        .leftJoin(item).on(item.id.eq(contractItem.item.id))
                        .where(
                                workOrderDetail.produceOrder.id.eq(produceOrderId),
                                workOrderDetail.id.eq(workOrderId),
                                isDeleteYnFalse()
                        )
                        .fetchOne()
        );
    }

    // 해당 공정이 존재하는지 여부
    @Override
    public boolean existByWorkProcess(Long produceOrderId, Long workProcessId) {
        Integer fetchOne =  jpaQueryFactory
                .selectOne()
                .from(workOrderDetail)
                .where(
                        workOrderDetail.produceOrder.id.eq(produceOrderId),
                        workOrderDetail.workProcess.id.eq(workProcessId),
                        workOrderDetail.deleteYn.isFalse(),
                        workOrderDetail.workProcess.deleteYn.isFalse()
                )
                .fetchFirst();

        return fetchOne != null;
    }

    // =============================================== 8-1. 작지상태 확인 ===============================================
    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderStateResponse> findWorkOrderStateResponsesByCondition(
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
                                workOrderDetail.orderState.as("orderState"),
                                contract.periodDate.as("periodDate"),
                                workOrderDetail.orderAmount.as("orderAmount"),
                                workOrderDetail.productionAmount.as("productionAmount"),
                                contract.contractNo.as("contractNo"),
                                workProcess.workProcessDivision.as("workProcessDivision"),
                                workProcess.id.as("workProcessId"),
                                item.id.as("itemId")
                        )
                )
                .from(workOrderDetail)
                .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .where(
                        isWorkProcessIdEq(workProcessId),
                        isWorkLineIdEq(workLineId),
                        isProduceOrderNoContain(produceOrderNo),
//                        isItemAccountIdEq(itemAccountId),
                        isOrderStateEq(orderState),
                        isWorkDateBetween(fromDate, toDate),
                        isContractNoContain(contractNo),
                        isDeleteYnFalse()
                )
                .orderBy(workOrderDetail.createdDate.desc())
                .fetch();
    }

    // 작업지시 정보 단일 조회
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkOrderStateResponse> findWorkOrderStateResponseById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(WorkOrderStateResponse.class,
                                        workOrderDetail.id.as("id"),
                                        workOrderDetail.orderNo.as("orderNo"),
                                        workProcess.workProcessName.as("workProcess"),
                                        workLine.workLineName.as("workLine"),
                                        workOrderDetail.orderState.as("orderState"),
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
                        .where(
                                workOrderDetail.id.eq(id),
                                isDeleteYnFalse()
                        )
                        .fetchOne());
    }

    // 작업지시 상태 이력 정보 조회
    @Override
    public WorkOrderStateDetailResponse findWorkOrderStateDetailById(Long id) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                WorkOrderStateDetailResponse.class,
                                workOrderDetail.endDate.as("completionDateTime"),
                                workOrderDetail.startDate.as("ongoingDateTime"),
                                workOrderDetail.scheduleDate.as("scheduleTime")
                        )
                )
                .from(workOrderDetail)
                .where(
                        workOrderDetail.id.eq(id),
                        isDeleteYnFalse()
                )
                .fetchOne();
    }

    // =============================================== 8-2. 작업자 투입 수정 ===============================================
    // 작업자 투입 리스트 검색 조회, 검색조건: 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    @Override
    public List<WorkOrderUserResponse> findWorkOrderUserResponsesByCondition(
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
                                user.id.as("userId"),
                                user.userCode.as("userCode"),
                                user.korName.as("korName"),
                                workOrderDetail.startDate.as("startDateTime"),
                                workOrderDetail.endDate.as("endDateTime"),
                                workLine.workLineName.as("workLine"),
                                workOrderDetail.note.as("note"),
                                contract.contractNo.as("contractNo"),
                                produceOrder.produceOrderNo.as("produceOrderNo")
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
                .where(
                        isWorkLineIdEq(workLineId),
                        isProduceOrderNoContain(produceOrderNo),
                        isItemAccountIdEq(itemAccountId),
                        isContractNoContain(contractNo),
                        isWorkDateBetween(fromDate, toDate),
                        isOrderStateEq(orderState),
                        isDeleteYnFalse()
                )
                .orderBy(workOrderDetail.createdDate.desc())
                .fetch();
    }

    // 작업자 투입 단일 조회
    @Override
    public Optional<WorkOrderUserResponse> findWorkOrderUserResponseByIdAndDeleteYn(Long workOrderId) {
                return Optional.ofNullable(
                        jpaQueryFactory
                        .select(
                                Projections.fields(
                                        WorkOrderUserResponse.class,
                                        workOrderDetail.id.as("id"),
                                        user.id.as("userId"),
                                        user.userCode.as("userCode"),
                                        user.korName.as("korName"),
                                        workOrderDetail.startDate.as("startDateTime"),
                                        workOrderDetail.endDate.as("endDateTime"),
                                        workLine.workLineName.as("workLine"),
                                        workOrderDetail.note.as("note"),
                                        contract.contractNo.as("contractNo"),
                                        produceOrder.produceOrderNo.as("produceOrderNo")
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
                        .where(
                                workOrderDetail.id.eq(workOrderId),
                                isDeleteYnFalse()
                        )
                .fetchOne());
    }

    // 제조오더에 해당된 작업지시 정보의 지시수량 모두
    @Override
    public List<Integer> findOrderAmountsByProduceOrderId(Long produceOrderId) {
        return jpaQueryFactory
                .select(workOrderDetail.orderAmount)
                .from(workOrderDetail)
                .where(
                        workOrderDetail.produceOrder.id.eq(produceOrderId),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // =============================================== pop ===============================================
    // 작업지시 정보 리스트, 조건: 작업공정, 오늘
    @Override
    public List<PopWorkOrderResponse> findPopWorkOrderResponsesByCondition(
            Long workProcessId,
            LocalDate now
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopWorkOrderResponse.class,
                                workOrderDetail.id.as("workOrderId"),
                                workOrderDetail.orderNo.as("workOrderNo"),
                                workOrderDetail.orderAmount.as("orderAmount"),
                                workOrderDetail.productionAmount.as("productAmount"),
                                workOrderDetail.orderState.as("orderState"),
                                workOrderDetail.produceOrder.contractItem.item.id.as("produceOrderItemId")
                        )
                )
                .from(workOrderDetail)
                .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .where(
                        isWorkProcessId(workProcessId),
                        workOrderDetail.expectedWorkDate.eq(now),
                        workOrderDetail.deleteYn.isFalse()
                )
                .orderBy(workOrderDetail.createdDate.desc())
                .fetch();
    }

    @Override
    public Optional<Item> findPopWorkOrderItem(Long workProcessId, LocalDate fromDate, Long workOrderId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(item)
                .from(workOrderDetail)
                .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .where(
                        isWorkProcessIdEq(workProcessId),
                        workOrderDetail.expectedWorkDate.eq(fromDate),
                        workOrderDetail.deleteYn.isFalse(),
                        workOrderDetail.id.eq(workOrderId)
                )
                .fetchOne());
    }

    // 작업지시 상세 정보
    @Override
    public List<PopWorkOrderDetailResponse> findPopWorkOrderDetailResponsesByItemId(Long itemId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopWorkOrderDetailResponse.class,
                                bomMaster.id.as("bomDetailId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemAccount.account.as("itemAccount"),
                                qBomItemDetail.amount.as("bomAmount")
                        )
                )
                .from(qBomItemDetail)
                .leftJoin(bomMaster).on(bomMaster.id.eq(qBomItemDetail.bomMaster.id))
                .leftJoin(item).on(item.id.eq(qBomItemDetail.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .where(
                        bomMaster.item.id.eq(itemId),
                        bomMaster.deleteYn.isFalse(),
                        qBomItemDetail.deleteYn.isFalse()
                )
                .fetch();
    }

    // 수주수량
    @Override
    public Integer findContractItemAmountByWorkOrderId(Long workOrderId) {
        return jpaQueryFactory
                .select(workOrderDetail.produceOrder.contractItem.amount)
                .from(workOrderDetail)
                .where(
                        workOrderDetail.id.eq(workOrderId),
                        isDeleteYnFalse()
                )
                .fetchOne();
    }

    // 여기서 [javax.persistence.NonUniqueResultException: query did not return a unique result: ] 에러가 난다면
    // BomMaster 에 같은 item 이 2 개 이상 등록되어 있어서 나는 에러임
    @Override
    public Optional<Item> findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(Long itemId, Long workProcessId, GoodsType goodsType) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(item)
                        .from(qBomItemDetail)
                        .leftJoin(bomMaster).on(bomMaster.id.eq(qBomItemDetail.bomMaster.id))
                        .leftJoin(item).on(item.id.eq(qBomItemDetail.item.id))
                        .leftJoin(workProcess).on(workProcess.id.eq(qBomItemDetail.workProcess.id))
                        .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                        .where(
                                qBomItemDetail.bomMaster.item.id.eq(itemId),
                                workProcess.id.eq(workProcessId),
                                isGoodsTypeEq(goodsType),
                                qBomItemDetail.deleteYn.isFalse(),
                                bomMaster.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isGoodsTypeEq(GoodsType goodsType) {
        return goodsType != null ? itemAccount.goodsType.eq(goodsType) : null;
    }

    // bomMaster 의 item 에 해당하는 bomDetail 의 item 정보 가져옴
    @Override
    public List<Item> findBomDetailItemByBomMasterItem(Long bomMasterItemId, WorkProcessDivision workProcessDivision) {
        return jpaQueryFactory
                .select(item)
                .from(qBomItemDetail)
                .leftJoin(bomMaster).on(bomMaster.id.eq(qBomItemDetail.bomMaster.id))
                .leftJoin(workProcess).on(workProcess.id.eq(qBomItemDetail.workProcess.id))
                .leftJoin(item).on(item.id.eq(qBomItemDetail.item.id))
                .where(
                        bomMaster.item.id.eq(bomMasterItemId),
                        qBomItemDetail.deleteYn.isFalse(),
                        bomMaster.deleteYn.isFalse(),
                        isWorkProcessDivision(workProcessDivision)
//                        workProcess.workProcessDivision.eq(workProcessDivision) // 원료혼합
//                        workProcess.workProcessDivision.eq(FILLING),         // 충진
//                        workProcess.workProcessDivision.eq(CAP_ASSEMBLY),    // 캡조립
//                        workProcess.workProcessDivision.eq(LABELING)         // 라벨링

                )
                .fetch();
    }

    private BooleanExpression isWorkProcessDivision(WorkProcessDivision workProcessDivision) {
        return workProcessDivision != null ? workProcess.workProcessDivision.eq(workProcessDivision) : null;
    }

    // 품목에 해당하는 bomDetail 의 라벨링 공정의 bomDetail 의 Item, 포장공정의(원부자재) 의 item 가져옴
    @Override
    public List<Item> findBomDetailItemByBomMasterItemWorkProcessPackaging(Long bomMasterItemId) {
        return jpaQueryFactory
                .select(item)
                .from(qBomItemDetail)
                .leftJoin(bomMaster).on(bomMaster.id.eq(qBomItemDetail.bomMaster.id))
                .leftJoin(workProcess).on(workProcess.id.eq(qBomItemDetail.workProcess.id))
                .leftJoin(item).on(item.id.eq(qBomItemDetail.item.id))
                .where(
                        bomMaster.item.id.eq(bomMasterItemId),
                        bomMaster.deleteYn.isFalse(),
                        qBomItemDetail.deleteYn.isFalse(),
                        bomMaster.deleteYn.isFalse(),
                        workProcess.workProcessDivision.eq(LABELING).or(workProcess.workProcessDivision.eq(PACKAGING))           // 라벨링
                )
                .fetch();
    }

    // =============================================== 8-5. 불량등록 ===============================================
    // 작업지시 정보 리스트 조회,
    // 검색조건: 작업장 id, 작업라인 id, 품목그룹 id, 제조오더번호, JOB NO, 작업기간 fromDate~toDate, 품번|품목
    // COMPLETION 만 조회, 작업공정 구분이 자재입고, 출하는 제외
    @Override
    public List<BadItemWorkOrderResponse> findBadItemWorkOrderResponseByCondition(
            Long workCenterId,
            Long workLineId,
            Long itemGroupId,
            String produceOrderNo,
            String workOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndItemName
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                BadItemWorkOrderResponse.class,
                                workOrderDetail.id.as("workOrderId"),
                                workOrderDetail.orderNo.as("workOrderNo"),
                                workProcess.workProcessName.as("workProcessName"),
                                workLine.workLineName.as("workLineName"),
                                item.id.as("itemId"),
                                workOrderDetail.startDate.as("workDateTime"),
                                user.korName.as("userKorName"),
                                contract.contractNo.as("contractNo"),
                                workProcess.id.as("workProcessId")
                        )
                )
                .from(workOrderDetail)
                .leftJoin(workLine).on(workLine.id.eq(workOrderDetail.workLine.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(itemGroup).on(itemGroup.id.eq(item.itemGroup.id))
                .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                .where(
                        isWorkLineIdEq(workLineId),
                        isItemGroupEq(itemGroupId),
                        isProduceOrderNoContain(produceOrderNo),
                        isWorkOrderNoContain(workOrderNo),
                        isWorkOrderStartDateBetween(fromDate, toDate),
                        isDeleteYnFalse(),
                        workOrderDetail.orderState.ne(SCHEDULE),
                        workProcess.workProcessDivision.notIn(MATERIAL_INPUT),  // 공정 자제입고 제외
                        workProcess.workProcessDivision.notIn(SHIPMENT),         // 공정 출하 제외
                        workProcess.workProcessDivision.notIn(MATERIAL_MIXING)      // 원료혼합 공정 제외
                )
                .orderBy(workOrderDetail.startDate.desc(), workProcess.orders.asc())
                .fetch();
    }

    //Shortage용 날짜 기준 등록된 작업지시 가져오기
    public List<WorkOrderDetail> findByWorkDate(LocalDate stdDate){
        return jpaQueryFactory
                .selectFrom(workOrderDetail)
                .where(
                        workOrderDetail.expectedWorkDate.eq(stdDate),
                        workOrderDetail.orderState.ne(COMPLETION)
                )
                .fetch();
    }

    // 제조오더로 원료혼합 공정인 작업지시 조회
    @Override
    public Optional<WorkOrderDetail> findWorkOrderIsFillingByProduceOrderId(Long produceOrderId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(workOrderDetail)
                        .from(workOrderDetail)
                        .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                        .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                        .where(
                                produceOrder.id.eq(produceOrderId),                         // 제조오더 같은거
                                workOrderDetail.deleteYn.isFalse(),                         // 삭제된거
                                workProcess.workProcessDivision.eq(MATERIAL_MIXING) // 원료혼합 공정 조회
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<WorkOrderBadItemStatusResponse> findWorkOrderBadItemStatusResponseByCondition(
            Long workProcessId,
            String workOrderNo,
            String itemNoAndItemName,
            Long userId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                WorkOrderBadItemStatusResponse.class,
                                workOrderDetail.id.as("workOrderId"),
                                workOrderDetail.orderNo.as("workOrderNo"),
                                workProcess.id.as("workProcessId"),
                                workProcess.workProcessName.as("workProcessName"),
                                user.id.as("userId"),
                                user.korName.as("userKorName"),
                                workOrderDetail.productionAmount.as("productionAmount")
                        )
                )
                .from(workOrderDetail)
                .leftJoin(workProcess).on(workProcess.id.eq(workOrderDetail.workProcess.id))
                .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                .where(
                        isWorkProcessIdEq(workProcessId),
                        isWorkOrderNoContain(workOrderNo),
                        isUserId(userId),
                        isWorkOrderStartDateBetween(fromDate, toDate),
                        isDeleteYnFalse(),
                        workOrderDetail.orderState.ne(SCHEDULE),    // 진행중, 완료만 조회
                        workProcess.workProcessDivision.notIn(MATERIAL_INPUT),  // 공정 자제입고 제외
                        workProcess.workProcessDivision.notIn(SHIPMENT),         // 공정 출하 제외
                        workProcess.workProcessDivision.notIn(MATERIAL_MIXING)  // 공정 원료혼합 제외
                )
                .orderBy(workOrderDetail.startDate.desc(), workProcess.orders.asc())
                .fetch();
    }

    // 현재 진행중인 제조오더 개수
    @Override
    public Optional<Long> findProduceOrderStateOngoingProductionAmountSum() {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(produceOrder.id.count())
                        .from(produceOrder)
                        .where(
                                produceOrder.deleteYn.isFalse(),
                                produceOrder.orderState.eq(ONGOING)
                        )
                        .fetchOne()
        );
    }

    // 제조오더에 해당하는 공정 별 endDate 조회
    @Override
    public LocalDateTime findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(Long produceOrderId, WorkProcessDivision workProcessDivision) {
        return jpaQueryFactory
                .select(workOrderDetail.endDate)
                .from(workOrderDetail)
                .where(
                        workOrderDetail.produceOrder.id.eq(produceOrderId),
                        workOrderDetail.deleteYn.isFalse(),
                        workOrderDetail.workProcess.workProcessDivision.eq(workProcessDivision)
                )
                .fetchOne();
    }
    // 포장공정의 생산량
    @Override
    public Optional<Integer> findPackagingProductAmountByProduceOrderId(Long produceOrderId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(workOrderDetail.productionAmount)
                        .from(workOrderDetail)
                        .where(
                                workOrderDetail.produceOrder.id.eq(produceOrderId),
                                workOrderDetail.deleteYn.isFalse(),
                                workOrderDetail.workProcess.workProcessDivision.eq(PACKAGING)
                        )
                        .fetchOne()
        );
    }

    // 작업공정별 생산 정보
    @Override
    public Optional<Long> findOrderStateCountByWorkProcessDivisionAndOrderState(WorkProcessDivision workProcessDivision, OrderState orderState) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(workOrderDetail.id.count())
                        .from(workOrderDetail)
                        .where(
                                workOrderDetail.workProcess.workProcessDivision.eq(workProcessDivision),
                                workOrderDetail.orderState.eq(orderState),
                                workOrderDetail.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    // 작업공절별 생산수량
    @Override
    public Optional<Integer> findProductionAmountByWorkProcessDivision(WorkProcessDivision workProcessDivision) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(workOrderDetail.productionAmount.sum())
                        .from(workOrderDetail)
                        .where(
                                workOrderDetail.workProcess.workProcessDivision.eq(workProcessDivision),
                                workOrderDetail.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    // 작업지시 startDate 조회
    private BooleanExpression isWorkOrderStartDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? workOrderDetail.startDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)) : null;
    }

    // JOB NO
    private BooleanExpression isWorkOrderNoContain(String workOrderNo) {
        return workOrderNo != null ? workOrderDetail.orderNo.contains(workOrderNo) : null;
    }

    // 작업공정
    private BooleanExpression isWorkProcessId(Long workProcessId) {
        return workOrderDetail.workProcess.id.eq(workProcessId);
    }

    // 작업자
    private BooleanExpression isUserId(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    // 작업예정일 기준
    private BooleanExpression isWorkOrderDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? workOrderDetail.expectedWorkDate.between(fromDate, toDate) : null;
    }

    // 작업장
    private BooleanExpression isWorkProcessIdEq(Long workProcessId) {
        return workProcessId != null ? workProcess.id.eq(workProcessId) : null;
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
    private BooleanExpression isWorkDateBetween(LocalDate fromDate, LocalDate toDate) {
        // fromDate ~ toDate 사이에 진행중과 완료가 포함되어 있는거를 조회
        return fromDate != null ?
                (workOrderDetail.startDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)).and(workOrderDetail.endDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)))
                        .or(workOrderDetail.startDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)).or(workOrderDetail.endDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0))))) : null;
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
    // 삭제여부
    private BooleanExpression isDeleteYnFalse() {
        return workOrderDetail.deleteYn.isFalse();
    }
}
