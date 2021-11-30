package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "GAUGE 유형")
public class GaugeTypeRequest {
    @Schema(description = "GAUGE 유형")
    @NotBlank(message = NOT_NULL)
    String gaugeType;

    @Schema(description = "사용")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
