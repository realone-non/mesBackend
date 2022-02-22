package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "라우팅")
public class RoutingRequest {

    @Schema(description = "라우팅 번호")
    @NotBlank(message = NOT_NULL)
    String routingNo;

    @Schema(description = "라우팅 명")
    @NotBlank(message = NOT_NULL)
    String routingName;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
