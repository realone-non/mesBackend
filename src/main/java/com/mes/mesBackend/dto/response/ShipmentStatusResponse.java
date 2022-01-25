package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

// 4-7. 출하 현황
@Getter
@Setter
@Schema(description = "4-7. 출하 현황")
@JsonInclude(NON_NULL)
public class ShipmentStatusResponse {
    @Schema(description = "출하 고유아이디")
    Long shipmentId;

    @Schema(description = "출하일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate shipmentDate;

    @Schema(description = "출하번호")
    String shipmentNo;

    @Schema(description = "거래처")
    String clientCode;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "담당자")
    String userManager;

    @Schema(description = "출하창고")
    String wareHouseName;

    @Schema(description = "부가세적용")
    boolean surtax;

    @Schema(description = "출하 품목정보 고유아이디")
    Long shipmentItemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "수주단위")
    String contractUnit;

    @Schema(description = "수주번호")
    String contractNo;

    @Schema(description = "출하수량")
    int shipmentAmount;

    @Schema(description = "화폐")
    String currency;

    @Schema(description = "환율")
    float exchangeRate;

    @Schema(description = "출하금액")
    int shipmentPrice;

    @Schema(description = "출하금액(원화)")
    int shipmentPriceWon;

    @Schema(description = "부가세")
    double vat;

    @Schema(description = "출하등록 LOT 정보 고유아이디")
    Long shipmentLotId;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "납기일자")
    LocalDate periodDate;

    @Schema(description = "비고")
    String note;
}
