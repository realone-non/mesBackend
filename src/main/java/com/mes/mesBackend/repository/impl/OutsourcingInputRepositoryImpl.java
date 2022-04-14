package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.OutsourcingInputLOTResponse;
import com.mes.mesBackend.dto.response.OutsourcingInputResponse;
import com.mes.mesBackend.dto.response.OutsourcingStatusResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.OutsourcingInputRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.OUTSOURCING_INPUT;

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
    final QLotType lotType = QLotType.lotType1;


    //외주 입고 정보 검색 조건:외주처, 품목, 입고기간
    public List<OutsourcingInputResponse> findAllByCondition(Long clientId, String itemNo, String itemName, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingInputResponse.class,
                                request.id.as("id"),
                                client.clientName.as("clientName"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                request.inputTestYn.as("inputTestYn"),
                                request.note.as("note"),
                                request.productionAmount.as("productionAmount")
                        )
                )
                .from(request)
                .leftJoin(item).on(item.id.eq(request.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .where(
                        clientNull(clientId),
                        isItemNoContaining(itemNo),
                        isItemNameContaining(itemName),
                        dateNull(startDate, endDate),
                        request.deleteYn.isFalse()
                )
                .orderBy(request.createdDate.desc())
                .fetch();
    }


    //외주생산의뢰 단일 조회
    public Optional<OutsourcingInputResponse> findInputByIdAndDeleteYnAndUseYn(Long id){
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        OutsourcingInputResponse.class,
                                        request.id.as("id"),
                                        client.clientName.as("clientName"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        request.inputTestYn.as("inputTestYn"),
                                        request.note.as("note"),
                                        request.productionAmount.as("productionAmount")
                                )
                        )
                        .from(request)
                        .leftJoin(item).on(item.id.eq(request.item.id))
                        .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                        .where(
                                input.id.eq(id),
                                request.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    //외주생산의뢰 ID로 외주입고 조회
    @Transactional(readOnly = true)
    public List<OutSourcingInput> findAllByRequestId(Long id){
        return jpaQueryFactory
                .selectFrom(input)
                .where(
                        input.deleteYn.isFalse(),
                        input.productionRequest.id.eq(id))
                .orderBy(input.createdDate.desc())
                .fetch();
    }

    //외주생산의뢰 ID로 생산수량 조회
    @Transactional(readOnly = true)
    public Integer findAmountByRequestId(Long requestId){
        return jpaQueryFactory
                .select(
                    request.productionAmount.as("amount")
                )
                .from(request)
                .where(
                        request.id.eq(requestId),
//                        request.useYn.isTrue(),
                        request.deleteYn.isFalse()
                )
                .fetchOne();
    }

    //외주재고현황 전체 조회 검색 조건:외주처, 품목, 의뢰기간
    @Transactional(readOnly = true)
    public List<OutsourcingStatusResponse> findStatusByCondition(Long clientId, String itemNo, String itemName){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingStatusResponse.class,
                                client.clientName.as("clientName"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.inputAmount.sum().as("inputAmount"),
                                lotMaster.stockReturnAmount.sum().as("stockReturnAmount"),
                                lotMaster.badItemAmount.sum().as("badItemAmount"),
                                lotMaster.badItemReturnAmount.sum().as("badItemReturnAmount"),
                                lotMaster.stockAmount.sum().as("stockAmount")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .where(
                        clientNull(clientId),
                        isItemNoContaining(itemNo),
                        isItemNameContaining(itemName),
                        lotMaster.useYn.eq(true),
                        lotMaster.deleteYn.eq(false),
                        lotMaster.enrollmentType.eq(OUTSOURCING_INPUT)
                )
                .groupBy(item.itemName)
                .fetch();
    }

    // item조회
    @Transactional(readOnly = true)
    public Long findItemIdByInputId(Long inputId) {
        return jpaQueryFactory
                .select(item.id)
                .from(input)
                .innerJoin(request).on(request.id.eq(input.productionRequest.id))
//                .innerJoin(master).on(master.id.eq(request.bomMaster.id))
                .innerJoin(item).on(item.id.eq(master.item.id))
                .where(
                    input.id.eq(inputId)
                )
                .fetchOne();
    }

    // 외주입고 lot 정보 리스트 조회
    @Override
    public List<OutsourcingInputLOTResponse> findOutsourcingInputLotResponsesByRequestId(Long requestId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingInputLOTResponse.class,
                                input.id.as("id"),
                                lotMaster.id.as("lotId"),
                                lotType.lotType.as("lotType"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.createdAmount.as("inputAmount"),
                                lotMaster.item.testType.as("testRequestType"),
                                input.inputTestYn.as("inputTestYn"),
                                wareHouse.id.as("warehouseId"),
                                wareHouse.wareHouseName.as("warehouseName"),
                                input.note.as("note")
                        )
                )
                .from(lotMaster)
                .leftJoin(input).on(input.id.eq(lotMaster.outSourcingInput.id))
                .leftJoin(request).on(request.id.eq(input.productionRequest.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(input.inputWareHouse.id))
                .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .where(
                        request.id.eq(requestId),
                        input.deleteYn.isFalse(),
                        lotMaster.deleteYn.isFalse(),
                        lotMaster.enrollmentType.eq(OUTSOURCING_INPUT)
                )
                .orderBy(lotMaster.createdDate.desc())
                .fetch();
    }

    // 외주입고 lot 정보 단일 조회
    @Override
    public Optional<OutsourcingInputLOTResponse> findOutsourcingInputLotResponseByRequestId(Long requestId, Long inputId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        OutsourcingInputLOTResponse.class,
                                        input.id.as("id"),
                                        lotMaster.id.as("lotId"),
                                        lotType.lotType.as("lotType"),
                                        lotMaster.lotNo.as("lotNo"),
                                        lotMaster.createdAmount.as("inputAmount"),
                                        lotMaster.item.testType.as("testRequestType"),
                                        input.inputTestYn.as("inputTestYn"),
                                        wareHouse.id.as("warehouseId"),
                                        wareHouse.wareHouseName.as("warehouseName"),
                                        input.note.as("note")
                                )
                        )
                        .from(lotMaster)
                        .leftJoin(input).on(input.id.eq(lotMaster.outSourcingInput.id))
                        .leftJoin(request).on(request.id.eq(input.productionRequest.id))
                        .leftJoin(wareHouse).on(wareHouse.id.eq(input.inputWareHouse.id))
                        .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                        .where(
                                request.id.eq(requestId),
                                input.id.eq(inputId),
                                input.deleteYn.isFalse(),
                                lotMaster.deleteYn.isFalse(),
                                lotMaster.enrollmentType.eq(OUTSOURCING_INPUT)
                        )
                        .fetchOne()
        );
    }

    // 외주생산의뢰로 등록된 외주입고가 존재하는지여부
    @Override
    public boolean existsByOutsourcingProductionRequestId(Long requestId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(input)
                .where(
                        input.productionRequest.id.eq(requestId),
                        input.deleteYn.isFalse()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    // 외주생산의뢰 id 로 외주입고 list 조회
    @Override
    public List<OutSourcingInput> findOutsourcingInputByRequestId(Long requestId) {
        return jpaQueryFactory
                .selectFrom(input)
                .where(
                        input.productionRequest.id.eq(requestId),
                        input.deleteYn.isFalse()
                )
                .orderBy(input.createdDate.desc())
                .fetch();
    }

    private BooleanExpression clientNull(Long clientId){
//        return clientId != null ? request.bomMaster.item.manufacturer.id.eq(clientId) : null;
        return clientId != null ? client.id.eq(clientId) : null;
    }

    private BooleanExpression itemNull(Long itemId){
        return itemId != null ? item.id.eq(itemId) : null;
    }

    private  BooleanExpression dateNull(LocalDate startDate, LocalDate endDate){
        if (startDate != null && endDate != null) {
            return request.productionDate.between(startDate, endDate);
        } else if (startDate != null) {
            return request.productionDate.after(startDate);
        } else if (endDate != null) {
            return request.productionDate.before(endDate);
        } else {
            return null;
        }
    }

    // 품번 검색
    private BooleanExpression isItemNoContaining(String itemNo) {
        return itemNo !=  null ? item.itemNo.contains(itemNo) : null;
    }

    // 품명 검색
    private BooleanExpression isItemNameContaining(String itemName) {
        return itemName != null ? item.itemName.contains(itemName) : null;
    }
}
