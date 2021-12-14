package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.BomItemDetailResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.BomItemDetailRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class BomItemDetailRepositoryImpl implements BomItemDetailRepositoryCustom {
    // 검색조건: 품목|품명

    private final JPAQueryFactory jpaQueryFactory;

    final QBomItemDetail bomItemDetail = QBomItemDetail.bomItemDetail;
    final QBomMaster qBomMaster = QBomMaster.bomMaster;
    final QItem item = QItem.item;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QUnit unit = QUnit.unit;
    final QClient client = QClient.client;
    final QWorkProcess workProcess = QWorkProcess.workProcess;

    @Transactional(readOnly = true)
    @Override
    public List<BomItemDetailResponse> findAllByCondition(
            Long bomMasterId,
            String itemNoOrItemName
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(BomItemDetailResponse.class,
                                bomItemDetail.id.as("id"),
                                bomItemDetail.level.as("level"),
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemAccount.account.as("itemAccount"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                item.manufacturer.clientName.as("itemClientName"),
                                unit.unitCodeName.as("itemUnitCodeName"),
                                item.storageLocation.as("itemStorageLocation"),
                                item.inputUnitPrice.as("itemInputUnitPrice"),
                                bomItemDetail.amount.as("amount"),
                                client.id.as("toBuyId"),
                                client.clientName.as("toBuyName"),
                                bomItemDetail.amount.multiply(item.inputUnitPrice).as("price"),
                                workProcess.id.as("workProcessId"),
                                workProcess.workProcessName.as("workProcessName"),
                                bomItemDetail.useYn.as("useYn"),
                                bomItemDetail.note.as("note"),
                                qBomMaster.id.as("qbomId")
                        )
                )
                .from(bomItemDetail)
                .leftJoin(item).on(item.id.eq(bomItemDetail.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .leftJoin(client).on(client.id.eq(bomItemDetail.toBuy.id))
                .leftJoin(workProcess).on(workProcess.id.eq(bomItemDetail.workProcess.id))
                .leftJoin(qBomMaster).on(qBomMaster.id.eq(bomItemDetail.bomMaster.id))
                .where(
                        isBomMasterEq(bomMasterId),
                        isItemNoOrItemNameToItemNoOrItemName(itemNoOrItemName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 품목|품명으로 검색
    private BooleanExpression isItemNoOrItemNameToItemNoOrItemName(String itemNoOrItemName) {
        return itemNoOrItemName != null ? item.itemNo.contains(itemNoOrItemName)
                .or(item.itemName.contains(itemNoOrItemName)) : null;
    }

    // BOM master Id 로 검색
    private BooleanExpression isBomMasterEq(Long bomMasterId) {
        return qBomMaster.id.eq(bomMasterId);
    }

    // 삭제여부 false
    private BooleanExpression isDeleteYnFalse() {
        return bomItemDetail.deleteYn.isFalse();
    }
}
