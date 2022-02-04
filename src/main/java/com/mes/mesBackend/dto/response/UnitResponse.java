package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "단위")
public class UnitResponse {
    @Schema(description = "고유아이디")
    Long id;                         // 단위 고유아이디

    @Schema(description = "단위코드")
    String unitCode;                 // 단위코드

    @Schema(description = "단위명")
    String unitCodeName;             // 단위명

    @Schema(description = "심볼")
    String symbol;                   // 심볼

    @Schema(description = "기본단위")
    String defaultUnit;              // 기본단위

    @Schema(description = "base 대비 율")
    String baseScale;                   // base 대비 율

    @Schema(description = "소수점자리수")
    int decimalPoint;                // 소수점자리수

    @Schema(description = "소진여부")
    boolean exhaustYn;

    @Schema(description = "사용여부")
    boolean useYn = true;            // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String unitCodeName;
    }
}
