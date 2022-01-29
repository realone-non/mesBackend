package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.QualityLevel;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "LOT")
@NoArgsConstructor
@AllArgsConstructor
public class LotMasterRequest {
    Item item;
    WareHouse wareHouse;
    Long lotTypeId;        // lot 타입
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
    QualityLevel qualityLevel;      // 품질등급

    Long purchaseInputId;       //구매입고 ID
    Long outsourcingInputId;        //외주입고 ID

    Long workProcessId;

    WorkProcessDivision workProcessDivision;    // 공정 구분
    Long equipmentId;       // 설비 id

    public void putPurchaseInput(
            Item item,
            WareHouse wareHouse,
            Long purchaseInputId,
            int stockAmount,
            int createdAmount,
            Long lotType,
            boolean processYn
    ) {
        setItem(item);
        setWareHouse(wareHouse);
        setPurchaseInputId(purchaseInputId);
        setStockAmount(stockAmount);
        setCreatedAmount(createdAmount);
        setLotTypeId(lotType);
        setProcessYn(processYn);
    }

    public void putOutsourcingInput(
            Item item,
            WareHouse wareHouse,
            Long outsourcingInputId,
            int stockAmount,
            Long lotType
    ) {
        setItem(item);
        setWareHouse(wareHouse);
        setOutsourcingInputId(outsourcingInputId);
        setStockAmount(stockAmount);
        setCreatedAmount(stockAmount);
        setCreatedAmount(stockAmount);
        setLotTypeId(lotType);
    }
}
