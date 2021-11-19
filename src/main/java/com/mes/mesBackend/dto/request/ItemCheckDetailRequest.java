package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "품목별 검사항목 디테일")
public class ItemCheckDetailRequest {

    @Schema(description = "조건코드")
    @NotBlank(message = NOT_NULL)
    String conditionCode;       // 조건코드

    @Schema(description = "조건항목")
    @NotBlank(message = NOT_NULL)
    String conditionItem;       // 조건항목

    @Schema(description = "조건내용")
    @NotBlank(message = NOT_NULL)
    String conditionContent;    // 조건내용

    @Schema(description = "판정기준")
    @NotBlank(message = NOT_NULL)
    String criteriaStandard;    // 판정기준

    @Schema(description = "판정방법")
    @NotBlank(message = NOT_NULL)
    String criteriaMethod;      // 판정방법

    @Schema(description = "입력방법")
    @NotBlank(message = NOT_NULL)
    String inputMethod;         // 입력방법

    @Schema(description = "숫자입력포멧")
    @NotBlank(message = NOT_NULL)
    String inputFmt;            // 숫자입력포멧

    @Schema(description = "상한값")
    @NotNull(message = NOT_NULL)
    float usl;                 // 상한값

    @Schema(description = "하한값")
    @NotNull(message = NOT_NULL)
    float lsl;                 // 하한값

    @Schema(description = "관리상한값")
    @NotNull(message = NOT_NULL)
    float masterUsl;           // 관리상한값

    @Schema(description = "관리하한값")
    @NotNull(message = NOT_NULL)
    float masterLsl;           // 관리하한값

    @Schema(description = "상한공차")
    @NotNull(message = NOT_NULL)
    float uTotalVal;           // 상한공차

    @Schema(description = "하한공차")
    @NotNull(message = NOT_NULL)
    float lTotalVal;           // 하한공차

    @Schema(description = "중심값")
    @NotNull(message = NOT_NULL)
    float cl;                  // 중심값

    @Schema(description = "검사주기")
    @NotNull(message = NOT_NULL)
    int checkCycle;             // 검사주기

    @Schema(description = "표시순서")
    @NotNull(message = NOT_NULL)
    int orders;                // 표시순서

    @Schema(description = "최대차수")
    @NotNull(message = NOT_NULL)
    int maxCnt;                 // 최대차수

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;   // 사용여부
}
