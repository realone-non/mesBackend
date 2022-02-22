package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "라우팅")
public class RoutingResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "라우팅 번호")
    String routingNo;

    @Schema(description = "라우팅 명")
    String routingName;

    @Schema(description = "사용여부")
    boolean useYn;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String routingName;
    }
}
