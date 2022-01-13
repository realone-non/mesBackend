package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 9-6. 구매입고 반품 등록
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "구매입고 반품 등록")
public class PurchaseInputReturnResponse {
    @Schema(description = "구매입고 반품 고유아이디")
    Long id;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "입고번호")
    Long purchaseInputId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "제조사품번")
    String itemManufacturerPartNo;

    @Schema(description = "LOT master id")
    Long lotMasterId;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "반품일시")
    LocalDate returnDate;

    @Schema(description = "가능수량")
    int possibleAmount;

    @Schema(description = "반품수량")
    int returnAmount;

    @Schema(description = "비고")
    String note;

    @JsonIgnore
    boolean returnDivision;             // 반품종류

    @JsonIgnore
    int stockAmountPossibleAmount;      // lotMaster stockAmount

    @JsonIgnore
    int badItemAmountPossibleAmount;    // lotMaster badItemAmount

    public void setPossibleAmount() {
        if (returnDivision) {
            setPossibleAmount(stockAmountPossibleAmount);
        }else
            setPossibleAmount(badItemAmountPossibleAmount);
    }
}
