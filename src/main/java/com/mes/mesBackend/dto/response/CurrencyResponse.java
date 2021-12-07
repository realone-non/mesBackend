package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;

@Getter
@Setter
@Schema(description = "화폐")
public class CurrencyResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "화폐")
    String currency;        // 화폐

    @Schema(description = "화폐 단위")
    String currencyUnit;

    @Schema(description = "현재 환율")
    float exchangeRate;

    @Getter
    @Setter
    @Schema(description = "화폐")
    public static class idAndUnit {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "화폐 단위")
        String currencyUnit;
    }
}
