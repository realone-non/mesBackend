package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitResponse {
    Long id;                         // 단위 고유아이디
    String unitCode;                 // 단위코드
    String unitCodeName;             // 단위명
    String symbol;                   // 심볼
    String defaultUnit;              // 기본단위
    String baseScale;                   // base 대비 율
    int decimalPoint;                // 소수점자리수
    boolean useYn = true;            // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String unitCodeName;
    }
}
