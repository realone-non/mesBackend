package com.mes.mesBackend.repository.impl;

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
    final QUser user = QUser.user;
    final QCurrency currency = QCurrency.currency1;
    final QShipmentItem shipmentItem = QShipmentItem.shipmentItem;
    final QContractItem contractItem = QContractItem.contractItem;
    final QContract contract = QContract.contract;
    final QUnit unit = QUnit.unit;
    final QItem item = QItem.item;

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
                                client.clientCode.as("clientNo"),
                                client.clientName.as("clientName"),
                                shipment.shipmentDate.as("shipmentDate"),
                                shipment.clientManager.as("clientManager"),     // 거래처 담당자
                                shipment.note.as("note")
                        )
                )
                .from(shipment)
                .innerJoin(client).on(client.id.eq(shipment.client.id))
                .where(
                        isClientNameContain(clientId),
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
    private BooleanExpression isClientNameContain(Long clientId) {
        return clientId != null ? shipment.client.id.eq(clientId) : null;
    }
    // 출하기간
    private BooleanExpression isShipmentDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? shipment.shipmentDate.between(fromDate, toDate) : null;
    }
}
