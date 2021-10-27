package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitRequest {
    String unitCode;    // 단위코드
    String unitCodeName;    // 단위명
    String symbol;      // 심볼
    String defaultUnit; // 기본단위
    float baseScale;      // base대비 율
    int decimalPoint;   // 소수점자리수
    boolean useYn = true;      // 사용여부
}
