package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ItemLogResponse;
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

    //일자별 품목 변동 사항 전체 조회 / 검색조건 : 창고, 생성기간
    public List<ItemLogResponse> findAllCondition(Long warehouseId, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ItemLogResponse.class,
                                itemLog.wareHouse.wareHouseName.as("warehouse"),
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
                .where(
                    isItemLogBetween(startDate, endDate),
                    isWareHouseNull(warehouseId),
                    itemLog.deleteYn.eq(false)
                )
                .fetch();
    }

    //일자별 품목 변동 사항 단일 조회
    public ItemLogResponse findByItemIdAndwareHouseId(Long itemId, Long warehouseId){
        return jpaQueryFactory
        .select(
                Projections.fields(
                        ItemLogResponse.class,
                        itemLog.wareHouse.wareHouseName.as("warehouse"),
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
                .where(
                        isWareHouseNull(warehouseId),
                        isItemNull(itemId),
                        itemLog.deleteYn.eq(false)
                )
                .fetchOne();
    }

    private BooleanExpression isItemLogBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? itemLog.logDate.between(fromDate, toDate) : null;
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
