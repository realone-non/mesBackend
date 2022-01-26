package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "구매발주")
@JsonInclude(NON_NULL)
public class PurchaseOrderDetailResponse {
    @Schema(description = "요청번호(고유아이디)")
    Long id;

    @Schema(description = "품목 고유아이디")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "제조사품번")
    String itemManufacturerPartNo;

    @Schema(description = "발주단위")
    String orderUnitCodeName;

    @Schema(description = "발주수량")
    int orderAmount;

    @Schema(description = "단가")
    int unitPrice;

    @Schema(description = "발주금액")
    int orderPrice;

    @Schema(description = "발주금액(원화)")
    int orderPriceWon;

    @Schema(description = "VAT")
    double vat;

    @Schema(description = "발주가능수량")
    int orderPossibleAmount;

    @Schema(description = "입고수량")
    int inputAmount;

    @Schema(description = "취소수량")
    int cancelAmount;

    @Schema(description = "구매납기일자")
    LocalDate orderPeriodDate;

    @Schema(description = "비고")
    String note;

    @Schema(description = "수입검사유형")
    TestType inputTestType;

    @Schema(description = "제조사")
    String manufacturerName;

//    @Schema(description = "지시상태")
//    OrderState orderState;

    public PurchaseOrderDetailResponse putOrderPossibleAmountAndInputAmount(int allInputAmount) {
        setInputAmount(allInputAmount);
        setOrderPossibleAmount(this.orderAmount - this.inputAmount);
        return this;
    }
}
