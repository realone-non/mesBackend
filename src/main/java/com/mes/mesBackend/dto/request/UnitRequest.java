package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;
import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "단위")
public class UnitRequest {

    @Schema(description = "단위코드")
    @NotBlank(message = NOT_EMPTY)
    String unitCode;    // 단위코드

    @Schema(description = "단위명")
    @NotBlank(message = NOT_EMPTY)
    String unitCodeName;    // 단위명

    @Schema(description = "심볼")
    String symbol;      // 심볼

    @Schema(description = "기본단위")
    String defaultUnit; // 기본단위

    @Schema(description = "base 대비 율")
    @NotNull(message = NOT_NULL)
    float baseScale;      // base대비 율

    @Schema(description = "소수점자리수")
    @NotNull(message = NOT_NULL)
    int decimalPoint;   // 소수점자리수

    @Schema(description = "소진여부")
    @NotNull(message = NOT_NULL)
    boolean exhaustYn;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;      // 사용여부
}
