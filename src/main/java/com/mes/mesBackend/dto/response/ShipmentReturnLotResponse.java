package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "조건에 맞는 shipmentLot 조회")
public class ShipmentReturnLotResponse {
    @Schema(description = "출하 lot 고유아이디")
    Long shipmentLotId;

    @Schema(description = "출하번호")
    String shipmentNo;

    @Schema(description = "거래처")
    String clientCode;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "LOT 유형")
    String lotType;

    @Schema(description = "출하 수량")
    int shipmentAmount;
}
