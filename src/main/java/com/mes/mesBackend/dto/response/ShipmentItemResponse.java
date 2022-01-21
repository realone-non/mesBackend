package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "출하 품목")
@JsonInclude(NON_NULL)
public class ShipmentItemResponse {
    @Schema(description = "수주 고유아이디")
    Long contractId;

    @Schema(description = "수주번호")
    String contractNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "수주단위")
    String contractUnit;

    @Schema(description = "수주미출하수량")
    int notShippedAmount;           // 수주미출하수량 = 수주수량 - 출하수량

    @Schema(description = "출하수량")
    int shipmentAmount;

    @Schema(description = "출하금액")
    int shipmentPrice;               // 출하금액 = 출하수량 * 수주 품목 단가

    @Schema(description = "출하금액(원화)")
    int shipmentPriceWon;            // 출하금액(원화) = 출하수량 * 수주 품목 단가

    @Schema(description = "비고")
    String note;
}
