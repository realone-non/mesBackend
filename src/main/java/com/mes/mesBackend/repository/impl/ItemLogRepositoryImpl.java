package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ReceiptAndPaymentResponse;
import com.mes.mesBackend.dto.response.WarehouseInventoryResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.ItemLogRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ItemLogRepositoryImpl implements ItemLogRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QItemLog itemLog = QItemLog.itemLog;
    final QItem item = QItem.item;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QItemAccount itemAccount = QItemAccount.itemAccount;

    //일자별 품목 변동 사항 전체 조회 / 검색조건 : 창고, 생성기간
    public List<ItemLog> findAllCondition(Long warehouseId, LocalDate startDate, LocalDate endDate, boolean isOut){
        return jpaQueryFactory
                .selectFrom(itemLog)
                .where(
                    isItemLogBetween(startDate, endDate),
                    isWareHouseNull(warehouseId),
                    itemLog.deleteYn.eq(false),
                    itemLog.outsourcingYn.eq(isOut),
                        itemLog.logDate.eq(LocalDate.now())
                )
                .fetch();
    }

    //일자별 품목 변동 사항 전체 조회(Response반환) / 검색조건 : 품목그룹, 창고, 생성기간
    public List<ReceiptAndPaymentResponse> findAllConditionResponse(Long warehouseId, Long itemAccountId, LocalDate startDate, LocalDate endDate, boolean isOut){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ReceiptAndPaymentResponse.class,
                                wareHouse.wareHouseName.as("warehouseName"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemLog.stockAmount.as("storeAmount"),
                                itemLog.createdAmount.as("createdAmount"),
                                itemLog.badItemAmount.as("badItemAmount"),
                                itemLog.inputAmount.as("inputAmount"),
                                itemLog.shipmentAmount.as("shipmentAmount"),
                                itemLog.stockRealAmount.as("stockRealAmount"),
                                itemLog.moveAmount.as("moveAmount"),
                                itemLog.returnAmount.as("returnAmount"),
                                itemLog.stockAmount.as("stockAmount")
                        )
                )
                .from(itemLog)
                .innerJoin(item).on(item.id.eq(itemLog.item.id))
                .innerJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .innerJoin(wareHouse).on(wareHouse.id.eq(itemLog.wareHouse.id))
                .where(
                        isItemLogBetween(startDate, endDate),
                        isWareHouseNull(warehouseId),
                        isItemAccountNull(itemAccountId),
                        itemLog.deleteYn.eq(false),
                        itemLog.outsourcingYn.eq(isOut),
                        itemLog.logDate.eq(LocalDate.now())
                )
                .fetch();
    }

    //일자별 품목 변동 사항 단일 조회
    public ItemLog findByItemIdAndwareHouseIdAndOutsourcingYn(Long itemId, Long warehouseId, boolean isOut){
        return jpaQueryFactory
        .selectFrom(itemLog)
                .where(
                        isWareHouseNull(warehouseId),
                        isItemNull(itemId),
                        itemLog.deleteYn.eq(false),
                        itemLog.outsourcingYn.eq(isOut),
                        itemLog.logDate.eq(LocalDate.now())
                )
                .fetchOne();
    }

    //전날 재고 확인
    public ItemLog findByItemIdAndWareHouseAndBeforeDay(Long itemId, Long warehouseId, LocalDate beforeDay){
        return jpaQueryFactory
                .selectFrom(itemLog)
                .leftJoin(item).on(item.id.eq(itemLog.item.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(itemLog.wareHouse.id))
                .where(
                        item.id.eq(itemId),
                        wareHouse.id.eq(warehouseId),
                        itemLog.logDate.eq(beforeDay)
                )
                .fetchOne();
    }

    private BooleanExpression isItemLogBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? itemLog.logDate.between(fromDate, toDate) : null;
    }

    private BooleanExpression isItemAccountNull(Long itemAccountId){
        return itemAccountId != null ? itemAccount.id.eq(itemAccountId) : null;
    }

    private BooleanExpression isItemNull(Long itemId){
        return itemId != null ? item.id.eq(itemId) : null;
    }

    private BooleanExpression isWareHouseNull(Long warehouseId){
        return warehouseId != null ? wareHouse.id.eq(warehouseId) : null;
    }

    private BooleanExpression isItemLogBeforeBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? itemLog.logDate.between(fromDate, toDate) : itemLog.logDate.eq(LocalDate.now().minusDays(1));
    }
}
