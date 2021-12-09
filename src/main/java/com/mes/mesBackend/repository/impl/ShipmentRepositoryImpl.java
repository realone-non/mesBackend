package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.QShipment;
import com.mes.mesBackend.entity.Shipment;
import com.mes.mesBackend.repository.custom.ShipmentRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepositoryCustom {
    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    private final JPAQueryFactory jpaQueryFactory;
    private final QShipment shipment = QShipment.shipment;

    @Override
    public List<Shipment> findAllCondition(
            String clientName,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            String userName
    ) {
        return jpaQueryFactory
                .selectFrom(shipment)
                .where(
                        isClientNameContain(clientName),
                        isShipmentDateBetween(fromDate, toDate),
                        isCurrencyEq(currencyId),
                        isUserNameContain(userName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 거래처 명
    private BooleanExpression isClientNameContain(String clientName) {
        return clientName != null ? shipment.client.clientName.contains(clientName) : null;
    }
    // 출하기간
    private BooleanExpression isShipmentDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? shipment.shipmentDate.between(fromDate, toDate) : null;
    }
    // 화폐 id
    private BooleanExpression isCurrencyEq(Long currencyId) {
        return currencyId != null ? shipment.currency.id.eq(currencyId) : null;
    }
    // 담당자 명
    private BooleanExpression isUserNameContain(String userName) {
        return userName != null ? shipment.user.korName.contains(userName) : null;
    }
    // 삭제 여부
    private BooleanExpression isDeleteYnFalse() {
        return shipment.deleteYn.isFalse();
    }
}
