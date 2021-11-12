package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "단위")
public class UnitResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;                         // 단위 고유아이디

    @ApiModelProperty(value = "단위코드")
    String unitCode;                 // 단위코드

    @ApiModelProperty(value = "단위명")
    String unitCodeName;             // 단위명

    @ApiModelProperty(value = "심볼")
    String symbol;                   // 심볼

    @ApiModelProperty(value = "기본단위")
    String defaultUnit;              // 기본단위

    @ApiModelProperty(value = "base 대비 율")
    String baseScale;                   // base 대비 율

    @ApiModelProperty(value = "소수점자리수")
    int decimalPoint;                // 소수점자리수

    @ApiModelProperty(value = "사용여부")
    boolean useYn = true;            // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String unitCodeName;
    }
}
