package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopPurchaseRequestResponse;
import com.mes.mesBackend.dto.response.PurchaseRequestResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.custom.PurchaseRequestRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;
import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;

@RequiredArgsConstructor
public class PurchaseRequestRepositoryImpl implements PurchaseRequestRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;
    final QBomItemDetail bomItemDetail = QBomItemDetail.bomItemDetail;
    final QItem item = QItem.item;
    final QUnit unit = QUnit.unit;
    final QPurchaseRequest purchaseRequest = QPurchaseRequest.purchaseRequest;
    final QContractItem contractItem = QContractItem.contractItem;
    final QContract contract = QContract.contract;
    final QClient client = QClient.client;
    final QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
        /*
        * 구매요청 품목정보는 produceOrder 의 contractItem 의 item 을 찾아서
        * item 에 해당하는 bomMaster 의 데이터에 해당되는
        * bomMasterDetail 의 item 만 등록 할 수 있다.
        */
    @Override
    @Transactional(readOnly = true)
    public List<Long> findItemIdByContractItemId(Long itemId) {
        return jpaQueryFactory
                .select(bomItemDetail.item.id)
                .from(bomItemDetail)
                .where(
                        bomItemDetail.bomMaster.item.id.eq(itemId),
                        bomItemDetail.deleteYn.isFalse()
                )
                .fetch();
    }

    // 단일조회
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseRequestResponse> findByIdAndOrderStateSchedule(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .select(
                        Projections.fields(PurchaseRequestResponse.class,
                                purchaseRequest.id.as("id"),
                                produceOrder.id.as("produceOrderId"),
                                client.clientName.as("contractClient"),
                                produceOrder.produceOrderNo.as("produceOrderNo"),
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                unit.unitCodeName.as("itemUnitCodeName"),
                                purchaseRequest.requestDate.as("requestDate"),
                                purchaseRequest.requestAmount.as("requestAmount"),
                                purchaseRequest.orderAmount.as("orderAmount"),
                                purchaseRequest.purchasePeriodDate.as("purchasePeriodDate"),
                                item.testType.as("testType"),
                                item.manufacturer.clientName.as("itemManufacturerName"),
                                purchaseRequest.note.as("note"),
                                contractItem.item.itemNo.as("modelItemNo"),
                                contract.periodDate.as("periodDate"),
                                purchaseRequest.inputTestYn.as("inputTestYn"),
                                purchaseRequest.stockUnitRequestAmount.as("stockUnitRequestAmount"),
                                purchaseRequest.stockUnitOrderAmount.as("stockUnitOrderAmount")
                        )
                )
                .from(purchaseRequest)
                .leftJoin(produceOrder).on(produceOrder.id.eq(purchaseRequest.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(client).on(client.id.eq(contract.client.id))
                .leftJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .where(
                        purchaseRequest.id.eq(id),
                        isDeleteYnFalse()
                )
                .fetchOne());
    }

    // 구매요청 리스트 조회, 검색조건: 요청기간, 제조오더번호, 품목그룹, 품번|품명, 제조사 품번, 완료포함(check)
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseRequestResponse> findAllByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            String produceOrderNo,
            Long itemGroupId,
            String itemNoAndName,
            String manufacturerPartNo,
            Boolean orderCompletion,
            Boolean purchaseOrderYn,
            Long purchaseOrderClientId
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(PurchaseRequestResponse.class,
                                purchaseRequest.id.as("id"),
                                produceOrder.id.as("produceOrderId"),
                                client.clientName.as("contractClient"),
                                produceOrder.produceOrderNo.as("produceOrderNo"),
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                unit.unitCodeName.as("itemUnitCodeName"),
                                purchaseRequest.requestDate.as("requestDate"),
                                purchaseRequest.requestAmount.as("requestAmount"),
                                purchaseRequest.orderAmount.as("orderAmount"),
                                purchaseRequest.purchasePeriodDate.as("purchasePeriodDate"),
                                item.testType.as("testType"),
                                item.manufacturer.clientName.as("itemManufacturerName"),
                                purchaseRequest.note.as("note"),
                                contractItem.item.itemNo.as("modelItemNo"),
                                contract.periodDate.as("periodDate"),
                                purchaseRequest.inputTestYn.as("inputTestYn"),
                                purchaseRequest.stockUnitRequestAmount.as("stockUnitRequestAmount"),
                                purchaseRequest.stockUnitOrderAmount.as("stockUnitOrderAmount"),
                                contractItem.item.itemName.as("contractItemItemName")
                        )
                )
                .from(purchaseRequest)
                .leftJoin(produceOrder).on(produceOrder.id.eq(purchaseRequest.produceOrder.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(client).on(client.id.eq(contract.client.id))
                .leftJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .where(
                        isRequestDateBetween(fromDate, toDate),
                        isProduceOrderNoContain(produceOrderNo),
                        isItemGroupEq(itemGroupId),
                        isItemNoAndItemNameContain(itemNoAndName),
                        isManufacturerPartNoContain(manufacturerPartNo),
                        isOrderCompletionEq(orderCompletion),
                        isDeleteYnFalse(),
                        isPurchaseOrderYn(purchaseOrderYn),
                        isPurchaseOrderClientId(purchaseOrderClientId)
                )
                .orderBy(purchaseRequest.createdDate.desc())
                .fetch();
    }

    private BooleanExpression isPurchaseOrderYn(Boolean purchaseOrderYn) {
        if (purchaseOrderYn != null) {
            if (purchaseOrderYn) {
                return null;
            } else {
                return purchaseRequest.orderAmount.eq(0);
            }
        } else
            return null;
    }

    // 구매발주에서에서 상세 정보 등록할때 사용할 조건
    private BooleanExpression isPurchaseOrderClientId(Long purchaseOrderClientId) {
        return purchaseOrderClientId != null ? item.manufacturer.id.eq(purchaseOrderClientId) : null;
    }

    // 구매발주에 해당하는 구매요청이 있는지.
    // return: clientIds
    @Override
    @Transactional(readOnly = true)
    public List<Long> findClientIdsByPurchaseOrder(Long purchaseOrderId) {
        return jpaQueryFactory
                .select(purchaseRequest.item.manufacturer.id)
                .from(purchaseRequest)
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.produceOrder.id))
                .where(
                        purchaseRequest.purchaseOrder.id.eq(purchaseOrderId),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 구매발주에 해당하는 구매요청의 requestAmount 모두
//    @Override
//    public List<Integer> findOrderAmountByPurchaseOrderId(Long purchaseOrderId) {
//        return jpaQueryFactory
//                .select(purchaseRequest.requestAmount)
//                .from(purchaseRequest)
//                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
//                .where(
//                        purchaseOrder.id.eq(purchaseOrderId),
//                        purchaseRequest.deleteYn.isFalse()
//                )
//                .fetch();
//    }

    // 제조오더에 해당하는 구매요청의 requestAmount 모두
    @Override
    public List<Integer> findRequestAmountByProduceOrderId(Long produceOrderId) {
        return jpaQueryFactory
                .select(purchaseRequest.requestAmount)
                .from(purchaseRequest)
                .leftJoin(produceOrder).on(produceOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .where(
                        purchaseRequest.produceOrder.id.eq(produceOrderId),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // pop 해당 구매발주에 해당하는 구매요청정보 list 조회
    @Override
    public List<PopPurchaseRequestResponse> findPopPurchaseRequestResponseByPurchaseOrderId(Long purchaseOrderId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopPurchaseRequestResponse.class,
                                purchaseRequest.id.as("purchaseRequestId"),
                                purchaseRequest.item.id.as("itemId"),
                                purchaseRequest.item.itemNo.as("itemNo"),
                                purchaseRequest.item.itemName.as("itemName"),
                                purchaseRequest.orderAmount.as("purchaseRequestAmount")
                        )
                )
                .from(purchaseRequest)
                .where(
                        purchaseRequest.purchaseOrder.id.eq(purchaseOrderId),
                        purchaseRequest.deleteYn.isFalse()
//                        purchaseRequest.ordersState.notIn(COMPLETION)
                )
                .fetch();
    }

    //특정 날짜에 입고 예정인 품목 검색
    public Tuple findItemByItemAndDateForShortage(Long itemId, LocalDate fromDate){
        return jpaQueryFactory
                .select(purchaseRequest.item.id, purchaseRequest.orderAmount.sum())
                .from(purchaseRequest)
                .innerJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .where(
                        purchaseRequest.purchasePeriodDate.eq(fromDate),
                        purchaseRequest.item.id.eq(itemId),
                        purchaseRequest.deleteYn.eq(false)
                )
                .groupBy(purchaseRequest.item.id)
                .fetchOne();
    }

    // 구매발주에 해당하는 구매요청이 존재하는지?
    @Override
    public boolean existsPurchaseRequestByPurchaseOrder(Long purchaseOrderId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(purchaseRequest)
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .where(
                        purchaseRequest.purchaseOrder.id.eq(purchaseOrderId),
                        purchaseOrder.deleteYn.isFalse(),
                        purchaseRequest.deleteYn.isFalse()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    // 제조오더에 해당하는 구매요청의 상태값
    @Override
    public List<OrderState> findOrderStateByPurchaseOrder(Long purchaseOrderId) {
        return jpaQueryFactory
                .select(purchaseRequest.ordersState)
                .from(purchaseRequest)
                .where(
                        purchaseRequest.purchaseOrder.id.eq(purchaseOrderId),
                        purchaseRequest.deleteYn.isFalse()
                )
                .fetch();
    }

    // 요청기간
    private BooleanExpression isRequestDateBetween(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null) {
            return purchaseRequest.requestDate.between(fromDate, toDate);
        } else if (fromDate != null) {
            return purchaseRequest.requestDate.after(fromDate).or(purchaseRequest.requestDate.eq(fromDate));
        } else if (toDate != null) {
            return purchaseRequest.requestDate.before(toDate).or(purchaseRequest.requestDate.eq(toDate));
        } else {
            return null;
        }
    }
    // 제조오더번호
    private BooleanExpression isProduceOrderNoContain(String produceOrderNo) {
        return produceOrderNo != null ? produceOrder.produceOrderNo.contains(produceOrderNo) : null;
    }
    // 품목그룹
    // 원자재 품목에 대한 조건
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? item.itemGroup.id.eq(itemGroupId) : null;
    }
    // 품번|품명
    // 원자재 품목에 대한 조건
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName).or(item.itemName.contains(itemNoAndName)) : null;
    }
    // 제조사 품번
    // 원자재 품목에 대한 조건
    private BooleanExpression isManufacturerPartNoContain(String manufacturerPartNo) {
        return manufacturerPartNo != null ? item.manufacturerPartNo.contains(manufacturerPartNo) : null;
    }
    // 완료포함(check)
    private BooleanExpression isOrderCompletionEq(Boolean orderCompletion) {
        // 인자 값이 false 일 때 지시상태의 값이 Schedule 인 데이터 검색
        // true 일 땐 Completion
//        return orderCompletion ? purchaseRequest.ordersState.eq(COMPLETION) : purchaseRequest.ordersState.eq(SCHEDULE);
        return orderCompletion != null ? (orderCompletion ? purchaseRequest.ordersState.eq(COMPLETION) : purchaseRequest.ordersState.eq(SCHEDULE)) : null;
    }
    // 삭제여부 false
    private BooleanExpression isDeleteYnFalse() {
        return purchaseRequest.deleteYn.isFalse();
    }
}
