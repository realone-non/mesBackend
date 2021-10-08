package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryCodeRequest {
    String name;
    boolean useYn;  // 사용여부
}
