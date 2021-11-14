package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;

@Getter
@Setter
@Schema(description = "창고유형")
public class WareHouseTypeRequest {
    @Schema(description = "창고유형")
    @NotBlank(message = NOT_EMPTY)
    String name;    // 창고유형
}
