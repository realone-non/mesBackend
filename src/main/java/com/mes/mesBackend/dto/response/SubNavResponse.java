package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SubNavResponse {
    Long id;
    String name;
    int level;
}
