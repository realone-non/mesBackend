package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Setter
@Getter
@Schema(description = "헤더")
public class HeaderRequest {
    @Schema(description = "헤더")
    @NotBlank(message = NOT_NULL)
    String header;

    @Schema(description = "컨트롤러 이름")
    @NotBlank(message = NOT_NULL)
    String controllerName;

    @Schema(description = "컬럼명")
    @NotBlank(message = NOT_NULL)
    String columnName;

    @Schema(description = "순서")
    @NotNull(message = NOT_NULL)
    @Min(value = ID_VALUE, message = NOT_ZERO)
    int seq;
}
