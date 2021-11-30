package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "P/I")
public class EstimatePiRequest {
    @Schema(description = "customer PO NO")
    String customerPoNo;

    @Schema(description = "Bill To")
    String billTo;

    @Schema(description = "Remarks")
    String remarks;
}
