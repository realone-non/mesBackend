package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "화폐")
public class CurrencyRequest {
    @Schema(description = "화폐")
    @NotBlank(message = NOT_EMPTY)
    String currency;        // 화폐

    @Schema(description = "화폐 단위")
    @NotBlank(message = NOT_EMPTY)
    String currencyUnit;

    @Schema(description = "현재 환율")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    float exchangeRate;
}
