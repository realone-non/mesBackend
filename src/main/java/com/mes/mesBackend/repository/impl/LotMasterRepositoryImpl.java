package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.repository.custom.LotMasterRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LotMasterRepositoryImpl implements LotMasterRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QItemAccountCode itemAccountCode = QItemAccountCode.itemAccountCode;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QItem item = QItem.item;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItemGroup itemGroup = QItemGroup.itemGroup;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QLotType lotType = QLotType.lotType1;

    // id 로 itemAccountCode 의 symbol 조회
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
            Boolean testingYn
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
                                lotMaster.processYn.as("processYn"),
                                lotMaster.stockAmount.as("stockAmount"),
                                lotMaster.createdAmount.as("createdAmount"),
                                lotMaster.badItemAmount.as("badItemAmount"),
                                lotMaster.inputAmount.as("inputAmount"),
                                lotMaster.changeAmount.as("changeAmount"),
                                lotMaster.transferAmount.as("transferAmount"),
                                lotMaster.inspectAmount.as("inspectAmount"),
                                lotMaster.shipmentAmount.as("shipmentAmount"),
                                lotMaster.returnAmount.as("returnAmount"),
                                lotMaster.checkRequestAmount.as("checkRequestAmount"),
                                lotMaster.checkAmount.as("checkAmount"),
                                lotMaster.qualityLevel.as("qualityLevel"),
                                lotMaster.createdDate.as("createdDate")
                        )
                )
                .from(lotMaster)
                .innerJoin(item).on(item.id.eq(lotMaster.item.id))
                .innerJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .innerJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .leftJoin(itemGroup).on(itemGroup.id.eq(item.itemGroup.id))
                .where(
                        isItemGroupEq(itemGroupId),
                        isLotNoContain(lotNo),
                        isItemNoAndItemNameContain(itemNoAndItemName),
                        isWarehouseEq(wareHouseId),
                        isEnrollmentTypeEq(enrollmentType),
                        isStockYn(stockYn),
                        isLotTypeEq(lotTypeId),
                        isCheckAmountYn(testingYn),
                        isDeleteYnFalse()
                )
                .fetch();
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
