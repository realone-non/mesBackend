package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;
import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@ApiModel(description = "단위")
public class UnitRequest {

    @ApiModelProperty(value = "단위코드 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String unitCode;    // 단위코드

    @ApiModelProperty(value = "단위명 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String unitCodeName;    // 단위명

    @ApiModelProperty(value = "심볼")
    String symbol;      // 심볼

    @ApiModelProperty(value = "기본단위")
    String defaultUnit; // 기본단위

    @ApiModelProperty(value = "base 대비 율 NOT NULL")
    @NotNull(message = NOT_NULL)
    float baseScale;      // base대비 율

    @ApiModelProperty(value = "소수점자리수 NOT NULL")
    @NotNull(message = NOT_NULL)
    int decimalPoint;   // 소수점자리수

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;      // 사용여부
}
