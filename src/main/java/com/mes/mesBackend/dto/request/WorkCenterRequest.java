package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "작업장")
public class WorkCenterRequest {
    @Schema(description = "작업장코드 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workCenterCode;

    @Schema(description = "작업장명")
    String workCenterName;

    @Schema(description = "외주사 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long outCompany;

    @Schema(description = "Cost Center")
    String costCenter;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;
}
