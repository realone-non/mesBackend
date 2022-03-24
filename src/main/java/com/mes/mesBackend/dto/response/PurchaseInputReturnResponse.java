package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

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

    @Schema(description = "전체수량")
    int stockAmount;

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


    // 감리대비 수정사항
    @Schema(description = "구매입고 LOT 입고수량")
    int lotInputAmount; // 구매입고 LOT 입고수량

    @Schema(description = "구매입고 LOT 입고금액")
    int lotInputPrice;  // 구매입고 LOT 입고금액

    @Schema(description = "구매입고LOT부가세")
    double lotInputPriceSurtax; // 구매입고LOT부가세

    @Schema(description = "구매입고LOT구매처LOT번호")
    String clientLotNo; // 구매입고LOT구매처LOT번호

    @Schema(description = "구매입고LOT제조일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate manufactureDate; // 구매입고LOT제조일자

    @Schema(description = "구매입고LOT유효일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate validDate;        // 구매입고LOT유효일자

    @Schema(description = "구매입고LOT검사기준")
    String testCriteria;        // 구매입고LOT검사기준

    @Schema(description = "구매입고LOT시험성적서")
    boolean testReportYn;       // 구매입고LOT시험성적서

    @Schema(description = "반품금액")
    int returnPrice;            // 반품금액

    @Schema(description = "반품금액(원)")
    int returnPriceWon;         // 반품금액(원)

    public void setPossibleAmount() {
        if (returnDivision) {
            setStockAmount(stockAmountPossibleAmount);
        }else
            setStockAmount(badItemAmountPossibleAmount);
    }
}
