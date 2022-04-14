package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ShipmentLotInfoResponse;
import com.mes.mesBackend.dto.response.ShipmentStatusResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.custom.ShipmentLotRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ShipmentLotRepositoryImpl implements ShipmentLotRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QShipmentLot shipmentLot = QShipmentLot.shipmentLot;
    final QShipmentItem shipmentItem = QShipmentItem.shipmentItem;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QContractItem contractItem = QContractItem.contractItem;
    final QItem item = QItem.item;
    final QUnit unit = QUnit.unit;
    final QShipment shipment = QShipment.shipment;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QClient client = QClient.client;
    final QUser user = QUser.user;
    final QCurrency currency = QCurrency.currency1;
    final QContract contract = QContract.contract;


    // lotMaster: shipmentItem 의 item 에 해당되는 lotMaster 가져옴, 조건? 공정이 포장까지 완료된, stockAmount 가 1 이상
    @Override
    public List<Long> findLotMasterIdByItemIdAndWorkProcessShipment(Long itemId, WorkProcessDivision workProcessDivision) {
        return jpaQueryFactory
                .select(lotMaster.id)
                .from(lotMaster)
                .where(
                        lotMaster.item.id.eq(itemId),
                        lotMaster.workProcess.workProcessDivision.eq(workProcessDivision),
                        lotMaster.stockAmount.goe(1),
                        lotMaster.deleteYn.isFalse()
                )
                .fetch();
    }

    // 단일조회
    @Override
    public Optional<ShipmentLotInfoResponse> findShipmentLotResponseById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        ShipmentLotInfoResponse.class,
                                        shipmentLot.id.as("shipmentLotId"),
                                        lotMaster.id.as("lotId"),
                                        lotMaster.lotNo.as("lotNo"),
                                        unit.unitCodeName.as("contractUnit"),
                                        lotMaster.shipmentAmount.as("shipmentOutputAmount"),
                                        lotMaster.shipmentAmount.multiply(item.inputUnitPrice).as("shipmentPrice"),
                                        lotMaster.shipmentAmount.multiply(item.inputUnitPrice).as("shipmentPriceWon"),
                                        lotMaster.shipmentAmount.multiply(item.inputUnitPrice).doubleValue().multiply(0.1).as("vat")
                                )
                        )
                        .from(shipmentLot)
                        .leftJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                        .leftJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                        .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                        .leftJoin(item).on(item.id.eq(contractItem.item.id))
                        .leftJoin(unit).on(unit.id.eq(item.unit.id))
                        .where(
                                shipmentLot.id.eq(id),
                                shipmentLot.deleteYn.isFalse(),
                                shipmentLot.shipmentItem.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }
    // 전체조회
    @Override
    public List<ShipmentLotInfoResponse> findShipmentLotResponsesByShipmentItemId(Long shipmentItemId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ShipmentLotInfoResponse.class,
                                shipmentLot.id.as("shipmentLotId"),
                                lotMaster.id.as("lotId"),
                                lotMaster.lotNo.as("lotNo"),
                                unit.unitCodeName.as("contractUnit"),
                                lotMaster.shipmentAmount.as("shipmentOutputAmount"),
                                lotMaster.shipmentAmount.multiply(item.inputUnitPrice).as("shipmentPrice"),
                                lotMaster.shipmentAmount.multiply(item.inputUnitPrice).as("shipmentPriceWon"),
                                lotMaster.shipmentAmount.multiply(item.inputUnitPrice).doubleValue().multiply(0.1).as("vat")
                        )
                )
                .from(shipmentLot)
                .leftJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                .leftJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .where(
                        shipmentLot.shipmentItem.id.eq(shipmentItemId),
                        shipmentLot.deleteYn.isFalse()
                )
                .orderBy(shipmentLot.createdDate.desc())
                .fetch();
    }

    // 출하 품목정보에 해당하는 LOT 정보가 있는지 여부
    @Override
    public boolean existsByShipmentItemInShipmentLot(Long shipmentId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(shipmentLot)
                .where(
                        shipmentLot.shipmentItem.id.eq(shipmentId),
                        shipmentLot.deleteYn.isFalse()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    // 출하 품목에 등록 된 lotMaster 의 재고수량 모듀
    @Override
    public List<Integer> findShipmentLotShipmentAmountByShipmentItemId(Long shipmentItemId) {
        return jpaQueryFactory
                .select(lotMaster.shipmentAmount)
                .from(shipmentLot)
                .innerJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                .where(
                        shipmentLot.shipmentItem.id.eq(shipmentItemId),
                        shipmentLot.deleteYn.isFalse()
                )
                .orderBy(shipmentLot.createdDate.desc())
                .fetch();
    }

    // ==================================================== 4-7. 출하 현황 ====================================================
    // 출하현황 검색 리스트 조회, 검색조건: 거래처 id, 출하기간 fromDate~toDate, 화폐 id, 담당자 id, 품번|품명
    @Override
    public List<ShipmentStatusResponse> findShipmentStatusesResponsesByCondition(
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            Long userId,
            String itemNoAndItemName
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ShipmentStatusResponse.class,
                                shipment.id.as("shipmentId"),
                                shipment.shipmentDate.as("shipmentDate"),
                                shipment.shipmentNo.as("shipmentNo"),
                                client.clientCode.as("clientCode"),
                                client.clientName.as("clientName"),
                                user.id.as("userId"),
                                user.korName.as("userManager"),
                                wareHouse.wareHouseName.as("wareHouseName"),
                                contract.surtax.as("surtax"),
                                shipmentItem.id.as("shipmentItemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                unit.unitCodeName.as("contractUnit"),
                                contract.contractNo.as("contractNo"),
                                lotMaster.shipmentAmount.as("shipmentAmount"),
                                currency.currency.as("currency"),
                                currency.exchangeRate.as("exchangeRate"),
                                lotMaster.shipmentAmount.multiply(item.inputUnitPrice).as("shipmentPrice"),
                                lotMaster.shipmentAmount.multiply(item.inputUnitPrice).as("shipmentPriceWon"),
                                lotMaster.shipmentAmount.multiply(item.inputUnitPrice).doubleValue().multiply(0.1).as("vat"),
                                shipmentLot.id.as("shipmentLotId"),
                                lotMaster.lotNo.as("lotNo"),
                                contract.periodDate.as("periodDate"),
                                shipment.note.as("note")
                        )
                )
                .from(shipmentLot)
                .innerJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                .innerJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                .innerJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                .leftJoin(client).on(client.id.eq(shipment.client.id))
                .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                .leftJoin(contract).on(contract.id.eq(contractItem.contract.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .leftJoin(user).on(user.id.eq(contract.user.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .leftJoin(currency).on(currency.id.eq(contract.currency.id))
                .where(
                        isClientIdEq(clientId),
                        isShipmentDateBetween(fromDate, toDate),
                        isCurrencyEq(currencyId),
                        isUserIdEq(userId),
                        isItemNoAndItemNameContain(itemNoAndItemName),
                        isShipmentDeleteYnFalse(),
                        isShipmentItemDeleteYnFalse(),
                        isShipmentLotDeleteYnFalse()
                )
                .orderBy(shipmentLot.createdDate.desc())
                .fetch();
    }
    // 출하현황 검색 리스트 조회, 검색조건: 거래처 id, 출하기간 fromDate~toDate, 화폐 id, 담당자 id, 품번|품명
    // 품번|품명
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName).or(item.itemName.contains(itemNoAndName)) : null;
    }
    // 거래처 조회
    private BooleanExpression isClientIdEq(Long clientId) {
        return clientId != null ? client.id.eq(clientId) : null;
    }
    // 출하기간 조회
    private BooleanExpression isShipmentDateBetween(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null) {
            return shipment.shipmentDate.between(fromDate, toDate);
        } else if (fromDate != null) {
            return shipment.shipmentDate.after(fromDate);
        } else if (toDate != null) {
            return shipment.shipmentDate.before(toDate);
        } else {
            return null;
        }
    }
    // 화폐로 조회
    private BooleanExpression isCurrencyEq(Long currencyId) {
        return currencyId != null ? currency.id.eq(currencyId) : null;
    }
    // 담당자
    private BooleanExpression isUserIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }
    // shipment
    private BooleanExpression isShipmentDeleteYnFalse() {
        return shipment.deleteYn.isFalse();
    }
    // shipmentItem
    private BooleanExpression isShipmentItemDeleteYnFalse() {
        return shipmentItem.deleteYn.isFalse();
    }
    // shipmentLot
    private BooleanExpression isShipmentLotDeleteYnFalse() {
        return shipmentLot.deleteYn.isFalse();
    }
}
