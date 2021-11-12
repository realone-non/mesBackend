package com.mes.mesBackend.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@ApiModel(description = "작업장별 세부 점검항목")
public class WorkCenterCheckDetailRequest {

    @ApiModelProperty(value = "점검항목 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String checkCategory;

    @ApiModelProperty(value = "점검내용 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String checkContent;

    @ApiModelProperty(value = "판정기준 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String checkCriteria;

    @ApiModelProperty(value = "판정방법 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String checkMethod;

    @ApiModelProperty(value = "입력방법 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String inputType;

    @ApiModelProperty(value = "숫자입력포맷")
    String inputNumberFormat;

    @ApiModelProperty(value = "상한값")
    float usl;

    @ApiModelProperty(value = "하한값")
    float lsl;

    @ApiModelProperty(value = "표시순서 NOT NULL")
    @NotNull(message = NOT_NULL)
    @Min(value = ID_VALUE, message = NOT_ZERO)
    int orders;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
