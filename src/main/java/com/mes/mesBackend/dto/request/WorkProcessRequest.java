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
@Schema(description = "작업공정")
public class WorkProcessRequest {
    @Schema(description = "작업공정코드 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workProcessCode;

    @Schema(description = "작업공정명")
    @NotBlank(message = NOT_EMPTY)
    String workProcessName;

    @Schema(description = "공정검사")
    @NotNull(message = NOT_NULL)
    boolean processTest;

    @Schema(description = "공정순번")
    @NotNull(message = NOT_NULL)
    int orders;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
