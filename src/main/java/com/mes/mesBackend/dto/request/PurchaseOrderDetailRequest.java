package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "구매발주")
public class PurchaseOrderDetailRequest {
    @Schema(description = "구매요청 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long purchaseRequestId;

    @Schema(description = "발주수량")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long purchaseAmount;
}
