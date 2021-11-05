package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetailNavRequest {
    String name;
    int level;
    boolean useYn;
    int orders;
    String path;
}
