package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckTypeResponse {
    Long id;
    String checkType;       // 점검유형
}
