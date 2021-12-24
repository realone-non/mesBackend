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
@Schema(description = "구매발주")
public class PurchaseOrderRequest {
    @Schema(description = "발주일자")
    @NotNull(message = NOT_NULL)
    LocalDate purchaseOrderDate;

    @Schema(description = "담당자 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long user;

    @Schema(description = "입고창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long wareHouse;

    @Schema(description = "화폐 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long currency;

    @Schema(description = "부가세 적용")
    @NotNull(message = NOT_NULL)
    boolean surtaxYn;

    @Schema(description = "비고")
    String note;

    @Schema(description = "운송조건")
    String transportCondition;

    @Schema(description = "별첨")
    String addendum;

    @Schema(description = "운송유형")
    String transportType;

    @Schema(description = "지불조건")
    String payCondition;

    @Schema(description = "Requested Shipping(w)")
    String requestedShipping;

    @Schema(description = "특이사항")
    String specialNote;
}
