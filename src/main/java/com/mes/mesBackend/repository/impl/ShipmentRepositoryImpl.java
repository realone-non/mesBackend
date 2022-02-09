package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopShipmentResponse;
import com.mes.mesBackend.dto.response.ShipmentResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.ShipmentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 4-5. 출하등록
@RequiredArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QShipment shipment = QShipment.shipment;
    final QClient client = QClient.client;
    final QShipmentItem shipmentItem = QShipmentItem.shipmentItem;
    final QShipmentLot shipmentLot = QShipmentLot.shipmentLot;
    final QLotMaster lotMaster = QLotMaster.lotMaster;

    // 출하 등록 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponse> findShipmentResponsesByCondition(
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            Long userId
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ShipmentResponse.class,
                                shipment.id.as("id"),
                                shipment.shipmentNo.as("shipmentNo"),
                                client.id.as("clientId"),
                                client.clientCode.as("clientCode"),
                                client.clientName.as("clientName"),
                                shipment.shipmentDate.as("shipmentDate"),
                                shipment.clientManager.as("clientManager"),     // 거래처 담당자
                                shipment.note.as("note")
                        )
                )
                .from(shipment)
                .innerJoin(client).on(client.id.eq(shipment.client.id))
                .where(
                        isClientNameEq(clientId),
                        isShipmentDateBetween(fromDate, toDate),
                        isShipmentDeleteYnFalse()
                )
                .fetch();
    }

    // 출하 등록 단일 조회
    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentResponse> findShipmentResponseById(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .select(
                        Projections.fields(
                                ShipmentResponse.class,
                                shipment.id.as("id"),
                                shipment.shipmentNo.as("shipmentNo"),
                                shipment.client.id.as("clientId"),
                                shipment.client.clientCode.as("clientNo"),
                                shipment.client.clientName.as("clientName"),
                                shipment.shipmentDate.as("shipmentDate"),
                                shipment.clientManager.as("clientManager"),     // 거래처 담당자
                                shipment.note.as("note")
                        )
                )
                .from(shipment)
                .where(
                        isShipmentIdEq(id),
                        isShipmentDeleteYnFalse()
                )
                .fetchOne());
    }

    // 출하에 등록된 품목정보
    @Override
    public boolean existsByShipmentItemInShipment(Long shipmentId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(shipmentItem)
                .where(
                        shipmentItem.shipment.id.eq(shipmentId),
                        shipmentItem.deleteYn.isFalse()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    // 출하정보 리스트 조회
    @Override
    public List<PopShipmentResponse> findPopShipmentResponseByCondition(LocalDate fromDate, LocalDate toDate, String clientName, Boolean completionYn) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopShipmentResponse.class,
                                shipment.id.as("shipmentId"),
                                shipment.barcodeNumber.as("barcodeNumber"),
                                shipment.client.clientName.as("clientName"),
                                shipment.orderState.as("orderState")
                        )
                )
                .from(shipmentLot)
                .innerJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                .innerJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                .leftJoin(client).on(client.id.eq(shipment.client.id))
                .where(
                        isShipmentDateBetween(fromDate, toDate),
                        isClientNameContain(clientName),
                        shipmentLot.deleteYn.isFalse(),
                        shipmentItem.deleteYn.isFalse(),
                        shipment.deleteYn.isFalse()
                )
                .groupBy(shipment.id)
                .fetch();
    }

    // 출하 lot 가 등록되어 있는 출하품목만 가져옴
    @Override
    public List<ShipmentItem> findShipmentItemByShipmentId(Long shipmentId) {
        return jpaQueryFactory
                .select(shipmentItem)
                .from(shipmentLot)
                .innerJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                .innerJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                .where(
                        shipment.id.eq(shipmentId),
                        shipmentItem.deleteYn.isFalse(),
                        shipmentLot.deleteYn.isFalse(),
                        shipment.deleteYn.isFalse()
                )
                .fetch();
    }

    // 출하에 해당되는 출하 lot 정보의 모든 stockAmount
    @Override
    public List<Integer> findShipmentLotStockAmountByShipmentId(Long shipmentId) {
        return jpaQueryFactory
                .select(lotMaster.stockAmount)
                .from(shipmentLot)
                .innerJoin(shipmentItem).on(shipmentItem.id.eq(shipmentLot.shipmentItem.id))
                .innerJoin(shipment).on(shipment.id.eq(shipmentItem.shipment.id))
                .innerJoin(lotMaster).on(lotMaster.id.eq(shipmentLot.lotMaster.id))
                .where(
                        shipment.id.eq(shipmentId),
                        shipmentItem.deleteYn.isFalse(),
                        shipmentLot.deleteYn.isFalse(),
                        shipment.deleteYn.isFalse()
                )
                .fetch();
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
    // 거래처 id
    private BooleanExpression isClientNameEq(Long clientId) {
        return clientId != null ? shipment.client.id.eq(clientId) : null;
    }
    // 출하기간
    private BooleanExpression isShipmentDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? shipment.shipmentDate.between(fromDate, toDate) : null;
    }
    // 거래처 명 검색
    private BooleanExpression isClientNameContain(String clientName) {
        return clientName != null ? client.clientName.contains(clientName) : null;
    }
    // 완료 여부 조회
    private BooleanExpression isCompletionYn(Boolean completionYn) {
//        return completionYn != null ?
        return null;
    }
}
