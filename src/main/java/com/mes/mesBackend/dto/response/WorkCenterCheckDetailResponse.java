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
@ApiModel(description = "작업장별 세부 점검항목")
public class WorkCenterCheckDetailResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "점검항목")
    String checkCategory;

    @ApiModelProperty(value = "점검내용")
    String checkContent;

    @ApiModelProperty(value = "판정기준")
    String checkCriteria;

    @ApiModelProperty(value = "판정방법")
    String checkMethod;

    @ApiModelProperty(value = "입력방법")
    String inputType;

    @ApiModelProperty(value = "숫자입력포맷")
    String inputNumberFormat;

    @ApiModelProperty(value = "상한값")
    String usl;

    @ApiModelProperty(value = "하한값")
    String lsl;

    @ApiModelProperty(value = "표시순서")
    int orders;

    @ApiModelProperty(value = "사용여부")
    boolean useYn;
}
