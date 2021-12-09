package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "출하 품목")
public class ShipmentItemRequest {
    @Schema(description = "수주 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long contract;

    @Schema(description = "수주 품목 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long contractItem;

    @Schema(description = "출하수량")
    int shipmentAmount;

    @Schema(description = "재고수량(미구현)")
    int inventoryAmount;

    @Schema(description = "PKGS(미구현)")
    float pkgs;

    @Schema(description = "N/W(KG)(미구현)")
    float nw;

    @Schema(description = "G/W(KG)(미구현)")
    float gw;

    @Schema(description = "비고")
    String note;
}
