package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetailNavResponse {
    Long id;
    String name;
    int level;
    int orders;
    String path;
}
