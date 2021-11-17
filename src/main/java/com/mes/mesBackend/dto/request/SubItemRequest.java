package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "대체품")
public class SubItemRequest {
    @Schema(description = "품목 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long item;

    @Schema(description = "대체 품목 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long subItem;

    @Schema(description = "대체순번 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int subOrders;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
