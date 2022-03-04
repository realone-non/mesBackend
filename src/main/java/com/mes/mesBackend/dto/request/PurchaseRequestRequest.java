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
@Schema(description = "구매요청")
public class PurchaseRequestRequest {
    @Schema(description = "제조오더 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long produceOrder;

    @Schema(description = "품목 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long itemId;

    @Schema(description = "요청일자")
    @NotNull(message = NOT_NULL)
    LocalDate requestDate;

    @Schema(description = "요청수량")
    @NotNull(message = NOT_NULL)
    int requestAmount;

    @Schema(description = "구매납기일자")
    @NotNull(message = NOT_NULL)
    LocalDate periodDate;

    @Schema(description = "비고")
    String note;

    @Schema(description = "재고단위요청수량")
    int stockUnitRequestAmount;

    @Schema(description = "재고단위발주수량")
    int stockUnitOrderAmount;

    @Schema(description = "수입검사여부")
    boolean inputTestYn;
}
