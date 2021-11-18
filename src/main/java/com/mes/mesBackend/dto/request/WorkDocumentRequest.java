package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter @Setter
@Schema(description = "작업표준서")
public class WorkDocumentRequest {

    @Schema(description = "작업공정 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workProcess;

    @Schema(description = "작업라인 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workLine;

    @Schema(description = "생산품번 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long item;

    @Schema(description = "순번")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int orders;

    @Schema(description = "비고")
    String note;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}