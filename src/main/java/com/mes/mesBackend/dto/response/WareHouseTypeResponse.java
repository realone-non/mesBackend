package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "창고유형")
public class WareHouseTypeResponse {

    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "창고유형")
    String name;
}
