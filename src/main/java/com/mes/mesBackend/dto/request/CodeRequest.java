package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeRequest {
    String code;
    String codeName;
    String description;
    boolean useYn;
}
