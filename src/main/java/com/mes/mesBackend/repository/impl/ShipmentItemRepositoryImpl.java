package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ShipmentItemResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.ShipmentItemRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ShipmentItemRepositoryImpl implements ShipmentItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QContract contract = QContract.contract;
    final QShipmentItem shipmentItem = QShipmentItem.shipmentItem;
    final QShipment shipment = QShipment.shipment;
    final QContractItem contractItem = QContractItem.contractItem;
    final QUnit unit = QUnit.unit;
    final QItem item = QItem.item;
    final QLotMaster lotMaster = QLotMaster.lotMaster;

    // shipmentItem 에 해당되는 제일 처음 등록된 contract 조회
    @Override
    public Optional<Contract> findContractsByShipmentId(Long shipmentId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(shipmentItem.contractItem.contract)
                        .from(shipmentItem)
                        .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                        .leftJoin(contract).on(contract.id.eq(contractItem.contract.id))
                        .where(
                                shipmentItem.shipment.id.eq(shipmentId),
                                shipmentItem.deleteYn.isFalse()
                        )
                        .orderBy(shipmentItem.createdDate.desc())
                        .fetchOne()
        );
    }

    // 출하 품목정보 단일 조회
    @Override
    public Optional<ShipmentItemResponse> findShipmentItemResponseByShipmentItemId(Long shipmentId, Long shipmentItemId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        ShipmentItemResponse.class,
                                        shipmentItem.id.as("id"),
                                        contract.id.as("contractId"),
                                        contract.contractNo.as("contractNo"),
                                        contractItem.id.as("contractItemId"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        item.standard.as("itemStandard"),
                                        unit.unitCodeName.as("contractUnit"),
                                        shipmentItem.note.as("note"),
                                        contractItem.amount.as("contractItemAmount"),
                                        item.inputUnitPrice.as("itemInputUnitPrice")
                                )
                        )
                        .from(shipmentItem)
                        .innerJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                        .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                        .leftJoin(contract).on(contract.id.eq(contractItem.contract.id))
                        .leftJoin(item).on(item.id.eq(contractItem.item.id))
                        .leftJoin(unit).on(unit.id.eq(item.unit.id))
                        .where(
                                isShipmentIdEq(shipmentId),
                                isShipmentItemIdEq(shipmentItemId),
                                isShipmentItemDeleteYnFalse()
                        )
                        .fetchOne()
        );
    }

    // 출하 품목정보 전체 조회
    @Override
    public List<ShipmentItemResponse> findShipmentResponsesByShipmentId(Long shipmentId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ShipmentItemResponse.class,
                                shipmentItem.id.as("id"),
                                contract.id.as("contractId"),
                                contract.contractNo.as("contractNo"),
                                contractItem.id.as("contractItemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                unit.unitCodeName.as("contractUnit"),
                                shipmentItem.note.as("note"),
                                contractItem.amount.as("contractItemAmount"),
                                item.inputUnitPrice.as("itemInputUnitPrice")
                        )
                )
                .from(shipmentItem)
                .innerJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                .leftJoin(contract).on(contract.id.eq(contractItem.contract.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .where(
                        isShipmentIdEq(shipmentId),
                        isShipmentItemDeleteYnFalse()
                )
                .fetch();
    }

    // 출하에 수주품목이 있는지
    @Override
    public boolean existsByContractItemInShipment(Long shipmentId, Long contractItemId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(shipmentItem)
                .where(
                        shipmentItem.shipment.id.eq(shipmentId),
                        shipmentItem.contractItem.id.eq(contractItemId),
                        shipmentItem.deleteYn.isFalse()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    // shipment id eq
    private BooleanExpression isShipmentIdEq(Long shipmentId) {
        return shipment.id.eq(shipmentId);
    }
    // shipmentItem id eq
    private BooleanExpression isShipmentItemIdEq(Long shipmentItemId) {
        return shipmentItem.id.eq(shipmentItemId);
    }
    // shipment deleteYn false 삭제여부
    private BooleanExpression isShipmentDeleteYnFalse() {
        return shipment.deleteYn.isFalse();
    }
    // shipmentItem deleteYn false 삭제여부
    private BooleanExpression isShipmentItemDeleteYnFalse() {
        return shipmentItem.deleteYn.isFalse();
    }
}