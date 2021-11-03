package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkProcessRequest {
    Long workProcessCode;        // 작업공정코드
    String workProcessName;     // 작업공정명
    boolean processTest;        // 공정검사
    int orders;              // 공정순번
    boolean useYn;              // 사용여부
}
