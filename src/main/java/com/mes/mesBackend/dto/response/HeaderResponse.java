package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderResponse {
    String header;              // 헤더
    String controllerName;      // 컨트롤러 이름ㄴ
    String columnName;          // 컬럼명
    int seq;                    // 순서
    boolean useYn;  // 사용여부
}
