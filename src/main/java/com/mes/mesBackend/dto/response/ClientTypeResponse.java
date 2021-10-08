package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientTypeResponse {
    Long id;
    String name;
    boolean useYn;  // 사용여부
}
