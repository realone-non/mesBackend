package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "품목별 검사항목 디테일")
public class ItemCheckDetailResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "조건코드")
    String conditionCode;       // 조건코드

    @Schema(description = "조건항목")
    String conditionItem;       // 조건항목

    @Schema(description = "조건내용")
    String conditionContent;    // 조건내용

    @Schema(description = "판정기준")
    String criteriaStandard;    // 판정기준

    @Schema(description = "판정방법")
    String criteriaMethod;      // 판정방법

    @Schema(description = "입력방법")
    String inputMethod;         // 입력방법

    @Schema(description = "숫자입력포멧")
    String inputFmt;            // 숫자입력포멧

    @Schema(description = "상한값")
    float usl;                 // 상한값

    @Schema(description = "하한값")
    float lsl;                 // 하한값

    @Schema(description = "관리상한값")
    float masterUsl;           // 관리상한값

    @Schema(description = "관리하한값")
    float masterLsl;           // 관리하한값

    @Schema(description = "상한공차")
    float uTotalVal;           // 상한공차

    @Schema(description = "하한공차")
    float lTotalVal;           // 하한공차

    @Schema(description = "중심값")
    float cl;                  // 중심값

    @Schema(description = "검사주기")
    int checkCycle;             // 검사주기

    @Schema(description = "표시순서")
    int orders;                // 표시순서

    @Schema(description = "최대차수")
    int maxCnt;                 // 최대차수

    @Schema(description = "사용여부")
    boolean useYn;   // 사용여부
}
