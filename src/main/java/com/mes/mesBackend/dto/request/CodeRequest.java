package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "코드")
public class CodeRequest {
    @Schema(description = "코드")
    @NotBlank(message = NOT_NULL)
    String code;

    @Schema(description = "코드명")
    @NotBlank(message = NOT_NULL)
    String codeName;

    @Schema(description = "설명")
    String description;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
