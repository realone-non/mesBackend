package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ShipmentReturnLotResponse;
import com.mes.mesBackend.dto.response.ShipmentReturnResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.ShipmentReturnRepositoryCustom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;

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
                                shipmentReturn.note.as("note"),
                                shipmentLot.id.as("shipmentLotId")
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
                                        shipmentReturn.note.as("note"),
                                        shipmentLot.id.as("shipmentLotId")
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

    // clientId 로 shipmentLot 조회
    @Override
    public Page<ShipmentReturnLotResponse> findShipmentReturnLotResponsesByClientId(
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {
        QueryResults<ShipmentReturnLotResponse> results = jpaQueryFactory
                .select(
                        Projections.fields(
                                ShipmentReturnLotResponse.class,
                                shipmentLot.id.as("shipmentLotId"),
                                shipment.shipmentNo.as("shipmentNo"),
                                client.clientName.as("clientName"),
                                client.clientCode.as("clientCode"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.lotNo.as("lotNo"),
                                lotType.lotType.as("lotType"),
                                lotMaster.shipmentAmount.as("shipmentAmount"),
                                item.standard.as("standard")
                        )
                )
                .from(shipmentLot)
                .leftJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                .leftJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                .leftJoin(client).on(client.id.eq(shipment.client.id))
                .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .where(
                        client.id.eq(clientId),
                        shipmentLot.deleteYn.isFalse(),
                        shipmentItem.deleteYn.isFalse(),
                        shipment.deleteYn.isFalse(),
                        shipment.orderState.eq(COMPLETION),
                        isShipmentDateBetween(fromDate, toDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public ShipmentReturnLotResponse findShipmentReturnLotResponseByClientId(Long shipmentId) {
        return jpaQueryFactory
                        .select(
                                Projections.fields(
                                        ShipmentReturnLotResponse.class,
                                        shipmentLot.id.as("shipmentLotId"),
                                        shipment.shipmentNo.as("shipmentNo"),
                                        client.clientName.as("clientName"),
                                        client.clientCode.as("clientCode"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        lotMaster.lotNo.as("lotNo"),
                                        lotType.lotType.as("lotType"),
                                        lotMaster.shipmentAmount.as("shipmentAmount"),
                                        item.standard.as("standard")
                                )
                        )
                        .from(shipmentLot)
                        .leftJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                        .leftJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                        .leftJoin(client).on(client.id.eq(shipment.client.id))
                        .leftJoin(contractItem).on(contractItem.id.eq(shipmentItem.contractItem.id))
                        .leftJoin(item).on(item.id.eq(contractItem.item.id))
                        .leftJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                        .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                        .where(
                                shipmentLot.shipmentItem.shipment.id.eq(shipmentId),
                                shipmentLot.deleteYn.isFalse(),
                                shipment.deleteYn.isFalse(),
                                shipmentLot.deleteYn.isFalse()
                        )
                        .fetchOne();
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
        if (fromDate != null && toDate != null) {
            return shipmentReturn.returnDate.between(fromDate, toDate);
        } else if (fromDate != null) {
            return shipmentReturn.returnDate.after(fromDate);
        } else if (toDate != null) {
            return shipmentReturn.returnDate.before(toDate);
        } else {
            return null;
        }
    }

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

    private BooleanExpression isDeleteYnFalse() {
        return shipmentReturn.deleteYn.isFalse();
    }
}
