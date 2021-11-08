package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutingResponse {
    Long id;
    Long routingNo;     // 라우팅 번호
    String routingName;     // 라우팅 명
    boolean useYn;
}
