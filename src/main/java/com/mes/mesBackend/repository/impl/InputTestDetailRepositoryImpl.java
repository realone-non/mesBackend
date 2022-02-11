package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.InputTestDetailResponse;
import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;
import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;
import com.mes.mesBackend.dto.response.InputTestScheduleResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.repository.custom.InputTestDetailRepositoryCustom;
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

import static com.mes.mesBackend.entity.enumeration.InputTestDivision.OUT_SOURCING;
import static com.mes.mesBackend.entity.enumeration.InputTestDivision.PART;
import static com.mes.mesBackend.entity.enumeration.InputTestState.*;

// 14-2. 검사 등록
@RequiredArgsConstructor
public class InputTestDetailRepositoryImpl implements InputTestDetailRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QInputTestDetail inputTestDetail = QInputTestDetail.inputTestDetail;
    final QInputTestRequest inputTestRequest = QInputTestRequest.inputTestRequest;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;
    final QClient client = QClient.client;
    final QWareHouse wareHouse = QWareHouse.wareHouse;
    final QItemForm itemForm = QItemForm.itemForm;
    final QTestProcess testProcess = QTestProcess.testProcess1;
    final QTestCriteria testCriteria = QTestCriteria.testCriteria1;
    final QPurchaseInput purchaseInput = QPurchaseInput.purchaseInput;
    final QUser user = QUser.user;
    final QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
    final QPurchaseRequest purchaseRequest = QPurchaseRequest.purchaseRequest;
    final QOutSourcingInput outSourcingInput = QOutSourcingInput.outSourcingInput;
    final QLotType lotType = QLotType.lotType1;
    final QOutSourcingProductionRequest outSourcingProductionRequest = QOutSourcingProductionRequest.outSourcingProductionRequest;

    // 검사요청정보 리스트 조회
    // 검색조건: 창고 id, 품명|품목, 완료여부, 입고번호, 품목그룹 id, LOT 유형 id, 요청기간 from~toDate, 제조사 id
    @Override
    @Transactional(readOnly = true)
    public List<InputTestRequestInfoResponse> findInputTestRequestInfoResponseByCondition(
            Long warehouseId,
            String itemNoAndName,
            Boolean completionYn,
            Long inputNo,
            Long itemGroupId,
            Long lotTypeId,
            LocalDate fromDate,
            LocalDate toDate,
            Long manufactureId,
            InputTestDivision inputTestDivision,
            InspectionType inspectionType
    ) {

        return jpaQueryFactory
                .select(
                        Projections.fields(
                                InputTestRequestInfoResponse.class,
                                inputTestRequest.id.as("id"),
                                inputTestRequest.createdDate.as("requestDate"),
                                lotMaster.id.as("lotMasterId"),
                                lotMaster.lotNo.as("lotNo"),
                                purchaseInput.id.as("purchaseInputNo"),             // 구매입고 입고번호
                                outSourcingInput.id.as("outsourcingInputNo"),       // 외주입고 입고번호
                                purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),                    // 구매입고 발주번호
                                outSourcingProductionRequest.id.as("outsourcingProductionRequestNo"),   // 외주입고 발주번호
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
//                                item.clientItemNo.as("itemClientPartNo"), // 고객사품번
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),    // 제조사품번
//                                client.clientName.as("clientName"), // 고객사
                                client.clientName.as("manufacturerName"), // 제조사
                                itemForm.form.as("itemForm"),
                                testCriteria.testCriteria.as("testCriteria"),
                                wareHouse.wareHouseName.as("warehouse"),
                                inputTestRequest.requestAmount.as("requestAmount"),
                                purchaseInput.urgentYn.as("urgentYn"),
                                purchaseInput.testReportYn.as("testReportYn"),
                                purchaseInput.coc.as("coc"),
                                lotMaster.enrollmentType.as("enrollmentType"),
                                inputTestRequest.testCompletionRequestDate.as("testCompletionRequestDate"),
                                item.testType.as("testType"),
                                inputTestRequest.inspectionType.as("inspectionType")
                        )
                )
                .from(inputTestRequest)
                .innerJoin(lotMaster).on(lotMaster.id.eq(inputTestRequest.lotMaster.id))
                .innerJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(purchaseInput).on(purchaseInput.id.eq(lotMaster.purchaseInput.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .leftJoin(itemForm).on(itemForm.id.eq(item.itemForm.id))
                .leftJoin(testCriteria).on(testCriteria.id.eq(item.testCriteria.id))
                .leftJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .leftJoin(outSourcingInput).on(outSourcingInput.id.eq(lotMaster.outSourcingInput.id))
                .leftJoin(outSourcingProductionRequest).on(outSourcingProductionRequest.id.eq(outSourcingInput.productionRequest.id))
                .where(
                        isWareHouseEq(warehouseId),
                        isItemNoAndItemNameContain(itemNoAndName),
                        isCompletionYn(completionYn),
                        isInputNoEq(inputNo, inputTestDivision),
                        isItemGroupEq(itemGroupId),
                        isLotTypeEq(lotTypeId),
                        isRequestDateBetween(fromDate, toDate),
                        isManufactureEq(manufactureId),
                        isInputTestRequestDeleteYnFalse(),
                        isInputTestDivision(inputTestDivision),
                        isInspectionTypeEq(inspectionType)
                )
                .fetch();
    }

    // 검사상세정보 단일조회
    @Override
    @Transactional(readOnly = true)
    public Optional<InputTestDetailResponse> findDetailByInputTestRequestIdAndInputTestDetailIdAndDeleteYnFalse(
            Long inputTestRequestId,
            Long inputTestDetailId,
            InputTestDivision inputTestDivision
    ) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        InputTestDetailResponse.class,
                                        inputTestDetail.id.as("id"),
                                        inputTestDetail.testDate.as("testDate"),
                                        inputTestDetail.testAmount.as("testAmount"),
                                        inputTestDetail.fairQualityAmount.as("fairQualityAmount"),
                                        inputTestDetail.incongruityAmount.as("incongruityAmount"),
                                        inputTestDetail.testResult.as("testResult"),
                                        wareHouse.id.as("warehouseId"),
                                        wareHouse.wareHouseName.as("warehouse"),
                                        inputTestDetail.testReportFileUrl.as("testReportFileUrl"),
                                        inputTestDetail.cocFileUrl.as("cocFileUrl"),
                                        inputTestDetail.user.id.as("userId"),
                                        inputTestDetail.user.korName.as("user"),
                                        inputTestDetail.note.as("note")
                                )
                        )
                        .from(inputTestDetail)
                        .leftJoin(inputTestRequest).on(inputTestRequest.id.eq(inputTestDetail.inputTestRequest.id))
                        .leftJoin(lotMaster).on(lotMaster.id.eq(inputTestRequest.lotMaster.id))
                        .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                        .leftJoin(user).on(user.id.eq(inputTestDetail.user.id))
                        .where(
                                inputTestDetail.id.eq(inputTestDetailId),
                                inputTestRequest.id.eq(inputTestRequestId),
                                isInputTestRequestDeleteYnFalse(),
                                isInputTestDetailDeleteYnFalse(),
                                isInputTestDivision(inputTestDivision)
                        )
                        .fetchOne());
    }

    // 검사상세정보 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<InputTestDetailResponse> findDetailsByInputTestRequestIdAndDeleteYnFalse(Long inputTestRequestId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                InputTestDetailResponse.class,
                                inputTestDetail.id.as("id"),
                                inputTestDetail.testDate.as("testDate"),
                                inputTestDetail.testAmount.as("testAmount"),
                                inputTestDetail.fairQualityAmount.as("fairQualityAmount"),
                                inputTestDetail.incongruityAmount.as("incongruityAmount"),
                                inputTestDetail.testResult.as("testResult"),
                                wareHouse.id.as("warehouseId"),
                                wareHouse.wareHouseName.as("warehouse"),
                                inputTestDetail.testReportFileUrl.as("testReportFileUrl"),
                                inputTestDetail.cocFileUrl.as("cocFileUrl"),
                                inputTestDetail.user.id.as("userId"),
                                inputTestDetail.user.korName.as("user"),
                                inputTestDetail.note.as("note")
                        )
                )
                .from(inputTestDetail)
                .leftJoin(inputTestRequest).on(inputTestRequest.id.eq(inputTestDetail.inputTestRequest.id))
                .leftJoin(lotMaster).on(lotMaster.id.eq(inputTestRequest.lotMaster.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .leftJoin(user).on(user.id.eq(inputTestDetail.user.id))
                .where(
                        inputTestRequest.id.eq(inputTestRequestId),
                        isInputTestDetailDeleteYnFalse()
                ).fetch();
    }

    // 검사요청정보에 해당하는 검사정보의 모든 검사수량
    @Override
    @Transactional(readOnly = true)
    public List<Integer> findTestAmountByInputTestRequestId(Long inputTestRequestId) {
        return jpaQueryFactory
                .select(inputTestDetail.testAmount)
                .from(inputTestDetail)
                .innerJoin(inputTestRequest).on(inputTestRequest.id.eq(inputTestDetail.inputTestRequest.id))
                .where(
                        inputTestDetail.inputTestRequest.id.eq(inputTestRequestId),
                        isInputTestDetailDeleteYnFalse()
                )
                .fetch();
    }

    // 검사요청정보에 해당하는 검사정보의 총 부적합수량
    @Override
    @Transactional(readOnly = true)
    public List<Integer> findBadItemAmountByInputTestRequestId(Long inputTestRequestId) {
        return jpaQueryFactory
                .select(inputTestDetail.incongruityAmount)
                .from(inputTestDetail)
                .innerJoin(inputTestRequest).on(inputTestRequest.id.eq(inputTestDetail.inputTestRequest.id))
                .where(
                        inputTestDetail.inputTestRequest.id.eq(inputTestRequestId),
                        isInputTestDetailDeleteYnFalse()
                )
                .fetch();
    }
    // 검사요청정보에 해당하는 검사정보의 총 양품수량
    @Override
    @Transactional(readOnly = true)
    public List<Integer> findStockAmountByInputTestRequestId(Long inputTestRequestId) {
        return jpaQueryFactory
                .select(inputTestDetail.fairQualityAmount)
                .from(inputTestDetail)
                .innerJoin(inputTestRequest).on(inputTestRequest.id.eq(inputTestDetail.inputTestRequest.id))
                .where(
                        inputTestDetail.inputTestRequest.id.eq(inputTestRequestId),
                        isInputTestDetailDeleteYnFalse()
                )
                .fetch();
    }

    // ================================================= 14-3. 검사실적조회 =================================================
    // 검사실적조회
    // 검색조건: 검사기간 fromDate~toDate, 품명|품목, 거래처 id, 입고번호(구매입고 id)
    @Override
    @Transactional(readOnly = true)
    public List<InputTestPerformanceResponse> findInputTestPerformanceResponseByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndName,
            Long clientId,
            Long purchaseInputNo,
            InputTestDivision inputTestDivision,
            InspectionType inspectionType,
            Long wareHouseId
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                InputTestPerformanceResponse.class,
                                inputTestDetail.id.as("inputTestDetailId"),
                                inputTestRequest.createdDate.as("requestDate"),
                                inputTestDetail.testDate.as("testDate"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemForm.form.as("itemForm"),
                                purchaseOrder.purchaseOrderNo.as("purchaseOrderNo"),
                                outSourcingProductionRequest.id.as("outsourcingOrderNo"),
                                lotMaster.lotNo.as("lotNo"),
//                                item.clientItemNo.as("itemClientPartNo"), // 고객사품번
                                item.manufacturerPartNo.as("itemManufacturerPartNo"),    // 제조사품번
                                client.clientName.as("manufacturerName"), // 제조사
//                                client.clientName.as("clientName"), // 고객사
                                testCriteria.testCriteria.as("testCriteria"),
                                inputTestDetail.testAmount.as("testAmount"),
                                inputTestDetail.fairQualityAmount.as("fairQualityAmount"),
                                inputTestDetail.incongruityAmount.as("incongruityAmount"),
                                user.korName.as("user"),
                                purchaseInput.testReportYn.as("testReportYn"),
                                purchaseInput.coc.as("cocYn"),
                                inputTestDetail.testReportFileUrl.as("testReportFileUrl"),
                                inputTestDetail.cocFileUrl.as("cocFileUrl"),
                                inputTestDetail.note.as("note"),
                                lotMaster.lotType.lotType.as("lotType"),
                                wareHouse.wareHouseName.as("wareHouseName"),
                                item.testType.as("testType"),
                                inputTestRequest.inspectionType.as("inspectionType")
                        )
                )
                .from(inputTestDetail)
                .leftJoin(inputTestRequest).on(inputTestRequest.id.eq(inputTestDetail.inputTestRequest.id))
                .leftJoin(lotMaster).on(lotMaster.id.eq(inputTestRequest.lotMaster.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(itemForm).on(itemForm.id.eq(item.itemForm.id))
                .leftJoin(purchaseInput).on(purchaseInput.id.eq(lotMaster.purchaseInput.id))
                .leftJoin(outSourcingInput).on(outSourcingInput.id.eq(lotMaster.outSourcingInput.id))
                .leftJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .leftJoin(testCriteria).on(testCriteria.id.eq(item.testCriteria.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .leftJoin(user).on(user.id.eq(inputTestDetail.user.id))
                .leftJoin(outSourcingProductionRequest).on(outSourcingProductionRequest.id.eq(outSourcingInput.productionRequest.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .where(
                        isTestDateBetween(fromDate, toDate),
                        isItemNoAndItemNameContain(itemNoAndName),
                        isClientIdEq(clientId),
                        isInputTestDetailDeleteYnFalse(),
                        isInputTestRequestDeleteYnFalse(),
                        isInputTestDivision(inputTestDivision),
                        isInputIdEq(purchaseInputNo, inputTestDivision),
                        isInspectionTypeEq(inspectionType),
                        isWareHouseEq(wareHouseId)
                )
                .fetch();
    }

    // 입고번호
    private BooleanExpression isInputIdEq(Long inputId, InputTestDivision inputTestDivision) {
        if (inputId != null) {
            if (inputTestDivision.equals(PART)) return purchaseInput.id.eq(inputId);
            else if (inputTestDivision.equals(OUT_SOURCING))return outSourcingInput.id.eq(inputId);
        }
        return null;
    }

    // 제조사 id
    private BooleanExpression isManufactureEq(Long manufactureId) {
        return manufactureId != null ? client.id.eq(manufactureId) : null;
    }

    // 14-4. 검사대기현황, 15-4. 검사대기현황
    // 검색조건: 검사창고 id, 검사유형, 품명|품번, 거래처, 검사기간 fromDate~toDate
    @Override
    public List<InputTestScheduleResponse> findInputTestScheduleResponsesByCondition(
            Long wareHouseId,
            InspectionType inspectionType,
            String itemNoAndName,
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            InputTestDivision inputTestDivision
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                InputTestScheduleResponse.class,
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                lotType.lotType.as("lotType"),
                                lotMaster.lotNo.as("lotNo"),
                                inputTestRequest.createdDate.as("requestDate"),
                                inputTestRequest.requestAmount.as("requestAmount"),
                                purchaseRequest.periodDate.as("purchaseInputPeriodDate"),   // purchaseInput
                                outSourcingProductionRequest.periodDate.as("outsourcingPeriodDate"),  // outsourcing
                                wareHouse.wareHouseName.as("wareHouseName"),
                                client.clientName.as("clientName"),
                                inputTestRequest.id.as("inputTestRequestId"),
                                item.testType.as("testType"),
                                inputTestRequest.inspectionType.as("inspectionType")
                        )
                )
                .from(inputTestRequest)
                .leftJoin(lotMaster).on(lotMaster.id.eq(inputTestRequest.lotMaster.id))
                .leftJoin(lotType).on(lotType.id.eq(lotMaster.lotType.id))
                .leftJoin(purchaseInput).on(purchaseInput.id.eq(lotMaster.purchaseInput.id))
                .leftJoin(purchaseRequest).on(purchaseRequest.id.eq(purchaseInput.purchaseRequest.id))
                .leftJoin(outSourcingInput).on(outSourcingInput.id.eq(lotMaster.outSourcingInput.id))
                .leftJoin(outSourcingProductionRequest).on(outSourcingProductionRequest.id.eq(outSourcingInput.productionRequest.id))
                .leftJoin(wareHouse).on(wareHouse.id.eq(lotMaster.wareHouse.id))
                .leftJoin(purchaseOrder).on(purchaseOrder.id.eq(purchaseRequest.purchaseOrder.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .leftJoin(client).on(client.id.eq(item.manufacturer.id))
                .where(
                        isWareHouseEq(wareHouseId),
                        isInspectionTypeEq(inspectionType),
                        isItemNoAndItemNameContain(itemNoAndName),
                        isClientIdEq(clientId),
                        isRequestDateBetween(fromDate, toDate),
                        isInputTestDivision(inputTestDivision),
                        isInputTestRequestDeleteYnFalse(),
                        inputTestRequest.inputTestState.eq(SCHEDULE)
                )
                .fetch();
    }

    // 검색조건: 검사창고 id, 검사유형, 품명|품번, 거래처, 검사기간 fromDate~toDate
    private BooleanExpression isInspectionTypeEq(InspectionType inspectionType) {
        return inspectionType != null ? inputTestRequest.inspectionType.eq(inspectionType) : null;
    }
    // 거래처
    private BooleanExpression isClientIdEq(Long clientId) {
        return clientId != null ? client.id.eq(clientId) : null;
    }
    // 창고 id
    private BooleanExpression isWareHouseEq(Long wareHouseId) {
        return wareHouseId != null ? wareHouse.id.eq(wareHouseId) : null;
    }
    // 품명|품목
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? item.itemNo.contains(itemNoAndName).or(item.itemName.contains(itemNoAndName)) : null;
    }
    // 완료여부
    private BooleanExpression isCompletionYn(Boolean completionYn) {
        return completionYn != null ? completionYn ? inputTestRequest.inputTestState.eq(COMPLETION) : inputTestRequest.inputTestState.eq(SCHEDULE).or(inputTestRequest.inputTestState.eq(ONGOING)) : null;
    }

    // 입고번호
    private BooleanExpression isInputNoEq(Long inputNo, InputTestDivision inputTestDivision) {
        if (inputNo != null) {
            if (inputTestDivision.equals(PART)) return purchaseInput.id.eq(inputNo);
            else if (inputTestDivision.equals(OUT_SOURCING))return outSourcingInput.id.eq(inputNo);
        }
        return null;
    }

    // 품목그룹 id
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? item.itemGroup.id.eq(itemGroupId) : null;
    }
    // LOT 유형 id
    private BooleanExpression isLotTypeEq(Long lotTypeId) {
        return lotTypeId != null ? lotMaster.lotType.id.eq(lotTypeId) : null;
    }
    // 요청기간 from~toDate
    private BooleanExpression isRequestDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? inputTestRequest.createdDate.between(fromDate.atStartOfDay(), LocalDateTime.of(toDate, LocalTime.MAX).withNano(0)) : null;
    }

    private BooleanExpression isInputTestRequestDeleteYnFalse() {
        return inputTestRequest.deleteYn.isFalse();
    }
    private BooleanExpression isInputTestDetailDeleteYnFalse() {
        return inputTestDetail.deleteYn.isFalse();
    }
    // 검사기간
    private BooleanExpression isTestDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? inputTestDetail.testDate.between(fromDate, toDate) : null;
    }
    private BooleanExpression isInputTestDivision(InputTestDivision testDivision) {
        return inputTestRequest.inputTestDivision.eq(testDivision);
    }
}
