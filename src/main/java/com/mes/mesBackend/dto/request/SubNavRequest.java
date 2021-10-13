package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubNavRequest {
    String name;
    int level;
    boolean useYn;
}
