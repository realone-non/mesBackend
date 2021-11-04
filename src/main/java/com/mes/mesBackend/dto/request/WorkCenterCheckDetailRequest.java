package com.mes.mesBackend.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkCenterCheckDetailRequest {
    String checkCategory;   // 점검항목
    String checkContent;    // 점검내용
    String checkCriteria;        // 판정기준
    String checkMethod;        // 판정방법
    String inputType;       // 입력방법
    String inputNumberFormat;   // 숫자입력포맷
    float usl;        // 상한값
    float lsl;        // 하한값
    int orders;     // 표시순서
    boolean useYn;   // 사용여부
}
