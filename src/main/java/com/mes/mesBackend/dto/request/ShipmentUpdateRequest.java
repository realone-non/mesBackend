package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "출하")
public class ShipmentUpdateRequest {
    @Schema(description = "출하일자")
    LocalDate shipmentDate;

    @Schema(description = "거래처담당자")
    String clientManager;

    @Schema(description = "비고")
    String note;
}
