package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Schema(description = "GAUGE 유형")
public class GaugeTypeResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "GAUGE 유형")
    String gaugeType;

    @Schema(description = "사용")
    boolean useYn;
}
