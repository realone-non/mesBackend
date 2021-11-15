package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "작업장별 세부 점검항목")
public class WorkCenterCheckDetailResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "점검항목")
    String checkCategory;

    @Schema(description = "점검내용")
    String checkContent;

    @Schema(description = "판정기준")
    String checkCriteria;

    @Schema(description = "판정방법")
    String checkMethod;

    @Schema(description = "입력방법")
    String inputType;

    @Schema(description = "숫자입력포맷")
    String inputNumberFormat;

    @Schema(description = "상한값")
    String usl;

    @Schema(description = "하한값")
    String lsl;

    @Schema(description = "표시순서")
    int orders;

    @Schema(description = "사용여부")
    boolean useYn;
}
