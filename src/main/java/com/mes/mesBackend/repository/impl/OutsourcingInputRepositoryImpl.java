package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.OutsourcingInputResponse;
import com.mes.mesBackend.dto.response.OutsourcingStatusResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.repository.custom.OutsourcingInputRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OutsourcingInputRepositoryImpl implements OutsourcingInputRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QOutSourcingInput input = QOutSourcingInput.outSourcingInput;
    final QOutSourcingProductionRequest request = QOutSourcingProductionRequest.outSourcingProductionRequest;
    final QBomMaster master = QBomMaster.bomMaster;
    final QItem item = QItem.item;
    final QClient client = QClient.client;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QLotMaster lotMaster = QLotMaster.lotMaster;

    //외주 입고 정보 검색 조건:외주처, 품목, 입고기간
    public List<OutsourcingInputResponse> findAllByCondition(Long clientId, Long itemId, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingInputResponse.class,
                                input.id.as("id"),
                                client.clientName.as("clientName"),
                                request.id.as("requestNo"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                input.inputDate.as("inputDate"),
                                input.noInputAmount.as("noInputAmount"),
                                input.inputAmount.as("inputAmount"),
                                wareHouse.wareHouseName.as("warehouseName"),
                                input.testRequestType.as("testRequestType"),
                                input.note.as("note")
                        )
                )
                .from(input)
                .leftJoin(wareHouse).on(wareHouse.id.eq(input.inputWareHouse.id))
                .leftJoin(request).on(request.id.eq(input.outSourcingInput.id))
                .leftJoin(master).on(master.id.eq(request.bomMaster.id))
                .leftJoin(item).on(item.id.eq(master.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .where(
                        clientNull(clientId),
                        itemNull(itemId),
                        dateNull(startDate, endDate),
                        input.useYn.eq(true),
                        input.deleteYn.eq(false)
                )
                .fetch();
    }

    //외주생산의뢰 단일 조회
    public Optional<OutsourcingInputResponse> findInputByIdAndDeleteYnAndUseYn(Long id){
        return Optional.ofNullable(
                jpaQueryFactory
                    .select(
                            Projections.fields(
                                OutsourcingInputResponse.class,
                                input.id.as("id"),
                                client.clientName.as("clientName"),
                                request.id.as("requestNo"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                input.inputDate.as("inputDate"),
                                input.noInputAmount.as("noInputAmount"),
                                input.inputAmount.as("inputAmount"),
                                wareHouse.wareHouseName.as("warehouseName"),
                                input.testRequestType.as("testRequestType"),
                                input.note.as("note")
                        )
                )
                .from(input)
                .leftJoin(wareHouse).on(wareHouse.id.eq(input.inputWareHouse.id))
                .leftJoin(request).on(request.id.eq(input.outSourcingInput.id))
                .leftJoin(master).on(master.id.eq(request.bomMaster.id))
                .leftJoin(item).on(item.id.eq(master.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .where(
                        input.id.eq(id),
                        input.useYn.eq(true),
                        input.deleteYn.eq(false)
                )
                .fetchOne()
            );
    }

    //외주재고현황 전체 조회 검색 조건:외주처, 품목, 의뢰기간
    @Transactional(readOnly = true)
    public List<OutsourcingStatusResponse> findStatusByCondition(Long clientId, Long itemId, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingStatusResponse.class,
                                client.clientName.as("clientName"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.inputAmount.count().as("inputAmount"),
                                lotMaster.stockReturnAmount.count().as("stockReturnAmount"),
                                lotMaster.badItemAmount.count().as("badItemAmount"),
                                lotMaster.badItemReturnAmount.count().as("badItemReturnAmount"),
                                lotMaster.stockAmount.count().as("stockAmount")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .where(
                        clientNull(clientId),
                        itemNull(itemId),
                        dateNull(startDate, endDate),
                        lotMaster.useYn.eq(true),
                        lotMaster.deleteYn.eq(false),
                        lotMaster.enrollmentType.eq(EnrollmentType.OUTSOURCING_INPUT)
                )
                .groupBy(client.clientName, item.itemNo, item.itemName)
                .fetch();
    }

    // item조회
    @Transactional(readOnly = true)
    public Long findItemIdByInputId(Long inputId) {
        return jpaQueryFactory
                .select(item.id)
                .from(input)
                .innerJoin(request).on(request.id.eq(input.outSourcingInput.id))
                .innerJoin(master).on(master.id.eq(request.bomMaster.id))
                .innerJoin(item).on(item.id.eq(master.item.id))
                .where(
                    input.id.eq(inputId)
                )
                .fetchOne();
    }

    private BooleanExpression clientNull(Long clientId){
//        return clientId != null ? request.bomMaster.item.manufacturer.id.eq(clientId) : null;
        return clientId != null ? client.id.eq(clientId) : null;
    }

    private BooleanExpression itemNull(Long itemId){
        return itemId != null ? item.id.eq(itemId) : null;
    }

    private  BooleanExpression dateNull(LocalDate startDate, LocalDate endDate){
        return startDate != null ? request.productionDate.between(startDate, endDate) : null;
    }
}
