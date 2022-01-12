package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PurchaseInputReturnResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.PurchaseInputReturnRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 9-6. 구매입고 반품 등록
@RequiredArgsConstructor
public class PurchaseInputReturnRepositoryImpl implements PurchaseInputReturnRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QPurchaseInputReturn purchaseInputReturn = QPurchaseInputReturn.purchaseInputReturn;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;
    final QClient client = QClient.client;
    final QPurchaseInput purchaseInput = QPurchaseInput.purchaseInput;

    // 구매입고반품 단일조회
    @Override
    public Optional<PurchaseInputReturnResponse> findPurchaseInputReturnResponseById(Long purchaseInputReturnId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        PurchaseInputReturnResponse.class,
                                        purchaseInputReturn.id.as("id"),
                                        client.clientName.as("clientName"),
                                        purchaseInput.id.as("purchaseInputId"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        item.standard.as("itemStandard"),
                                        item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                        lotMaster.id.as("lotMasterId"),
                                        lotMaster.lotNo.as("lotNo"),
                                        purchaseInputReturn.returnDate.as("returnDate"),
                                        purchaseInputReturn.returnAmount.as("returnAmount"),
                                        purchaseInputReturn.note.as("note"),
                                        purchaseInputReturn.returnDivision.as("returnDivision"),
                                        lotMaster.stockAmount.as("stockAmountPossibleAmount"),
                                        lotMaster.badItemAmount.as("badItemAmountPossibleAmount")
                                )
                        )
                        .from(purchaseInputReturn)
                        .innerJoin(lotMaster).on(lotMaster.id.eq(purchaseInputReturn.lotMaster.id))
                        .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                        .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                        .leftJoin(purchaseInput).on(purchaseInput.id.eq(lotMaster.purchaseInput.id))
                        .where(
                                purchaseInputReturn.id.eq(purchaseInputReturnId),
                                isDeleteYnFalse()
                        )
                        .fetchOne()
        );
    }
    // 구매입고반품 리스트 검색 조회, 검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate
    @Override
    public List<PurchaseInputReturnResponse> findPurchaseInputReturnResponsesByCondition(
            Long clientId,
            String itemNoOrItemName,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseInputReturnResponse.class,
                                purchaseInputReturn.id.as("id"),
                                client.clientName.as("clientName"),
                                purchaseInput.id.as("purchaseInputId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                lotMaster.id.as("lotMasterId"),
                                lotMaster.lotNo.as("lotNo"),
                                purchaseInputReturn.returnDate.as("returnDate"),
                                purchaseInputReturn.returnAmount.as("returnAmount"),
                                purchaseInputReturn.note.as("note"),
                                purchaseInputReturn.returnDivision.as("returnDivision"),
                                lotMaster.stockAmount.as("stockAmountPossibleAmount"),
                                lotMaster.badItemAmount.as("badItemAmountPossibleAmount")
                        )
                )
                .from(purchaseInputReturn)
                .innerJoin(lotMaster).on(lotMaster.id.eq(purchaseInputReturn.lotMaster.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(purchaseInput).on(purchaseInput.id.eq(lotMaster.purchaseInput.id))
                .where(
                        isClientIdEq(clientId),
                        isItemNoAndItemNameContain(itemNoOrItemName),
                        isReturnDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 구매입고반품 리스트 검색 조회, 검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate
    private BooleanExpression isClientIdEq(Long clientId) {
        return clientId != null ? client.id.eq(clientId) : null;
    }

    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName)
                .or(item.itemName.contains(itemNoAndName)) : null;
    }

    private BooleanExpression isReturnDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? purchaseInputReturn.returnDate.between(fromDate, toDate) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return purchaseInputReturn.deleteYn.isFalse();
    }
}
