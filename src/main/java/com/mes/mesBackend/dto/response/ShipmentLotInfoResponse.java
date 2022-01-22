package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 4-5. 출하등록
@Getter
@Setter
@Schema(description = "출하등록 LOT 정보")
@JsonInclude(NON_NULL)
public class ShipmentLotInfoResponse {
    @Schema(description = "출하등록 LOT 정보 고유아이디")
    Long shipmentLotId;

    @Schema(description = "lot master 고유아이디")
    Long lotId;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "수주단위")
    String contractUnit;

    @Schema(description = "출고수량")
    int shipmentOutputAmount;

    @Schema(description = "출하금액")
    int shipmentPrice;

    @Schema(description = "출하금액(원화)")
    int shipmentPriceWon;

    @Schema(description = "VAT")
    double vat;

    @Schema(description = "검사여부")
    boolean inputTestYn;
}
