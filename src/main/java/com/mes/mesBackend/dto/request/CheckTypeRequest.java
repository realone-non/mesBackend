package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "점검유형")
public class CheckTypeRequest {
    @Schema(description = "점검유형")
    @NotBlank(message = NOT_NULL)
    String checkType;
}
