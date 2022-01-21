package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "출하")
public class ShipmentRequest {
    @Schema(description = "거래처 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long client;

    @Schema(description = "출하일자")
    LocalDate shipmentDate;

    @Schema(description = "거래처담당자")
    String clientManager;

    @Schema(description = "비고")
    String note;
}
