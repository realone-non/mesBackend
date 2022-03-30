package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.ProductionPerformanceRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

// 8-6. 생산실적 관리
@RequiredArgsConstructor
public class ProductionPerformanceRepositoryImpl implements ProductionPerformanceRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QProductionPerformance productionPerformance = QProductionPerformance.productionPerformance;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;
    final QContract contract = QContract.contract;
    final QContractItem contractItem = QContractItem.contractItem;
    final QItem item = QItem.item;
    final QUser user = QUser.user;
    final QItemGroup itemGroup = QItemGroup.itemGroup;
    final QClient client = QClient.client;
    final QLotMaster lotMaster = QLotMaster.lotMaster;

    // 생산실적 리스트 조회, 검색조건: 조회기간 fromDate~toDate, 품목그룹 id, 품명|품번
    @Override
    public List<ProductionPerformanceResponse> findProductionPerformanceResponsesByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            Long itemGroupId,
            String itemNoOrItemName
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ProductionPerformanceResponse.class,
                                productionPerformance.id.as("id"),
                                contract.contractNo.as("contractNo"),
                                produceOrder.produceOrderNo.as("produceOrderNo"),
                                client.clientName.as("clientName"),
                                item.itemName.as("itemName"),
                                contractItem.contract.periodDate.as("periodDate"),
                                user.korName.as("korName"),
                                contractItem.amount.as("contractItemAmount"),
//                                productionPerformance.productionAmount.as("productionAmount"),
                                productionPerformance.materialInput.as("materialInput"),
                                productionPerformance.materialMixing.as("materialMixing"),
                                productionPerformance.filling.as("filling"),
                                productionPerformance.capAssembly.as("capAssembly"),
                                productionPerformance.labeling.as("labeling"),
                                productionPerformance.packaging.as("packaging"),
                                productionPerformance.shipment.as("shipment"),
                                item.inputUnitPrice.as("unitPrice"),
                                item.inputUnitPrice.multiply(contractItem.amount).as("price"),
                                lotMaster.createdAmount.as("productionAmount")
                        )
                )
                .from(productionPerformance)
                .innerJoin(workOrderDetail).on(workOrderDetail.id.eq(productionPerformance.workOrderDetail.id))
                .leftJoin(produceOrder).on(produceOrder.id.eq(workOrderDetail.produceOrder.id))
                .leftJoin(contract).on(contract.id.eq(produceOrder.contract.id))
                .leftJoin(contractItem).on(contractItem.id.eq(produceOrder.contractItem.id))
                .leftJoin(item).on(item.id.eq(contractItem.item.id))
                .leftJoin(user).on(user.id.eq(workOrderDetail.user.id))
                .leftJoin(client).on(client.id.eq(contract.client.id))
                .leftJoin(itemGroup).on(itemGroup.id.eq(item.itemGroup.id))
                .leftJoin(lotMaster).on(lotMaster.id.eq(productionPerformance.lotMaster.id))
                .where(
                        isSelectDate(fromDate, toDate),
                        isItemGroupIdEq(itemGroupId),
                        isItemNoAndItemNameContain(itemNoOrItemName),
                        isProductionPerformanceDeleteYnFalse(),
                        isWorkOrderDetailDeleteYnFalse()
                )
                .orderBy(productionPerformance.createdDate.desc())
                .fetch();
    }

    // 검색조건: 조회기간 fromDate~toDate
    private BooleanExpression isSelectDate(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ?
                productionPerformance.materialInput.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0))
                        .or(productionPerformance.materialMixing.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)))
                        .or(productionPerformance.filling.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)))
                        .or(productionPerformance.capAssembly.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)))
                        .or(productionPerformance.labeling.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)))
                        .or(productionPerformance.packaging.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)))
                        .or(productionPerformance.shipment.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)))
                : null;
    }

    @Override
    public Optional<ProductionPerformance> findByProduceOrderId(Long produceOrderId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(productionPerformance)
                        .from(productionPerformance)
                        .where(
                                productionPerformance.workOrderDetail.produceOrder.id.eq(produceOrderId),
                                productionPerformance.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    // 품목그룹 id
    private BooleanExpression isItemGroupIdEq(Long itemGroupId){
        return itemGroupId != null ? itemGroup.id.eq(itemGroupId) : null;
    }
    // 품명|품번
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndItemName) {
        return itemNoAndItemName != null ? item.itemNo.contains(itemNoAndItemName).or(item.itemName.contains(itemNoAndItemName)) : null;
    }
    private BooleanExpression isProductionPerformanceDeleteYnFalse() {
        return productionPerformance.deleteYn.isFalse();
    }
    private BooleanExpression isWorkOrderDetailDeleteYnFalse() {
        return workOrderDetail.deleteYn.isFalse();
    }
}
