package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.MaterialStockInspectRequestRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MaterialStockInspectRequestRepositoryImpl implements MaterialStockInspectRequestRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QMaterialStockInspectRequest materialStockInspectRequest = QMaterialStockInspectRequest.materialStockInspectRequest;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QWareHouseType wareHouseType = QWareHouseType.wareHouseType;
    final QItem item = QItem.item;
    final QItemAccount itemAccount = QItemAccount.itemAccount;

    //재고실사의뢰 전체 선택 /검색: 실사기간
    public List<MaterialStockInspectRequestResponse> findAllByCondition(LocalDate fromDate, LocalDate toDate){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                MaterialStockInspectRequestResponse.class,
                                materialStockInspectRequest.id.as("id"),
                                materialStockInspectRequest.inspectDate.as("inspectDate"),
                                materialStockInspectRequest.note.as("note"),
                                wareHouse.id.as("warehouseId"),
                                wareHouse.wareHouseName.as("warehouse"),
                                wareHouseType.name.as("warehouseType"),
                                itemAccount.id.as("itemAccountId"),
                                itemAccount.account.as("itemAccount"),
                                materialStockInspectRequest.inspectionType.as("stockInspectionType")
                        )
                )
                .from(materialStockInspectRequest)
                .innerJoin(wareHouse).on(wareHouse.id.eq(materialStockInspectRequest.wareHouse.id))
                .innerJoin(wareHouseType).on(wareHouseType.id.eq(wareHouse.wareHouseType.id))
                .innerJoin(itemAccount).on(itemAccount.id.eq(materialStockInspectRequest.itemAccount.id))
                .where(
                        dateNull(fromDate, toDate),
                        materialStockInspectRequest.deleteYn.eq(false)
                )
                .fetch();
    }

    public Optional<MaterialStockInspectRequestResponse> findByIdAndDeleteYn(Long id){
        return Optional.ofNullable(
                jpaQueryFactory
                    .select(
                            Projections.fields(
                                MaterialStockInspectRequestResponse.class,
                                materialStockInspectRequest.id.as("id"),
                                materialStockInspectRequest.inspectDate.as("inspectDate"),
                                materialStockInspectRequest.note.as("note"),
                                wareHouse.id.as("warehouseId"),
                                wareHouse.wareHouseName.as("warehouse"),
                                wareHouseType.name.as("warehouseType"),
                                itemAccount.id.as("itemAccountId"),
                                itemAccount.account.as("itemAccount"),
                                materialStockInspectRequest.inspectionType.as("stockInspectionType")
                        )
                )
                .from(materialStockInspectRequest)
                .innerJoin(wareHouse).on(wareHouse.id.eq(materialStockInspectRequest.wareHouse.id))
                .innerJoin(wareHouseType).on(wareHouseType.id.eq(wareHouse.wareHouseType.id))
                .innerJoin(itemAccount).on(itemAccount.id.eq(materialStockInspectRequest.itemAccount.id))
                .where(
                        materialStockInspectRequest.id.eq(id),
                        materialStockInspectRequest.deleteYn.eq(false)
                )
                .fetchOne()
        );
    }

    private BooleanExpression dateNull(LocalDate startDate, LocalDate endDate){
        return startDate != null ? materialStockInspectRequest.inspectDate.between(startDate, endDate) : null;
    }
}
