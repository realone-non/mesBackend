package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 4-6. 출하반품 등록
@Getter
@Setter
@Schema(description = "출하 반품")
public class ShipmentReturnRequest {
    @Schema(description = "shipmentLot id")
    Long shipmentLotId;

    @Schema(description = "반품일시")
    LocalDate returnDate;

    @Schema(description = "반품수량")
    int returnAmount;

    @Schema(description = "입고창고 id")
    Long inputWarehouseId;

    @Schema(description = "비고")
    String note;
}
