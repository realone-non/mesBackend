//package com.mes.mesBackend.repository.impl;
//
//import com.mes.mesBackend.dto.response.ShipmentResponse;
//import com.mes.mesBackend.entity.QClient;
//import com.mes.mesBackend.entity.QShipment;
//import com.mes.mesBackend.entity.Shipment;
//import com.mes.mesBackend.repository.custom.ShipmentRepositoryCustom;
//import com.querydsl.core.types.Projections;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//// 4-5. 출하등록
//@RequiredArgsConstructor
//public class ShipmentRepositoryImpl implements ShipmentRepositoryCustom {
//
//    private final JPAQueryFactory jpaQueryFactory;
//    final QShipment shipment = QShipment.shipment;
//    final QClient client = QClient.client;
//
//    // 출하 등록 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
//    @Override
//    @Transactional(readOnly = true)
//    public List<ShipmentResponse> findShipmentResponsesByCondition(
//            String clientName,
//            LocalDate fromDate,
//            LocalDate toDate,
//            Long currencyId,
//            String userName
//    ) {
//        return jpaQueryFactory
//                .select(
//                        Projections.fields(
//                                ShipmentResponse.class,
//                                shipment.id.as("id"),
//                                shipment.shipmentNo.as("shipmentNo"),
//
//                        )
//                )
//                .from(shipment)
//                .where()
//                .fetch();
//
//    }
//
//    // 출하 등록 단일 조회
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<ShipmentResponse> findShipmentResponseById(Long id) {
//        return Optional.empty();
//    }
//
//    // 거래처 명
//    private BooleanExpression isClientNameContain(String clientName) {
//        return clientName != null ? shipment.client.clientName.contains(clientName) : null;
//    }
//    // 출하기간
//    private BooleanExpression isShipmentDateBetween(LocalDate fromDate, LocalDate toDate) {
//        return fromDate != null ? shipment.shipmentDate.between(fromDate, toDate) : null;
//    }
//    // 화폐 id
//    private BooleanExpression isCurrencyEq(Long currencyId) {
//        return currencyId != null ? shipment.currency.id.eq(currencyId) : null;
//    }
//    // 담당자 명
//    private BooleanExpression isUserNameContain(String userName) {
//        return userName != null ? shipment.user.korName.contains(userName) : null;
//    }
//    // 삭제 여부
//    private BooleanExpression isDeleteYnFalse() {
//        return shipment.deleteYn.isFalse();
//    }
//}
