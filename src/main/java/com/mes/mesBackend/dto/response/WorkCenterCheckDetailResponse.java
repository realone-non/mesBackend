package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class WorkCenterCheckDetailResponse {
    Long id;
    String checkCategory;   // 점검항목
    String checkContent;    // 점검내용
    String checkCriteria;        // 판정기준
    String checkMethod;        // 판정방법
    String inputType;       // 입력방법
    String inputNumberFormat;   // 숫자입력포맷
    String usl;        // 상한값
    String lsl;        // 하한값
    int orders;     // 표시순서
    boolean useYn;   // 사용여부
}
