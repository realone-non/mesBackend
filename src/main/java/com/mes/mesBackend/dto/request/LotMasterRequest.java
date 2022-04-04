package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.entity.enumeration.QualityLevel;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.OUTSOURCING_INPUT;
import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PURCHASE_INPUT;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.*;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.MATERIAL_INPUT;

@Getter
@Setter
@Schema(description = "LOT")
@NoArgsConstructor
@AllArgsConstructor
public class LotMasterRequest {
    Item item;
    WareHouse wareHouse;
//    LotType lotType;        // lot 타입
    String serialNo;        // 시리얼번호
    EnrollmentType enrollmentType;      // 등록유형
    boolean processYn;      // 공정용
    int stockAmount;        // 재고수량
    int createdAmount;       // 생성수량
    int badItemAmount;      // 불량수량
    int inputAmount;        // 투입수량
    int changeAmount;       // 전환수량
    int transferAmount;     // 이동수량
    int inspectAmount;      // 실사수량
    int shipmentAmount;     // 출하수량
    int returnAmount;       // 반품수량
    int checkRequestAmount;     // 검사요청수량
    int checkAmount;            // 검사수량
    int recycleAmount;          // 재사용 수량
    QualityLevel qualityLevel;      // 품질등급

    PurchaseInput purchaseInput;       //구매입고
    OutSourcingInput outSourcingInput;        //외주입고

    Long workProcessId;

    WorkProcessDivision workProcessDivision;    // 공정 구분
    Long equipmentId;       // 설비 id

    LotMasterDivision lotMasterDivision;

    // 자재입고 lot
    public void putPurchaseInputLotRequest(
            Item item,
            WareHouse wareHouse,
            PurchaseInput purchaseInput,
            int stockAmount,
            int createdAmount,
//            LotType lotType,
            boolean processYn
    ) {
        setItem(item);
        setWareHouse(wareHouse);
        setPurchaseInput(purchaseInput);
        setStockAmount(stockAmount);
        setCreatedAmount(createdAmount);
//        setLotType(lotType);
        setProcessYn(processYn);
        setWorkProcessDivision(MATERIAL_INPUT);
        setLotMasterDivision(REAL_LOT);
        setEnrollmentType(PURCHASE_INPUT);
    }

    // 외주입고 lot
    public void putOutsourcingInputLotRequest(
            Item item,
            WareHouse wareHouse,
            OutSourcingInput outSourcingInput,
            int stockAmount,
            int createdAmount,
            LotType lotType
    ) {
        setItem(item);
        setWareHouse(wareHouse);
        setOutSourcingInput(outSourcingInput);
        setStockAmount(stockAmount);
        setCreatedAmount(createdAmount);
//        setLotType(lotType);
        setLotMasterDivision(REAL_LOT);
        setWorkProcessDivision(MATERIAL_INPUT);
        setEnrollmentType(OUTSOURCING_INPUT);
    }

    public void putPopWorkOrder(
            Item item,
            WorkProcessDivision workProcessDivision,
            WareHouse wareHouse,
            int createdAmount,
            int stockAmount,
            int badItemAmount,
            EnrollmentType enrollmentType,
            Long equipmentId,
            LotMasterDivision division
    ) {
        setItem(item);                                    // 품목
        setWorkProcessDivision(workProcessDivision);    // 공정
        setWareHouse(wareHouse);                          // 창고
        setEnrollmentType(enrollmentType);               // 등록유형
        setEquipmentId(equipmentId);                     // 설비유형
        setLotMasterDivision(division);            // lot 생성 구분

        setCreatedAmount(this.createdAmount + createdAmount);    // 생성수량

        // 더미 로트
        if (division.equals(DUMMY_LOT)) {
            setBadItemAmount(this.badItemAmount + badItemAmount);   // 불량수량
        }

        // 설비 로트
        if (division.equals(EQUIPMENT_LOT)) {
            setStockAmount(stockAmount);
            setBadItemAmount(this.badItemAmount + badItemAmount);
        }

        // 분할 로트
        if (division.equals(REAL_LOT)) {
            setStockAmount(this.stockAmount + createdAmount);    // 재고수량
        }
    }
}
