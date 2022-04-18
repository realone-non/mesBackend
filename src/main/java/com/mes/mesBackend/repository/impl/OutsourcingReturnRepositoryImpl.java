package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.OutsourcingReturnResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.OutsourcingReturnRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.loader.hql.QueryLoader;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OutsourcingReturnRepositoryImpl implements OutsourcingReturnRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QOutsourcingReturn outsourcingReturn = QOutsourcingReturn.outsourcingReturn;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QClient client = QClient.client;
    final QItem item = QItem.item;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QLotType lotType = QLotType.lotType1;

    //외주반품 전체 조회 검색 조건:외주처, 품목, 반품기간
    @Transactional(readOnly = true)
    public List<OutsourcingReturnResponse> findAllByCondition(Long clientId, String itemNoAndItemName, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingReturnResponse.class,
                                outsourcingReturn.id.as("id"),
                                client.clientName.as("clientName"),
                                lotMaster.id.as("lotMasterId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.lotNo.as("lotNo"),
                                lotType.lotType.as("lotType"),
                                outsourcingReturn.returnDate.as("returnDate"),
                                lotMaster.stockReturnAmount.as("stockReturnAmount"),
                                lotMaster.stockAmount.as("stockAmount"),
                                lotMaster.badItemReturnAmount.as("badItemReturnAmount"),
                                lotMaster.badItemAmount.as("badAmount"),
                                wareHouse.wareHouseName.as("wareHouse"),
                                outsourcingReturn.note.as("note"),
                                outsourcingReturn.returnDivision.as("returnDivision")
                        )
                )
                .from(outsourcingReturn)
                .leftJoin(lotMaster).on(lotMaster.id.eq(outsourcingReturn.lotMaster.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .where(
                        clientNull(clientId),
                        isItemNoAndItemNameContain(itemNoAndItemName),
                        dateNull(startDate, endDate),
                        outsourcingReturn.useYn.eq(true),
                        outsourcingReturn.deleteYn.eq(false)
                )
                .fetch();
    }

    //외주반품 단일 조회
    public Optional<OutsourcingReturnResponse> findReturnByIdAndDeleteYnAndUseYn(Long id){
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        OutsourcingReturnResponse.class,
                                        outsourcingReturn.id.as("id"),
                                        client.clientName.as("clientName"),
                                        lotMaster.id.as("lotMasterId"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        lotMaster.lotNo.as("lotNo"),
                                        lotType.lotType.as("lotType"),
                                        outsourcingReturn.returnDate.as("returnDate"),
                                        lotMaster.stockReturnAmount.as("stockReturnAmount"),
                                        lotMaster.stockAmount.as("stockAmount"),
                                        lotMaster.badItemReturnAmount.as("badItemReturnAmount"),
                                        lotMaster.badItemAmount.as("badAmount"),
                                        wareHouse.wareHouseName.as("wareHouse"),
                                        outsourcingReturn.note.as("note"),
                                        outsourcingReturn.returnDivision.as("returnDivision")
                                )
                        )
                        .from(outsourcingReturn)
                        .leftJoin(lotMaster).on(lotMaster.id.eq(outsourcingReturn.lotMaster.id))
                        .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                        .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                        .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                        .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                        .where(
                                outsourcingReturn.id.eq(id),
                                outsourcingReturn.useYn.eq(true),
                                outsourcingReturn.deleteYn.eq(false)
                        )
                        .fetchOne()
        );
    }


    private BooleanExpression clientNull(Long clientId){
//        return clientId != null ? request.bomMaster.item.manufacturer.id.eq(clientId) : null;
        return clientId != null ? client.id.eq(clientId) : null;
    }

    private BooleanExpression itemNull(Long itemId){
        return itemId != null ? item.id.eq(itemId) : null;
    }

    // 품번|품명
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName).or(item.itemName.contains(itemNoAndName)) : null;
    }

    private  BooleanExpression dateNull(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return outsourcingReturn.returnDate.between(startDate, endDate);
        } else if (startDate != null) {
            return outsourcingReturn.returnDate.after(startDate).or(outsourcingReturn.returnDate.eq(startDate));
        } else if (endDate != null) {
            return outsourcingReturn.returnDate.before(endDate).or(outsourcingReturn.returnDate.eq(endDate));
        } else {
            return null;
        }
    }
}
