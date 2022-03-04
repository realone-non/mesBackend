package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PurchaseInputDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseInputResponse;
import com.mes.mesBackend.dto.response.PurchaseStatusCheckResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.PurchaseInputRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// 9-5. 구매입고 등록
@RequiredArgsConstructor
public class PurchaseInputRepositoryImpl implements PurchaseInputRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QPurchaseInput purchaseInput = QPurchaseInput.purchaseInput;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;
    final QUnit unit = QUnit.unit;
    final QPurchaseRequest purchaseRequest = QPurchaseRequest.purchaseRequest;
    final QClient client = QClient.client;
    final QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
    final QUser user = QUser.user;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QCurrency currency = QCurrency.currency1;
    final QTestCriteria testCriteria = QTestCriteria.testCriteria1;

    // 구매입고 리스트 조회, 검색조건: 입고기간 fromDate~toDate, 입고창고, 거래처, 품명|품번
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseInputResponse> findPurchaseRequestsByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            Long wareHouseId,
            Long clientId,
            String itemNoOrItemName
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseInputResponse.class,
                                purchaseRequest.id.as("id"),
                                client.clientCode.as("clientCode"),
                                client.clientName.as("clientName"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                purchaseRequest.inputDate.as("inputDate"),
                                unit.unitCodeName.as("orderUnitCodeName"),
                                item.inputUnitPrice.as("unitPrice"),
                                wareHouse.wareHouseName.as("wareHouseName"),
                                purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                user.korName.as("userName"),
                                currency.currency.as("currencyUnit"),
                                purchaseOrder.note.as("note"),
                                purchaseOrder.purchaseOrderDate.as("periodDate"),
                                purchaseRequest.orderAmount.as("orderAmount")
                        )
                )
                .from(purchaseRequest)
                .innerJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .leftJoin(client).on(client.id.eq(purchaseOrder.client.id))
                .leftJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(purchaseOrder.wareHouse.id))
                .leftJoin(user).on(user.id.eq(purchaseOrder.user.id))
                .leftJoin(currency).on(currency.id.eq(purchaseOrder.currency.id))
                .where(
                        isInputDateBetween(fromDate, toDate),
                        isWareHouseEq(wareHouseId),
                        isClientEq(clientId),
                        isItemNoOrItemNameContain(itemNoOrItemName),
                        isPurchaseRequestDeleteYnFalse()
                )
                .fetch();
    }

    // 구매입고 LOT 정보 단일 조회
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseInputDetailResponse> findPurchaseInputDetailByIdAndPurchaseInputId(Long purchaseRequestId, Long purchaseInputId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        PurchaseInputDetailResponse.class,
                                        purchaseInput.id.as("id"),
                                        lotMaster.id.as("lotId"),
                                        lotMaster.lotNo.as("lotNo"),
                                        purchaseInput.inputAmount.as("inputAmount"),
                                        item.inputUnitPrice.multiply(purchaseInput.inputAmount).as("inputPrice"),
                                        (item.inputUnitPrice.multiply(purchaseInput.inputAmount).doubleValue()).multiply(0.1).as("vat"),
                                        item.testType.as("testType"),   // 검사유형 ex) 수입검사
                                        purchaseInput.manufactureDate.as("manufactureDate"),
                                        purchaseInput.validDate.as("validDate"),
                                        testCriteria.testCriteria.as("testCriteria"),
                                        item.inspectionType.as("inspectionType"),   // 검사방법 ex) 샘플링
                                        purchaseInput.urgentYn.as("urgentYn"),
                                        purchaseInput.testReportYn.as("testReportYn"),
                                        purchaseInput.coc.as("coc")
                                        )
                        )
                        .from(purchaseInput)
                        .innerJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                        .innerJoin(lotMaster).on(lotMaster.purchaseInput.id.eq(purchaseInput.id))
                        .innerJoin(item).on(item.id.eq(purchaseRequest.item.id))
                        .leftJoin(testCriteria).on(testCriteria.id.eq(item.testCriteria.id))
                        .where(
                                isPurchaseInputIdEq(purchaseInputId),
                                isPurchaseRequestIdEq(purchaseRequestId),
                                isPurchaseRequestDeleteYnFalse(),
                                isPurchaseInputDeleteYnFalse()
                        )
                        .fetchOne());
    }

    // 구매입고에 들어갈 item 조회 lot 번호 생성할때 쓰임
    @Override
    @Transactional(readOnly = true)
    public Long findItemIdByPurchaseInputId(Long purchaseInputId) {
        return jpaQueryFactory
                        .select(item.id)
                        .from(purchaseInput)
                        .innerJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                        .innerJoin(item).on(item.id.eq(purchaseRequest.item.id))
                        .where(
                                isPurchaseInputIdEq(purchaseInputId),
                                isPurchaseInputDeleteYnFalse(),
                                isPurchaseRequestDeleteYnFalse()
                        )
                        .fetchOne();
    }

    // 구매요청에 해당하는 구매발주상세의 입고수량을 전부 조회
    @Override
    @Transactional(readOnly = true)
    public List<Integer> findInputAmountByPurchaseRequestId(Long purchaseRequestId) {
        return jpaQueryFactory
                .select(purchaseInput.inputAmount)
                .from(purchaseInput)
                .innerJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                .where(
                        isPurchaseRequestIdEq(purchaseRequestId),
                        isPurchaseInputDeleteYnFalse(),
                        isPurchaseRequestDeleteYnFalse()
                )
                .fetch();
    }

    // 구매입고 정보에 해당하는 구매입고 LOT 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseInputDetailResponse> findPurchaseInputDetailByPurchaseRequestId(Long purchaseRequestId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseInputDetailResponse.class,
                                purchaseInput.id.as("id"),
                                lotMaster.id.as("lotId"),
                                lotMaster.lotNo.as("lotNo"),
                                purchaseInput.inputAmount.as("inputAmount"),
                                item.inputUnitPrice.multiply(purchaseInput.inputAmount).as("inputPrice"),
                                (item.inputUnitPrice.multiply(purchaseInput.inputAmount).doubleValue()).multiply(0.1).as("vat"),
                                item.testType.as("testType"),   // 검사유형 ex) 수입검사
                                purchaseInput.manufactureDate.as("manufactureDate"),
                                purchaseInput.validDate.as("validDate"),
                                testCriteria.testCriteria.as("testCriteria"),
                                item.inspectionType.as("inspectionType"),   // 검사방법 ex) 샘플링
                                purchaseInput.urgentYn.as("urgentYn"),
                                purchaseInput.testReportYn.as("testReportYn"),
                                purchaseInput.coc.as("coc"),
                                purchaseInput.clientLotNo.as("clientLotNo")
                        )
                )
                .from(purchaseInput)
                .innerJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                .innerJoin(lotMaster).on(lotMaster.purchaseInput.id.eq(purchaseInput.id))
                .innerJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .leftJoin(testCriteria).on(testCriteria.id.eq(item.testCriteria.id))
                .where(
                        isPurchaseRequestIdEq(purchaseRequestId),
                        isPurchaseInputDeleteYnFalse(),
                        isPurchaseRequestDeleteYnFalse()
                )
                .fetch();
    }

    // 구매요청에 해당하는 구매발주상세의 제일 최근 등록된 날짜 조회
    @Override
    @Transactional(readOnly = true)
    public Optional<LocalDateTime> findCreatedDateByPurchaseRequestId(Long purchaseRequestId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(purchaseInput.createdDate)
                        .from(purchaseInput)
                        .leftJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                        .where(
                                isPurchaseRequestIdEq(purchaseRequestId),
                                isPurchaseInputDeleteYnFalse(),
                                isPurchaseRequestDeleteYnFalse()
                        )
                        .orderBy(purchaseInput.createdDate.desc())
                        .limit(1)
                        .fetchOne());

    }

    //  9-4. 구매현황조회
    // 구매현황 리스트 조회 / 검색조건: 거래처 id, 품명|품목, 입고기간 fromDate~toDate
    @Override
    public List<PurchaseStatusCheckResponse> findPurchaseStatusCheckByCondition(
            Long clientId,
            String itemNoAndItemName,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PurchaseStatusCheckResponse.class,
                                purchaseInput.id.as("id"),
                                purchaseInput.createdDate.as("inputDate"),
                                client.clientCode.as("clientCode"),
                                client.clientName.as("clientName"),
                                purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),
                                purchaseInput.inputAmount.as("inputAmount"),
                                purchaseInput.inputAmount.multiply(item.inputUnitPrice).as("inputPrice"),
                                purchaseInput.id.as("purchaseInputNo"),
                                unit.unitCodeName.as("orderUnitCodeName"),
                                (purchaseInput.inputAmount.multiply(item.inputUnitPrice).doubleValue()).multiply(0.1).as("vat"),
                                lotMaster.lotNo.as("lotNo")
                        )
                )
                .from(purchaseInput)
                .innerJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                .innerJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .innerJoin(item).on(item.id.eq(purchaseRequest.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .innerJoin(lotMaster).on(lotMaster.purchaseInput.id.eq(purchaseInput.id))
                .leftJoin(client).on(client.id.eq(purchaseOrder.client.id))
                .where(
                        isInputDateBetween(fromDate, toDate),
                        isClientEq(clientId),
                        isItemNoOrItemNameContain(itemNoAndItemName),
                        isPurchaseInputDeleteYnFalse()
                )
                .fetch();
    }

    // 입고기간
    private BooleanExpression isInputDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? purchaseRequest.inputDate.between(fromDate, toDate) : null;
    }
    // 입고창고
    private BooleanExpression isWareHouseEq(Long wareHouseId) {
        return wareHouseId != null ? wareHouse.id.eq(wareHouseId) : null;
    }
    // 거래처
    private BooleanExpression isClientEq(Long clientId) {
        return clientId != null ? client.id.eq(clientId) : null;
    }
    // 품번|품명
    private BooleanExpression isItemNoOrItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName).or(item.itemName.contains(itemNoAndName)) : null;
    }
    // 구매입고 삭제여부
    private BooleanExpression isPurchaseInputDeleteYnFalse() {
        return purchaseInput.deleteYn.isFalse();
    }
    // 구매요청 삭제여부
    private BooleanExpression isPurchaseRequestDeleteYnFalse() {
        return purchaseRequest.deleteYn.isFalse();
    }
    // 구매요청 id
    public BooleanExpression isPurchaseRequestIdEq(Long purchaseRequestId) { return purchaseRequest.id.eq(purchaseRequestId); }
    // 구매입고 id
    public BooleanExpression isPurchaseInputIdEq(Long purchaseInputId) {
        return purchaseInput.id.eq(purchaseInputId);
    }
}
