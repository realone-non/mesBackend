package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkProcessResponse {
    Long id;
    CodeResponse.idAndCode workProcessCode;        // 작업공정코드
    String workProcessName;     // 작업공정명
    boolean processTest;        // 공정검사
    int orders;              // 공정순번
    boolean useYn;              // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String workProcessName;     // 작업공정명
    }
}
