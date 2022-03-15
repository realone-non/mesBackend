package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.custom.PurchaseOrderRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;

@RequiredArgsConstructor
public class PurchaseOrderRepositoryImpl implements PurchaseOrderRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QItem item = QItem.item;
    final QUnit unit = QUnit.unit;
    final QPurchaseRequest purchaseRequest = QPurchaseRequest.purchaseRequest;
    final QClient client = QClient.client;
    final QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
    final QUser user = QUser.user;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QCurrency currency = QCurrency.currency1;
    final QPayType payType = QPayType.payType1;


    // 구매발주 단일조회
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderResponse> findPurchaseOrderByIdAndDeleteYn(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        PurchaseOrderResponse.class,
                                        purchaseOrder.id.as("id"),
                                        purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                        purchaseOrder.purchaseOrderDate.as("purchaseOrderDate"),
                                        client.id.as("clientId"),
                                        client.clientCode.as("clientCode"),
                                        client.clientName.as("clientName"),
                                        user.id.as("userId"),
                                        user.korName.as("userName"),
                                        wareHouse.id.as("wareHouseId"),
                                        wareHouse.wareHouseName.as("wareHouseName"),
                                        currency.id.as("currencyId"),
                                        currency.currencyUnit.as("currencyUnit"),
                                        currency.exchangeRate.as("currencyExchangeRate"),
                                        purchaseOrder.surtaxYn.as("surtaxYn"),
                                        purchaseOrder.note.as("note"),
                                        purchaseOrder.transportCondition.as("transportCondition"),
                                        purchaseOrder.addendum.as("addendum"),
                                        purchaseOrder.transportType.as("transportType"),
                                        payType.id.as("payTypeId"),
                                        payType.payType.as("payType"),
                                        purchaseOrder.requestedShipping.as("requestedShipping"),
                                        purchaseOrder.specialNote.as("specialNote")
                                )
                        )
                        .from(purchaseOrder)
                        .leftJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseOrder.id))
                        .leftJoin(client).on(client.id.eq(purchaseOrder.client.id))
                        .leftJoin(user).on(user.id.eq(purchaseOrder.user.id))
                        .leftJoin(wareHouse).on(wareHouse.id.eq(purchaseOrder.wareHouse.id))
                        .leftJoin(currency).on(currency.id.eq(purchaseOrder.currency.id))
                        .leftJoin(payType).on(payType.id.eq(purchaseOrder.payType.id))
                        .where(
                                isDeleteYnFalse(),
                                purchaseOrder.id.eq(id)
                        )
                        .fetchOne()
        );
    }
    // 구매발주 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> findAllByCondition(
            Long currencyId,
            Long userId,
            Long clientId,
            Long wareHouseId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseOrderResponse.class,
                                purchaseOrder.id.as("id"),
                                purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                purchaseOrder.purchaseOrderDate.as("purchaseOrderDate"),
                                client.id.as("clientId"),
                                client.clientCode.as("clientCode"),
                                client.clientName.as("clientName"),
                                user.id.as("userId"),
                                user.korName.as("userName"),
                                wareHouse.id.as("wareHouseId"),
                                wareHouse.wareHouseName.as("wareHouseName"),
                                currency.id.as("currencyId"),
                                currency.currencyUnit.as("currencyUnit"),
                                currency.exchangeRate.as("currencyExchangeRate"),
                                purchaseOrder.surtaxYn.as("surtaxYn"),
                                purchaseOrder.note.as("note"),
                                purchaseOrder.transportCondition.as("transportCondition"),
                                purchaseOrder.addendum.as("addendum"),
                                purchaseOrder.transportType.as("transportType"),
                                payType.id.as("payTypeId"),
                                payType.payType.as("payType"),
                                purchaseOrder.requestedShipping.as("requestedShipping"),
                                purchaseOrder.specialNote.as("specialNote")
                        )
                )
                .from(purchaseOrder)
                .leftJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseOrder.id))
                .leftJoin(client).on(client.id.eq(purchaseOrder.client.id))
                .leftJoin(user).on(user.id.eq(purchaseOrder.user.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(purchaseOrder.wareHouse.id))
                .leftJoin(currency).on(currency.id.eq(purchaseOrder.currency.id))
                .leftJoin(payType).on(payType.id.eq(purchaseOrder.payType.id))
                .where(
                        isCurrencyEq(currencyId),
                        isUserEq(userId),
                        isClientEq(clientId),
                        isWareHouseEq(wareHouseId),
                        isPeriodDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .fetch();
    }


    // 구매발주에 해당하는 구매발주상세 정보의 모든 지시상태 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderState> findOrderStateByPurchaseOrderId(Long purchaseOrderId) {
        return jpaQueryFactory
                .select(purchaseRequest.ordersState)
                .from(purchaseRequest)
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.id))
                .where(
                        purchaseRequest.deleteYn.isFalse(),
                        purchaseRequest.purchaseOrder.id.eq(purchaseOrderId)
                )
                .fetch();
    }

    // 발주등록에 해당하는 상세정보의 최근 납기일자를 가져옴
    @Override
    @Transactional(readOnly = true)
    public LocalDate findPeriodDateByPurchaseOrderId(Long purchaseOrderId) {
        return jpaQueryFactory
                .select(purchaseRequest.periodDate)
                .from(purchaseRequest)
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.id))
                .where(
                        purchaseRequest.deleteYn.isFalse(),
                        purchaseRequest.purchaseOrder.id.eq(purchaseOrderId)
                )
                .orderBy(purchaseRequest.periodDate.desc())
                .limit(1)
                .fetchOne();
    }

    // 발주등록상세 단일 조회
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderDetailResponse> findPurchaseRequestByIdAndPurchaseOrderIdAndDeleteYnFalse(
            Long purchaseOrderId,
            Long purchaseRequestId
    ) {
        return Optional.ofNullable(jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseOrderDetailResponse.class,
                                purchaseRequest.id.as("id"),
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                unit.unitCodeName.as("orderUnitCodeName"),
                                purchaseRequest.requestAmount.as("orderAmount"),
                                item.inputUnitPrice.as("unitPrice"),
                                purchaseRequest.orderAmount.multiply(item.inputUnitPrice).as("orderPrice"),
                                purchaseRequest.orderAmount.multiply(item.inputUnitPrice).as("orderPriceWon"),
                                (purchaseRequest.orderAmount.multiply(item.inputUnitPrice).doubleValue()).multiply(0.1).as("vat"),
//                                purchaseRequest.orderPossibleAmount.as("orderPossibleAmount"),
//                                purchaseRequest.inputAmount.as("inputAmount"),
                                purchaseRequest.cancelAmount.as("cancelAmount"),    // 미구현
                                purchaseRequest.periodDate.as("orderPeriodDate"),
                                purchaseRequest.note.as("note"),
                                client.clientName.as("manufacturerName"),
                                purchaseRequest.ordersState.as("orderState"),
                                item.testType.as("testType")
                        )
                )
                .from(purchaseRequest)
                .innerJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .innerJoin(unit).on(unit.id.eq(item.unit.id))
                .innerJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .where(
                        purchaseRequest.id.eq(purchaseRequestId).and(purchaseRequest.purchaseOrder.id.eq(purchaseOrderId))
                )
                .fetchOne());
    }

    // 구매발주상세 리스트 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderDetailResponse> findAllByPurchaseOrderIdAndDeleteYnFalse(Long purchaseOrderId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseOrderDetailResponse.class,
                                purchaseRequest.id.as("id"),
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                unit.unitCodeName.as("orderUnitCodeName"),
                                purchaseRequest.requestAmount.as("orderAmount"),
                                item.inputUnitPrice.as("unitPrice"),
                                purchaseRequest.orderAmount.multiply(item.inputUnitPrice).as("orderPrice"),
                                purchaseRequest.orderAmount.multiply(item.inputUnitPrice).as("orderPriceWon"),
                                (purchaseRequest.orderAmount.multiply(item.inputUnitPrice).doubleValue()).multiply(0.1).as("vat"),
