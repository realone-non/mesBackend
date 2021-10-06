package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HeaderRequest {
    String header;
    String controllerName;
    String columnName;
    int seq;
}
