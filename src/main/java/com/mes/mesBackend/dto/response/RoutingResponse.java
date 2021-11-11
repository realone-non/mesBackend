package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class RoutingResponse {
    Long id;
    Long routingNo;     // 라우팅 번호
    String routingName;     // 라우팅 명
    boolean useYn;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String routingName;
    }
}