//                                purchaseRequest.orderPossibleAmount.as("orderPossibleAmount"),
//                                purchaseRequest.inputAmount.as("inputAmount"),
                                purchaseRequest.cancelAmount.as("cancelAmount"),
                                purchaseRequest.periodDate.as("orderPeriodDate"),
                                purchaseRequest.note.as("note"),
                                client.clientName.as("manufacturerName"),
                                purchaseRequest.ordersState.as("orderState"),
                                item.testType.as("testType")
                        )
                )
                .from(purchaseRequest)
                .innerJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .innerJoin(unit).on(unit.id.eq(item.unit.id))
                .innerJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .where(
                        purchaseRequest.purchaseOrder.id.eq(purchaseOrderId),
                        purchaseRequest.deleteYn.isFalse()
                )
                .fetch();
    }

    // 9-3. 발주현황조회
    // 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간 fromDate~toDate
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderStatusResponse> findPurchaseOrderStatusResponseAllByCondition(
            Long currencyId,
            Long userId,
            Long clientId,
            Long wareHouseId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseOrderStatusResponse.class,
                                purchaseRequest.id.as("id"),
                                purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                client.clientCode.as("clientCode"),
                                client.clientName.as("clientName"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                purchaseRequest.orderAmount.as("orderAmount"),
                                unit.unitCodeName.as("orderUnitCodeName"),
                                item.inputUnitPrice.as("unitPrice"),
                                item.inputUnitPrice.multiply(purchaseRequest.orderAmount).as("orderPrice"),
                                (purchaseRequest.orderAmount.multiply(item.inputUnitPrice).doubleValue()).multiply(0.1).as("vat"),
                                purchaseRequest.periodDate.as("orderPeriodDate"),
                                user.id.as("userId"),
                                user.korName.as("userName"),
                                purchaseOrder.note.as("note"),
                                wareHouse.wareHouseName.as("wareHouseName"),
                                currency.currency.as("currencyUnit"),
                                purchaseRequest.cancelAmount.as("cancelAmount"),
                                purchaseRequest.ordersState.as("orderState"),
                                purchaseOrder.createdDate.as("createdDate")
                        )
                )
                .from(purchaseRequest)
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .leftJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .leftJoin(client).on(client.id.eq(purchaseOrder.client.id))
                .leftJoin(user).on(user.id.eq(purchaseOrder.user.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(purchaseOrder.wareHouse.id))
                .leftJoin(currency).on(currency.id.eq(purchaseOrder.currency.id))
                .where(
                        isCurrencyEq(currencyId),
                        isUserEq(userId),
                        isClientEq(clientId),
                        isWareHouseEq(wareHouseId),
                        isPeriodDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 9-3. 발주현황조회 단일 조회
    // 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간 fromDate~toDate
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderStatusResponse> findPurchaseOrderStatusResponse(Long purchaseOrderId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        PurchaseOrderStatusResponse.class,
                                        purchaseRequest.id.as("id"),
                                        purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                        client.clientCode.as("clientCode"),
                                        client.clientName.as("clientName"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        item.standard.as("itemStandard"),
                                        item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                        purchaseRequest.orderAmount.as("orderAmount"),
                                        unit.unitCodeName.as("orderUnitCodeName"),
                                        item.inputUnitPrice.as("unitPrice"),
                                        item.inputUnitPrice.multiply(purchaseRequest.orderAmount).as("orderPrice"),
                                        (purchaseRequest.orderAmount.multiply(item.inputUnitPrice).doubleValue()).multiply(0.1).as("vat"),
                                        purchaseRequest.periodDate.as("orderPeriodDate"),
                                        user.korName.as("userName"),
                                        purchaseOrder.note.as("note"),
                                        wareHouse.wareHouseName.as("wareHouseName"),
                                        currency.currency.as("currencyUnit"),
                                        purchaseRequest.cancelAmount.as("cancelAmount"),
                                        purchaseRequest.ordersState.as("orderState"),
                                        purchaseOrder.createdDate.as("createdDate")
                                )
                        )
                        .from(purchaseRequest)
                        .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                        .leftJoin(item).on(item.id.eq(purchaseRequest.item.id))
                        .leftJoin(unit).on(unit.id.eq(item.unit.id))
                        .leftJoin(client).on(client.id.eq(purchaseOrder.client.id))
                        .leftJoin(user).on(user.id.eq(purchaseOrder.user.id))
                        .leftJoin(wareHouse).on(wareHouse.id.eq(purchaseOrder.wareHouse.id))
                        .leftJoin(currency).on(currency.id.eq(purchaseOrder.currency.id))
                        .where(
                                isDeleteYnFalse(),
                                purchaseOrder.id.eq(purchaseOrderId)
                        )
                        .orderBy(purchaseRequest.createdDate.desc())
                        .limit(1)
                        .fetchOne()
        );
    }

    // pop
    // 구매발주에 대한 입고가 완료되지 않은 구매발주
    @Override
    public List<PopPurchaseOrderResponse> findPopPurchaseOrderResponses(String clientName) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopPurchaseOrderResponse.class,
                                purchaseOrder.id.as("purchaseOrderId"),
                                purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                client.clientName.as("purchaseOrderClient"),
                                purchaseRequest.ordersState.as("orderState")
                        )
                )
                .from(purchaseRequest)
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .leftJoin(client).on(client.id.eq(purchaseOrder.client.id))
                .where(
                        purchaseRequest.deleteYn.isFalse(),
                        purchaseOrder.deleteYn.isFalse(),
//                        purchaseRequest.ordersState.eq(SCHEDULE).or(purchaseRequest.ordersState.eq(ONGOING)),
                        isClientNameContain(clientName)
                )
                .groupBy(purchaseOrder.id)
                .orderBy(purchaseOrder.purchaseOrderDate.desc())
                .fetch();
    }

    // 구매발주에 등록된 구매요청 하나 조회
    @Override
    public Optional<Long> findOneByPurchaseRequestId(Long purchaseOrderId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(purchaseRequest.id)
                        .from(purchaseRequest)
                        .where(
                                purchaseRequest.purchaseOrder.id.eq(purchaseOrderId)
                        )
                        .orderBy(purchaseRequest.createdDate.asc())
                        .limit(1)
                        .fetchOne()
        );
    }

    private BooleanExpression isClientNameContain(String clientName) {
        return clientName != null ? client.clientName.contains(clientName) : null;
    }

    // 거래처
    private BooleanExpression isClientEq(Long clientId) {
        return clientId != null ? client.id.eq(clientId) : null;
    }
    // 화폐
    private BooleanExpression isCurrencyEq(Long currencyId) {
        return currencyId != null ? currency.id.eq(currencyId) : null;
    }
    // 담당자
    private BooleanExpression isUserEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }
    // 입고창고
    private BooleanExpression isWareHouseEq(Long wareHouseId) {
        return wareHouseId != null ? wareHouse.id.eq(wareHouseId) : null;
    }
    // 발주기간
    private BooleanExpression isPeriodDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? purchaseOrder.purchaseOrderDate.between(fromDate, toDate) : null;
    }
    // 삭제여부
    private BooleanExpression isDeleteYnFalse() {
        return purchaseOrder.deleteYn.isFalse();
    }
}
