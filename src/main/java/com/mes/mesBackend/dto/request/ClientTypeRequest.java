package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientTypeRequest {
    String name;
    boolean useYn;  // 사용여부
}
