package com.mes.mesBackend.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "작업장별 세부 점검항목")
public class WorkCenterCheckDetailRequest {

    @Schema(description = "점검항목")
    @NotBlank(message = NOT_EMPTY)
    String checkCategory;

    @Schema(description = "점검내용")
    @NotBlank(message = NOT_EMPTY)
    String checkContent;

    @Schema(description = "판정기준")
    @NotBlank(message = NOT_EMPTY)
    String checkCriteria;

    @Schema(description = "판정방법")
    @NotBlank(message = NOT_EMPTY)
    String checkMethod;

    @Schema(description = "입력방법")
    @NotBlank(message = NOT_EMPTY)
    String inputType;

    @Schema(description = "숫자입력포맷")
    String inputNumberFormat;

    @Schema(description = "상한값")
    float usl;

    @Schema(description = "하한값")
    float lsl;

    @Schema(description = "표시순서")
    @NotNull(message = NOT_NULL)
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    int orders;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
