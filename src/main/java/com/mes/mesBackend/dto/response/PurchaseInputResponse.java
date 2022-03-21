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

@Getter
@Setter
@Schema(description = "구매입고 정보")
@JsonInclude(NON_NULL)
public class PurchaseInputResponse {
    @Schema(description = "고유아이디(구매요청)")
    Long id;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "제조사품번")
    String itemManufacturerPartNo;

    // 어떤 입고일시 ?
    // 구매입고가 제일 최근에 생성 된 createdDate
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    @Schema(description = "입고일시")
    LocalDate inputDate;

    @Schema(description = "발주단위")
    String orderUnitCodeName;

    @Schema(description = "단가")
    int unitPrice;

    @Schema(description = "입고수량")
    int inputAmount;

    @Schema(description = "입고금액")
    int inputPrice;

    @Schema(description = "VAT")
    double vat;

    @Schema(description = "입고창고")
    String wareHouseName;

    // 어디에 납기일자 ?
    // 구매발주등록 디테일에 보여주는 데이터
    @Schema(description = "납기일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate periodDate;

    @Schema(description = "발주번호")
    String purchaseOrderNo;

    @Schema(description = "미입고수량")
    int alreadyInput;

    @Schema(description = "담당자")
    String userName;

    @Schema(description = "화폐")
    String currencyUnit;

    @Schema(description = "비고")
    String note;

    @JsonIgnore
    int orderAmount;// 구매요청 발주수량

    @Schema(description = "부품수입검사 의뢰 품명")
    String partInputTestItemNo;

    // 입고금액
    public void setInputPrice() {
        this.inputPrice = this.unitPrice * this.inputAmount;
    }

    // 부가세
    public void setVat() {
        this.vat = this.inputPrice * 0.1;
    }

    // 미입고수량 = 발주수량 - 입고수량
    public void setAlreadyInput(int orderAmount, int inputAmountSum) {
        this.alreadyInput = orderAmount - inputAmountSum;
    }
}
