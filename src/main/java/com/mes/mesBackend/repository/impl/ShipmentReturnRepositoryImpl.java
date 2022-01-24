package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ShipmentReturnResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.ShipmentReturnRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 4-6. 출하반품 등록
@RequiredArgsConstructor
public class ShipmentReturnRepositoryImpl implements ShipmentReturnRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QShipment shipment = QShipment.shipment;
    final QShipmentItem shipmentItem = QShipmentItem.shipmentItem;
    final QShipmentLot shipmentLot = QShipmentLot.shipmentLot;
    final QShipmentReturn shipmentReturn = QShipmentReturn.shipmentReturn;
    final QItem item = QItem.item;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QClient client = QClient.client;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QContractItem contractItem = QContractItem.contractItem;
    final QLotType lotType = QLotType.lotType1;


    // 출하반품 리스트 검색 조회, 검색조건: 거래처 id, 품번|품명, 반품기간 fromDate~toDate
    @Override
    public List<ShipmentReturnResponse> findShipmentReturnResponsesByCondition(
            Long clientId,
            String itemNoAndItemName,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ShipmentReturnResponse.class,
                                shipmentReturn.id.as("id"),
                                shipment.shipmentNo.as("shipmentNo"),
                                client.clientCode.as("clientCode"),
                                client.clientName.as("clientName"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                item.standard.as("itemStandard"),
                                lotMaster.id.as("lotMasterId"),
                                lotMaster.lotNo.as("lotNo"),
                                lotType.lotType.as("lotType"),
                                shipmentReturn.returnDate.as("returnDate"),
                                shipmentReturn.returnAmount.as("returnAmount"),
                                lotMaster.shipmentAmount.as("shipmentAmount"),
                                wareHouse.id.as("warehouseId"),
                                wareHouse.wareHouseName.as("warehouseName"),
                                shipmentReturn.note.as("note")
                        )
                )
                .from(shipmentReturn)
                .leftJoin(shipmentLot).on(shipmentLot.id.eq(shipmentReturn.shipmentLot.id))
                .leftJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                .leftJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                .leftJoin(client).on(client.id.eq(shipment.client.id))
                .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(shipmentReturn.wareHouse.id))
                .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .where(
                        isClientIdEq(clientId),
                        isItemNoAndItemNameContain(itemNoAndItemName),
                        isReturnDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 출하반품 단일 조회
    @Override
    public Optional<ShipmentReturnResponse> findShipmentReturnResponseByIdAndDeleteYnFalse(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        ShipmentReturnResponse.class,
                                        shipmentReturn.id.as("id"),
                                        shipment.shipmentNo.as("shipmentNo"),
                                        client.clientCode.as("clientCode"),
                                        client.clientName.as("clientName"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        item.standard.as("itemStandard"),
                                        lotMaster.id.as("lotMasterId"),
                                        lotMaster.lotNo.as("lotNo"),
                                        lotType.lotType.as("lotType"),
                                        shipmentReturn.returnDate.as("returnDate"),
                                        shipmentReturn.returnAmount.as("returnAmount"),
                                        lotMaster.shipmentAmount.as("shipmentAmount"),
                                        wareHouse.id.as("warehouseId"),
                                        wareHouse.wareHouseName.as("warehouseName"),
                                        shipmentReturn.note.as("note")
                                )
                        )
                        .from(shipmentReturn)
                        .leftJoin(shipmentLot).on(shipmentLot.id.eq(shipmentReturn.shipmentLot.id))
                        .leftJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                        .leftJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                        .leftJoin(client).on(client.id.eq(shipment.client.id))
                        .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                        .leftJoin(item).on(item.id.eq(contractItem.item.id))
                        .leftJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                        .leftJoin(wareHouse).on(wareHouse.id.eq(shipmentReturn.wareHouse.id))
                        .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                        .where(
                                shipmentReturn.id.eq(id),
                                isDeleteYnFalse()
                        )
                        .fetchOne()
        );
    }

    // 출하 LOT 정보 id 로 등록된 모든 반품수량 가져옴
    @Override
    public List<Integer> findReturnAmountsByShipmentLotId(Long shipmentLotId) {
        return jpaQueryFactory
                .select(shipmentReturn.returnAmount)
                .from(shipmentReturn)
                .where(
                        shipmentReturn.shipmentLot.id.eq(shipmentLotId),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 품번|품명
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName).or(item.itemName.contains(itemNoAndName)) : null;
    }

    // 거래처 조회
    private BooleanExpression isClientIdEq(Long clientId) {
        return clientId != null ? client.id.eq(clientId) : null;
    }

    // 반품일시 조회
    private BooleanExpression isReturnDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? shipmentReturn.returnDate.between(fromDate, toDate) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return shipmentReturn.deleteYn.isFalse();
    }
}
