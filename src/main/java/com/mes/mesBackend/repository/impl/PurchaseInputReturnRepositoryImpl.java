package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.dto.response.PurchaseInputReturnResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.repository.custom.PurchaseInputReturnRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PURCHASE_INPUT;

// 9-6. 구매입고 반품 등록
@RequiredArgsConstructor
public class PurchaseInputReturnRepositoryImpl implements PurchaseInputReturnRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QPurchaseInputReturn purchaseInputReturn = QPurchaseInputReturn.purchaseInputReturn;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;
    final QClient client = QClient.client;
    final QPurchaseInput purchaseInput = QPurchaseInput.purchaseInput;
    final QTestCriteria testCriteria = QTestCriteria.testCriteria1;

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
                                lotMaster.badItemAmount.as("badItemAmountPossibleAmount"),
                                lotMaster.inputAmount.as("lotInputAmount"),
                                lotMaster.inputAmount.multiply(item.inputUnitPrice).as("lotInputPrice"),
                                (lotMaster.inputAmount.multiply(item.inputUnitPrice).doubleValue()).multiply(0.1).as("lotInputPriceSurtax"),
                                purchaseInput.clientLotNo.as("clientLotNo"),
                                purchaseInput.manufactureDate.as("manufactureDate"),
                                purchaseInput.validDate.as("validDate"),
                                testCriteria.testCriteria.as("testCriteria"),
                                purchaseInput.testReportYn.as("testReportYn"),
                                purchaseInputReturn.returnAmount.multiply(item.inputUnitPrice).as("returnPrice"),
                                purchaseInputReturn.returnAmount.multiply(item.inputUnitPrice).as("returnPriceWon")
                        )
                )
                .from(purchaseInputReturn)
                .innerJoin(lotMaster).on(lotMaster.id.eq(purchaseInputReturn.lotMaster.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(purchaseInput).on(purchaseInput.id.eq(lotMaster.purchaseInput.id))
                .leftJoin(testCriteria).on(testCriteria.id.eq(item.testCriteria.id))
                .where(
                        isClientIdEq(clientId),
                        isItemNoAndItemNameContain(itemNoOrItemName),
                        isReturnDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .orderBy(purchaseInputReturn.createdDate.desc())
                .fetch();
    }
    // LotMasterId, 분류로 구매입고반품 찾기
    public PurchaseInputReturnResponse findPurchaseInputReturnByCondition(Long lotMasterId, boolean returnDivision){
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
                        purchaseInputReturn.lotMaster.id.eq(lotMasterId),
                        purchaseInputReturn.returnDivision.eq(returnDivision),
                        purchaseInputReturn.deleteYn.eq(false)
                )
                .fetchOne();
    }

    // 구매입고반품 가능한 lotMatser 조회
    @Override
    public List<LotMasterResponse.stockAmountAndBadItemAmount> findPurchaseInputReturnPossbleLotMasters() {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                LotMasterResponse.stockAmountAndBadItemAmount.class,
                                lotMaster.id.as("id"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.stockAmount.as("stockAmount"),
                                lotMaster.badItemAmount.as("badItemAmount")
                        )
                )
                .from(lotMaster)
                .where(
                        lotMaster.enrollmentType.eq(PURCHASE_INPUT),
                        lotMaster.stockAmount.ne(0).or(lotMaster.badItemAmount.ne(0)),
                        lotMaster.deleteYn.isFalse()

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
