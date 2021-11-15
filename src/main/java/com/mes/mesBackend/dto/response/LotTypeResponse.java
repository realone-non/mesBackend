package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Lot 유형")
public class LotTypeResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "Lot 유형")
    String lotType;
}
