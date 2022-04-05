package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.custom.LotMasterRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.OUTSOURCING_INPUT;
import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PRODUCTION;
import static com.mes.mesBackend.entity.enumeration.GoodsType.PRODUCT;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.EXHAUST;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.FAMILY;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.DUMMY_LOT;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.REAL_LOT;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.PACKAGING;

@RequiredArgsConstructor
public class LotMasterRepositoryImpl implements LotMasterRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QItemAccountCode itemAccountCode = QItemAccountCode.itemAccountCode;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QItem item = QItem.item;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItemGroup itemGroup = QItemGroup.itemGroup;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QClient client = QClient.client;
    final QLotType lotType = QLotType.lotType1;
    final QOutSourcingInput outSourcingInput = QOutSourcingInput.outSourcingInput;
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QUnit unit = QUnit.unit;
    final QLotLog lotLog = QLotLog.lotLog;
    final QWorkOrderDetail workOrderDetail = QWorkOrderDetail.workOrderDetail;
    final QLotEquipmentConnect lotEquipmentConnect = QLotEquipmentConnect.lotEquipmentConnect;
    final QLotConnect lotConnect = QLotConnect.lotConnect;
    final QEquipment equipment = QEquipment.equipment;

    // id 로 itemAccountCode 의 code 조회
    @Override
    @Transactional(readOnly = true)
    public ItemAccountCode findCodeByItemId(Long itemId) {
        return jpaQueryFactory
                .select(itemAccountCode)
                .from(itemAccountCode)
                .innerJoin(itemAccount).on(itemAccount.id.eq(itemAccountCode.itemAccount.id))
                .innerJoin(item).on(item.itemAccountCode.id.eq(itemAccountCode.id))
                .where(
                        item.id.eq(itemId),
                        item.deleteYn.isFalse()
                )
                .fetchOne();
    }

    // 원부자재 일련번호 시퀀스 찾는법
    // 일련번호의 1~6이 현재날짜의 format 과 동일 And 일련번호가 9자
    // 끝에서 두번째 자리 수 중 제일 큰 애를 찾아서  +1
    @Override
    @Transactional(readOnly = true)
    public Optional<String> findLotNoByLotNoLengthAndLotNoDateAndCode(int length, String date, String code) {
        return Optional.ofNullable(jpaQueryFactory
                .select(lotMaster.lotNo)
                .from(lotMaster)
                .where(
                        isLotNoLengthEq(length),
                        isLotNoDateContain(date),
                        isCodeContain(code),
                        isDeleteYnFalse()
                )
                .orderBy(lotMaster.lotNo.desc())
                .limit(1)
                .fetchOne());
    }

    @Transactional(readOnly = true)
    public Optional<String> findLotNoByGoodsType(GoodsType goodsType, LocalDate startDate, LocalDate endDate){
        return Optional.ofNullable(jpaQueryFactory
                .select(lotMaster.lotNo)
                .from(lotMaster)
                .where(
//                        lotMaster.goodsType.eq(goodsType),
                        lotMaster.createdDate.between(startDate.atStartOfDay(), endDate.atStartOfDay())
                )
                .orderBy(lotMaster.createdDate.desc())
                .limit(1)
                .fetchOne());
    }

    @Transactional(readOnly = true)
    public Optional<String> findLotNoByAccountCodeAndDate(GoodsType goodsType, LocalDate now){
        return Optional.ofNullable(jpaQueryFactory
                    .select(lotMaster.lotNo)
                    .from(lotMaster)
                        .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                        .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                    .where(
                            lotMaster.createdDate.between(now.atStartOfDay(), LocalDateTime.of(now, LocalTime.MAX).withNano(0)),
                            itemAccount.goodsType.eq(goodsType),
                            lotMaster.lotMasterDivision.eq(REAL_LOT),
                            lotMaster.deleteYn.isFalse()
                    )
                    .orderBy(lotMaster.createdDate.desc())
                    .limit(1)
                    .fetchOne());
    }

    // 제품분유에 따른 달에 가장 마지막에 생성된 LOT NO
    @Override
    public Optional<String> findLotNoByAccountCodeAndMonth(GoodsType goodsType, LocalDate now) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(lotMaster.lotNo)
                        .from(lotMaster)
                        .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                        .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                        .where(
                                lotMaster.createdDate.between(now.atStartOfDay(), LocalDateTime.of(now, LocalTime.MAX).withNano(0)),
                                lotMaster.deleteYn.isFalse(),
                                lotMaster.lotMasterDivision.eq(REAL_LOT),
                                itemAccount.goodsType.eq(goodsType)
                        )
                        .orderBy(lotMaster.createdDate.desc())
                        .limit(1)
                        .fetchOne()
        );
    }

    // LOT 마스터 조회, 검색조건: 품목그룹 id, LOT 번호, 품번|품명, 창고 id, 등록유형, 재고유무, LOT 유형, 검사중여부, 유효여부
    @Override
    @Transactional(readOnly = true)
    public List<LotMasterResponse> findLotMastersByCondition(
            Long itemGroupId,
            String lotNo,
            String itemNoAndItemName,
            Long wareHouseId,
            EnrollmentType enrollmentType,
            Boolean stockYn,
            Long lotTypeId,
            Boolean testingYn,
            WorkProcessDivision workProcessDivision
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                LotMasterResponse.class,
                                lotMaster.id.as("id"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                wareHouse.wareHouseName.as("warehouse"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.serialNo.as("serialNo"),
                                lotType.lotType.as("lotType"),
                                lotMaster.enrollmentType.as("enrollmentType"),
//                                lotMaster.process.as("process"),
                                lotMaster.processYn.as("processYn"),
                                lotMaster.stockAmount.as("stockAmount"),
                                lotMaster.createdAmount.as("createdAmount"),
                                lotMaster.badItemAmount.as("badItemAmount"),
                                lotMaster.inputAmount.as("inputAmount"),
                                lotMaster.changeAmount.as("changeAmount"),
                                lotMaster.transferAmount.as("transferAmount"),
                                lotMaster.inspectAmount.as("inspectAmount"),
                                lotMaster.shipmentAmount.as("shipmentAmount"),
                                lotMaster.badItemReturnAmount.as("badItemReturnAmount"),
                                lotMaster.stockReturnAmount.as("stockReturnAmount"),
                                lotMaster.checkRequestAmount.as("checkRequestAmount"),
                                lotMaster.checkAmount.as("checkAmount"),
                                lotMaster.qualityLevel.as("qualityLevel"),
                                lotMaster.createdDate.as("createdDate"),
                                item.itemAccountCode.detail.as("itemAccountCode")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .leftJoin(itemGroup).on(itemGroup.id.eq(item.itemGroup.id))
                .leftJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                .where(
                        isItemGroupEq(itemGroupId),
                        isLotNoContain(lotNo),
                        isItemNoAndItemNameContain(itemNoAndItemName),
                        isWarehouseEq(wareHouseId),
                        isEnrollmentTypeEq(enrollmentType),
                        isStockYn(stockYn),
                        isLotTypeEq(lotTypeId),
                        isCheckAmountYn(testingYn),
                        isWorkProcessDivisionEq(workProcessDivision),
                        lotMaster.lotMasterDivision.eq(REAL_LOT),
                        isDeleteYnFalse()
                )
                .orderBy(lotMaster.createdDate.desc())
                .fetch();
    }

    private BooleanExpression isWorkProcessDivisionEq(WorkProcessDivision workProcessDivision) {
        return workProcessDivision != null ? workProcess.workProcessDivision.eq(workProcessDivision) : null;
    }

    //외주입고정보로 LOT마스터 조회
    @Override
    @Transactional(readOnly = true)
    public List<OutsourcingInputLOTResponse> findLotMastersByOutsourcing(Long input) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingInputLOTResponse.class,
                                outSourcingInput.id.as("id"),
                                lotMaster.id.as("lotId"),
                                lotType.lotType.as("lotType"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.createdAmount.as("inputAmount"),
                                outSourcingInput.testRequestType.as("testRequestType")
                        )
                )
                .from(lotMaster)
                .innerJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .innerJoin(outSourcingInput).on(outSourcingInput.id.eq(lotMaster.outSourcingInput.id))
                .where(
                    outSourcingInput.id.eq(input),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    //id로 LOT마스터 조회
    @Transactional(readOnly = true)
    public OutsourcingInputLOTResponse findLotMasterById(Long id){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingInputLOTResponse.class,
                                outSourcingInput.id.as("id"),
                                lotMaster.id.as("lotId"),
                                lotType.lotType.as("lotType"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.stockAmount.as("inputAmount"),
                                outSourcingInput.testRequestType.as("testRequestType")
                        )
                )
                .from(lotMaster)
                .innerJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .innerJoin(outSourcingInput).on(outSourcingInput.id.eq(lotMaster.outSourcingInput.id))
                .where(
                        lotMaster.id.eq(id),
                        isDeleteYnFalse()
                )
                .fetchOne();
    }

    //외주입고정보와 id로 LOT마스터 조회
    @Transactional(readOnly = true)
    public OutsourcingInputLOTResponse findLotMasterByInputAndId(OutSourcingInput input, Long id){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingInputLOTResponse.class,
                                outSourcingInput.id.as("id"),
                                lotMaster.id.as("lotId"),
                                lotType.lotType.as("lotType"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.stockAmount.as("inputAmount"),
                                outSourcingInput.testRequestType.as("testRequestType")
                        )
                )
                .from(lotMaster)
                .innerJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .innerJoin(outSourcingInput).on(outSourcingInput.id.eq(lotMaster.outSourcingInput.id))
                .where(
                        lotMaster.id.eq(id),
                        outSourcingInput.eq(input),
                        isDeleteYnFalse()
                )
                .fetchOne();
    }

    //재고현황 조회
    @Transactional(readOnly = true)
    public List<MaterialStockReponse> findStockByItemAccountAndItemAndItemAccountCode(
            Long itemAccountId, Long itemId, Long itemGroupId, Long warehouseId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                MaterialStockReponse.class,
                                lotMaster.item.itemNo.as("itemNo"),
                                lotMaster.item.itemName.as("itemName"),
                                item.manufacturer.clientCode.as("manufacturerCode"),
                                lotMaster.wareHouse.id.as("warehouseId"),
                                lotMaster.stockAmount.sum().as("amount")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .leftJoin(itemAccountCode).on(itemAccountCode.id.eq(item.itemAccountCode.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .where(
                        isWarehouseEq(warehouseId),
                        isItemGroupEq(itemGroupId),
                        isItemAccountEq(itemAccountId),
                        isDeleteYnFalse()
                )
                .groupBy(item,wareHouse,lotMaster.enrollmentType)
                .orderBy(item.id.asc())
                .fetch();
    }

    // 출하 LOT 정보 생성 시 LOT 정보 조회 API
    @Override
    public List<LotMasterResponse.idAndLotNo> findLotMastersByShipmentLotCondition(Long itemId, int notShippedAmount) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                LotMasterResponse.idAndLotNo.class,
                                lotMaster.id.as("id"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.item.id.as("itemId"),
                                lotMaster.item.itemNo.as("itemNo"),
                                lotMaster.item.itemName.as("itemName"),
                                lotMaster.workProcess.id.as("workProcessId"),
                                lotMaster.workProcess.workProcessDivision.as("workProcessDivision"),
                                lotMaster.stockAmount.as("stockAmount")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                .where(
                        item.id.eq(itemId),
                        workProcess.workProcessDivision.eq(PACKAGING),
                        lotMaster.stockAmount.goe(1),
                        lotMaster.stockAmount.loe(notShippedAmount),
                        lotMaster.deleteYn.isFalse()

                )
                .fetch();
    }

    @Transactional(readOnly = true)
    //ITEM으로 현재 재고현황 조회
    public List<MaterialStockReponse> findStockAmountByItemId(Long itemId, Long warehouseId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                MaterialStockReponse.class,
                                lotMaster.item.id.as("itemId"),
                                lotMaster.wareHouse.id.as("warehouseId"),
                                lotMaster.stockAmount.sum().as("amount"),
                                item.inputUnitPrice.as("inputUnitPrice"),
                                lotMaster.outSourcingInput.id.as("outsourcingId"),
                                lotMaster.workProcess.id.as("workProcessId")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .where(
                        isWarehouseEq(warehouseId),
                        item.id.eq(itemId),
                        lotMaster.deleteYn.isFalse(),
                        lotMaster.stockAmount.gt(0)
                )
                .groupBy(lotMaster.wareHouse, lotMaster.item, lotMaster.outSourcingInput)
                .fetch();
    }

    //공정별 불량 수량 가져오기
    @Transactional(readOnly = true)
    public List<PopRecycleResponse> findBadAmountByWorkProcess(Long workProcessId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopRecycleResponse.class,
                                item.id.as("id"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.badItemAmount.sum().as("badAmount"),
                                lotMaster.recycleAmount.sum().as("recycleAmount")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                .innerJoin(lotLog).on(lotLog.lotMaster.id.eq(lotMaster.id))
                .innerJoin(workOrderDetail).on(workOrderDetail.id.eq(lotLog.workOrderDetail.id))
                .where(
                        lotMaster.workProcess.id.eq(workProcessId),
                        //workOrderDetail.orderState.eq(COMPLETION),
                        lotMaster.lotMasterDivision.eq(DUMMY_LOT),
                        lotMaster.deleteYn.eq(false),
                        lotMaster.useYn.eq(true)
                )
                .groupBy(lotMaster.item.id)
                .fetch();
    }

    //공정별 불량 개수 단일 조회
    @Transactional(readOnly = true)
    public PopRecycleResponse findBadAmountByWorkProcess(Long workProcessId, Long itemId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopRecycleResponse.class,
                                item.id.as("id"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.badItemAmount.sum().as("badAmount"),
                                lotMaster.recycleAmount.sum().as("recycleAmount")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                .where(
                        lotMaster.workProcess.id.eq(workProcessId),
                        lotMaster.item.id.eq(itemId),
                        lotMaster.lotMasterDivision.eq(DUMMY_LOT)
                )
                .groupBy(lotMaster.item.id)
                .fetchOne();
    }

    //재사용 가능한 LOT검색
    @Transactional(readOnly = true)
    public List<LotMaster> findBadLotByItemIdAndWorkProcess(Long itemId, Long workProcessId, LotMasterDivision division){
        return jpaQueryFactory
                .selectFrom(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                .where(
                        lotMaster.item.id.eq(itemId),
                        lotMaster.workProcess.id.eq(workProcessId),
                        lotMaster.badItemAmount.gt(0),
                        lotMaster.deleteYn.eq(false),
                        lotMaster.useYn.eq(true),
                        lotMaster.lotMasterDivision.eq(division)
                )
                .orderBy(lotMaster.createdDate.desc())
                .fetch();
    }

    // 품목 id 에 해당되는 lotMaster 조회 1 이상
    @Override
    public List<PopBomDetailLotMasterResponse> findAllByItemIdAndLotNo(Long itemId, String lotNo) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopBomDetailLotMasterResponse.class,
                                lotMaster.id.as("lotMasterId"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.stockAmount.as("stockAmount"),
                                unit.unitCode.as("unitCodeName"),
                                unit.exhaustYn.as("exhaustYn")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .where(
                        lotMaster.item.id.eq(itemId),
                        lotMaster.deleteYn.isFalse(),
                        isLotNoContain(lotNo),
                        lotMaster.stockAmount.goe(1),
                        lotMaster.lotMasterDivision.eq(REAL_LOT)
                )
                .fetch();
    }

    // 공정, 설비로 라벨프린트 대상 조회
    public List<LabelPrintResponse> findPrintsByWorkProcessAndEquipment(Long workProcessId, Long equipmentId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                LabelPrintResponse.class,
                                lotMaster.lotNo.as("lotNo"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.stockAmount.as("amount")
                        )
                )
                .from(lotMaster)
                .innerJoin(item).on(item.id.eq(lotMaster.item.id))
                //.leftJoin(lotLog).on(lotLog.lotMaster.id.eq(lotMaster.id))
                //.innerJoin(workOrderDetail).on(workOrderDetail.id.eq(lotLog.workOrderDetail.id))
                .innerJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                .innerJoin(equipment).on(equipment.id.eq(lotMaster.equipment.id))
                .where(
                        //workOrderDetail.orderState.eq(COMPLETION),
                        lotMaster.lotMasterDivision.eq(REAL_LOT),
                        lotMaster.deleteYn.eq(false),
                        lotMaster.stockAmount.gt(0),
                        lotMaster.useYn.eq(true),
                        lotMaster.workProcess.id.eq(workProcessId),
                        lotMaster.equipment.id.eq(equipmentId)
                )
                .fetch();
    }

    // 생성날짜가 오늘이고, lotDivision 이 dummny 인 걸 찾아옴
    @Override
    public Optional<String> findDummyNoByDivision(LotMasterDivision lotMasterDivision, LocalDate startDate) {
        return Optional.ofNullable(jpaQueryFactory
                .select(lotMaster.lotNo)
                .from(lotMaster)
                .where(
                        lotMaster.createdDate.between(startDate.atStartOfDay(), LocalDateTime.of(startDate, LocalTime.MAX).withNano(0)),
                        lotMaster.lotMasterDivision.eq(lotMasterDivision)
                )
                .orderBy(lotMaster.createdDate.desc())
                .limit(1)
                .fetchOne());
    }

    @Override
    public Optional<BadItemWorkOrderResponse.subDto> findLotMaterByDummyLotIdAndWorkProcessId(Long dummyLotId, Long workProcessId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        BadItemWorkOrderResponse.subDto.class,
                                        lotMaster.badItemAmount.as("badAmount"),
                                        lotMaster.createdAmount.as("createAmount"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName")
                                )
                        )
                        .from(lotEquipmentConnect)
                        .leftJoin(lotMaster).on(lotMaster.id.eq(lotEquipmentConnect.parentLot.id))
                        .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                        .leftJoin(workProcess).on(workProcess.id.eq(lotMaster.workProcess.id))
                        .where(
                                lotMaster.id.eq(dummyLotId),
                                workProcess.id.eq(workProcessId),
                                lotMaster.lotMasterDivision.eq(DUMMY_LOT),
                                lotMaster.deleteYn.isFalse()
                        )
                        .limit(1)
                        .fetchOne()
        );
    }

    // 생성날짜가 오늘이고, 공정구분, inputEquipment 가 같은거
    @Override
    public Optional<LotMaster> findByTodayAndWorkProcessDivisionEqAndInputEquipmentEq(LocalDate now, WorkProcessDivision workProcessDivision, Long inputEquipmentId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotMaster)
                        .where(
                                lotMaster.createdDate.between(now.atStartOfDay(), LocalDateTime.of(now, LocalTime.MAX).withNano(0)),
                                lotMaster.workProcess.workProcessDivision.eq(workProcessDivision),
                                lotMaster.inputEquipment.id.eq(inputEquipmentId),
                                lotMaster.deleteYn.isFalse(),
                                lotMaster.exhaustYn.isFalse(),
                                lotMaster.stockAmount.goe(1),
                                lotMaster.lotMasterDivision.eq(REAL_LOT)
                        )
                        .fetchOne()
        );
    }

    //외주입고로 LOT 조회
    @Transactional(readOnly = true)
    public Optional<LotMaster> findByOutsourcingInput(Long outsourcingInputId){
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(lotMaster)
                        .leftJoin(outSourcingInput).on(outSourcingInput.id.eq(lotMaster.outSourcingInput.id))
                        .where(
                                outSourcingInput.id.eq(outsourcingInputId),
                                isDeleteYnFalse(),
                                lotMaster.enrollmentType.eq(OUTSOURCING_INPUT)
                        )
                        .fetchOne()
        );
    }

    // LOT 마스터 조회, 검색조건: 품목그룹 id, LOT 번호, 품번|품명, 창고 id, 등록유형, 재고유무, LOT 유형, 검사중여부, 유효여부
    // 품목그룹
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? itemGroup.id.eq(itemGroupId) : null;
    }
    // lot 번호
    private BooleanExpression isLotNoContain(String lotNo) {
        return lotNo != null ? lotMaster.lotNo.contains(lotNo) : null;
    }

    //품목계정
    private BooleanExpression isItemAccountEq(Long itemAccountId) {
        return itemAccountId != null ? itemAccount.id.eq(itemAccountId) : null;
    }

    //품목그룹
    private BooleanExpression isItemAccountCodeEq(Long itemAccountCodeId) {
        return itemAccountCodeId != null ? itemAccountCode.id.eq(itemAccountCodeId) : null;
    }
    // 품번|품명
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName).or(item.itemName.contains(itemNoAndName)) : null;
    }
    // 창고 id
    private BooleanExpression isWarehouseEq(Long warehouseId) {
        return warehouseId != null ? wareHouse.id.eq(warehouseId) : null;
    }
    // 등록유형
    private BooleanExpression isEnrollmentTypeEq(EnrollmentType enrollmentType) {
        return enrollmentType != null ? lotMaster.enrollmentType.eq(enrollmentType) : null;
    }
    // 재고유무
    // null
    // true: 재고수량 1 이상
    // false: 재고수량 0
    private BooleanExpression isStockYn(Boolean stockYn) {
        return stockYn != null ? (stockYn ? lotMaster.stockAmount.goe(1) : lotMaster.stockAmount.lt(1)) : null;
    }
    // LOT 유형
    private BooleanExpression isLotTypeEq(Long lotTypeId) {
        return lotTypeId != null ? lotMaster.lotType.id.eq(lotTypeId) : null;
    }
    // 검사중여부
    // null
    // true: checkAmount 1 이상 && 검사수량이 재고수량보다 미만
    // false: checkAmount 0
    private BooleanExpression isCheckAmountYn(Boolean checkAmountYn) {
        return checkAmountYn != null ?
                (checkAmountYn ? lotMaster.checkAmount.goe(1).and(lotMaster.checkAmount.lt(lotMaster.stockAmount)) : lotMaster.checkAmount.lt(1))
                : null;
    }

    // ================================================== 7-2. Lot Tracking ==============================================

    // lotTracking 검색조건: LOT 번호(필수값), 추적유형(필수값), 품명|품번
    // 정방향 true: 입력된 LOT 로 생산된 LOT 조회
    // 입력된 LOT NO 로 등록된 LotConnect 모두
    @Override
    public List<LotEquipmentConnect> findExhaustLotByLotNoAndTrackTypeTrue(String lotNo) {
        return jpaQueryFactory
                .select(lotEquipmentConnect)
                .from(lotConnect)
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotConnect.childLot.id))
                .leftJoin(lotEquipmentConnect).on(lotEquipmentConnect.id.eq(lotConnect.parentLot.id))
                .where(
                        lotMaster.lotNo.eq(lotNo),                   // LOT 번호 같은거
                        lotConnect.division.eq(EXHAUST),             // 구분값 소진으로 등록
                        lotMaster.deleteYn.isFalse(),                // 검색할 LOT 삭제 여부
                        lotMaster.lotMasterDivision.eq(REAL_LOT)     // lotMaster 구분 분할로트
                )
                .fetch();
    }

    // 위에 쿼리에서 return 받은 equipmentLotId 로 lotConnect 의 division 이 분할인걸 찾아서 조회?
    @Override
    public List<LotTrackingResponse> findLotTrackingResponseByTrackingTypeTrue(Long lotEquipmentConnectId, String itemNoAndItemName) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                LotTrackingResponse.class,
                                lotMaster.id.as("lotMasterId"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.item.itemNo.as("itemNo"),
                                lotMaster.item.itemName.as("itemName"),
                                lotMaster.enrollmentType.as("enrollmentType"),
                                lotMaster.createdDate.as("createdDate")
                        )
                )
                .from(lotConnect)
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotConnect.childLot.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .where(
                        lotConnect.parentLot.childLot.id.eq(lotEquipmentConnectId),      // lotEquipmentConnect 같은거
                        lotConnect.division.eq(FAMILY),                         // 구분값 분할로트
                        lotMaster.deleteYn.isFalse(),                           // 검색할 LOT 삭제 여부
                        isItemNoAndItemNameContain(itemNoAndItemName),          // 품번|품명,
                        lotMaster.lotMasterDivision.eq(REAL_LOT)                // lotMaster 구분 분할로트
                )
                .fetch();
    }

    // lotTracking 검색조건: LOT 번호(필수값), 추적유형(필수값), 품명|품번
    // 역방향 false: 입력된 LOT 를 생산하기 위해 투입된 LOT
    @Override
    public List<LotTrackingResponse> findLotTrackingResponseByTrackingTypeFalse(Long equipmentLotId, String itemNoAndItemName) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                LotTrackingResponse.class,
                                lotMaster.id.as("lotMasterId"),
                                lotMaster.lotNo.as("lotNo"),
                                lotMaster.item.itemNo.as("itemNo"),
                                lotMaster.item.itemName.as("itemName"),
                                lotMaster.enrollmentType.as("enrollmentType"),
                                lotMaster.createdDate.as("createdDate"),
                                lotConnect.amount.as("inputAmount")
                        )
                )
                .from(lotConnect)
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotConnect.childLot.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .where(
                        lotConnect.parentLot.childLot.id.eq(equipmentLotId),
                        lotConnect.division.eq(EXHAUST),
                        lotMaster.deleteYn.isFalse(),
                        isItemNoAndItemNameContain(itemNoAndItemName)
                )
                .fetch();
    }

    // 분할 lotNo 로 설비 LOT id 하나 찾음
    @Override
    public Optional<LotMaster> findEquipmentLotMasterByRealLotNo(String realLotNo) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(lotConnect.parentLot.childLot)
                        .from(lotConnect)
                        .where(
                                lotConnect.childLot.lotMasterDivision.eq(REAL_LOT),  // lotMaster 구분 분할로트
                                lotConnect.division.eq(FAMILY),                    // 구분값 분할
                                lotConnect.parentLot.childLot.deleteYn.isFalse(),
                                lotConnect.childLot.lotNo.eq(realLotNo)               // 분할로트
                        )
                        .limit(1)
                        .fetchOne()
        );
    }

    // 품목계정 별 재고현황 정보
    // lotMaster 의 realLot 중 stockAmount 가 0 이상이고, 검색조건으로 품목계정이 들어왔을때 품목의 갯수는 5개
    @Override
    public List<ItemInventoryStatusResponse> findItemInventoryStatusResponseByGoodsType(GoodsType goodsType) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ItemInventoryStatusResponse.class,
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotMaster.stockAmount.sum().as("stockAmount")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .where(
                        itemAccount.goodsType.eq(goodsType),
                        lotMaster.deleteYn.isFalse(),
                        lotMaster.stockAmount.ne(0),
                        lotMaster.lotMasterDivision.eq(REAL_LOT)
                )
                .orderBy(lotMaster.stockAmount.desc())
                .groupBy(item.id)
                .limit(5)
                .fetch();
    }

    // 매출관련현황 - 제품 생산
    @Override
    public List<SalesRelatedStatusResponse> findSalesRelatedStatusResponseByProductItems(LocalDate fromDate, LocalDate toDate) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                SalesRelatedStatusResponse.class,
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName")
                        )
                )
                .from(lotMaster)
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .where(
                        lotMaster.deleteYn.isFalse(),
                        lotMaster.createdDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)),
                        lotMaster.lotMasterDivision.eq(REAL_LOT),
                        itemAccount.goodsType.eq(PRODUCT),
                        lotMaster.enrollmentType.eq(PRODUCTION)
                )
                .groupBy(item.id)
                .orderBy(lotMaster.createdAmount.sum().desc())
                .limit(5)
                .fetch();
    }

    // 주 별로 생산 된 품목 갯수
    @Override
    public Optional<Integer> findCreatedAmountByWeekDate(LocalDate fromDate, LocalDate toDate, Long itemId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(lotMaster.createdAmount.sum())
                        .from(lotMaster)
                        .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                        .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                        .where(
                                lotMaster.deleteYn.isFalse(),
                                lotMaster.createdDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)),
                                lotMaster.lotMasterDivision.eq(REAL_LOT),
                                itemAccount.goodsType.eq(PRODUCT),
                                lotMaster.enrollmentType.eq(PRODUCTION),
                                lotMaster.item.id.eq(itemId)
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isLotNoLengthEq(int length) {
        return lotMaster.lotNo.length().eq(length);
    }

    private BooleanExpression isLotNoDateContain(String date) {
        return lotMaster.lotNo.contains(date);
    }

    private BooleanExpression isDeleteYnFalse() {
        return lotMaster.deleteYn.isFalse();
    }

    private BooleanExpression isCodeContain(String code) {
        return lotMaster.lotNo.contains(code);
    }
}
