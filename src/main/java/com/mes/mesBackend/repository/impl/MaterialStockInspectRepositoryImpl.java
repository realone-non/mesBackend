package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;
import com.mes.mesBackend.dto.response.MaterialStockInspectResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.MaterialStockInspectRepositoryCustom;
import com.mes.mesBackend.repository.custom.MaterialStockInspectRequestRepositoryCustom;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Case;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MaterialStockInspectRepositoryImpl implements MaterialStockInspectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QMaterialStockInspectRequest materialStockInspectRequest = QMaterialStockInspectRequest.materialStockInspectRequest;
    final QMaterialStockInspect materialStockInspect = QMaterialStockInspect.materialStockInspect;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QWareHouseType wareHouseType = QWareHouseType.wareHouseType;
    final QItem item = QItem.item;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QUser user = QUser.user;
    final QLotType lotType = QLotType.lotType1;

    //재고실사 전체 선택 /검색: 실사기간, 품목그룹
    public List<MaterialStockInspectResponse> findAllByCondition(Long requestId, LocalDate fromDate, LocalDate toDate, String itemAccount){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                MaterialStockInspectResponse.class,
                                materialStockInspect.id.as("id"),
                                materialStockInspect.itemNo.as("itemNo"),
                                materialStockInspect.itemName.as("itemName"),
                                materialStockInspect.lotType.as("lotType"),
                                materialStockInspect.lotNo.as("lotNo"),
                                materialStockInspect.dbAmount.as("dbAmount"),
                                materialStockInspect.inspectAmount.as("inspectAmount"),
                                materialStockInspect.dbAmount.subtract(materialStockInspect.inspectAmount).as("differenceAmount"),
                                materialStockInspect.itemAccount.as("itemAccount"),
                                user.id.as("userId"),
                                user.korName.as("userName"),
                                materialStockInspect.note.as("note")
                        )
                )
                .from(materialStockInspect)
                .innerJoin(materialStockInspectRequest).on(materialStockInspectRequest.id.eq(materialStockInspect.materialStockInspectRequest.id))
                .leftJoin(user).on(user.id.eq(materialStockInspect.user.id))
                .where(
                        requestNull(requestId),
                        dateNull(fromDate, toDate),
                        itemaccountNameNull(itemAccount),
                        materialStockInspect.deleteYn.eq(false)
                )
                .fetch();
    }

    public Optional<MaterialStockInspectResponse> findByIdAndDeleteYn(Long id){
        return Optional.ofNullable(
                jpaQueryFactory
                    .select(
                            Projections.fields(
                                    MaterialStockInspectResponse.class,
                                    materialStockInspect.id.as("id"),
                                    materialStockInspect.itemNo.as("itemNo"),
                                    materialStockInspect.itemName.as("itemName"),
                                    materialStockInspect.lotType.as("lotType"),
                                    materialStockInspect.lotNo.as("lotNo"),
                                    materialStockInspect.dbAmount.as("dbAmount"),
                                    materialStockInspect.inspectAmount.as("inspectAmount"),
                                    materialStockInspect.dbAmount.subtract(materialStockInspect.inspectAmount).as("differenceAmount"),
                                    materialStockInspect.itemAccount.as("itemAccount"),
                                    user.id.as("userId"),
                                    user.korName.as("userName"),
                                    materialStockInspect.note.as("note")
                        )
                )
                .from(materialStockInspect)
                .leftJoin(user).on(user.id.eq(materialStockInspect.user.id))
                .where(
                        materialStockInspect.id.eq(id),
                        materialStockInspect.deleteYn.eq(false)
                )
                .fetchOne()
        );
    }

    public List<MaterialStockInspect> findInspectFromDB(Long itemAccountId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                MaterialStockInspect.class,
                                lotMaster.wareHouse.wareHouseName.as("warehouse"),
                                lotMaster.item.itemAccount.account.as("itemAccount"),
                                lotMaster.item.itemNo.as("itemNo"),
                                lotMaster.item.itemName.as("itemName"),
                                lotMaster.item.lotType.lotType.as("lotType"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.stockAmount.as("dbAmount")
                        )
                )
                .from(lotMaster)
                .innerJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .innerJoin(item).on(item.id.eq(lotMaster.item.id))
                .innerJoin(wareHouse).on(wareHouse.id.eq((lotMaster.wareHouse.id)))
                .where(
                        lotMaster.deleteYn.eq(false),
                        itemaccountNull(itemAccountId)
                )
                .fetch();
    }

    private BooleanExpression dateNull(LocalDate startDate, LocalDate endDate){
        return startDate != null ? materialStockInspectRequest.inspectDate.between(startDate, endDate) : null;
    }

    private BooleanExpression requestNull(Long requestId){
        return requestId != null ? materialStockInspect.materialStockInspectRequest.id.eq(requestId) : null;
    }

    private BooleanExpression itemaccountNull(Long itemAccountId){
        return itemAccountId != null ? lotMaster.item.itemAccount.id.eq(itemAccountId) : null;
    }
    private BooleanExpression itemaccountNameNull(String itemAccount){
        return itemAccount != null ? materialStockInspect.itemAccount.eq(itemAccount) : null;
    }
}
