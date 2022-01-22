package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ShipmentLotInfoResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.custom.ShipmentLotRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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
                .fetch();
    }
}
