package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemFileRequest {
    int version;           // VER
    Long user; // 등록자
}
