package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

// 4-6. 출하반품 등록
@Getter
@Setter
@Schema(description = "출하 반품")
public class ShipmentReturnRequest {
    @Schema(description = "shipmentLot id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long shipmentLotId;

    @Schema(description = "반품일시")
    @NotNull(message = NOT_NULL)
    LocalDate returnDate;

    @Schema(description = "반품수량")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int returnAmount;

    @Schema(description = "입고창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long inputWarehouseId;

    @Schema(description = "비고")
    String note;
}
