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
@Schema(description = "부서")
public class DepartmentRequest {

    @Schema(description = "부서코드")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int deptCode;

    @Schema(description = "부서명")
    @NotBlank(message = NOT_NULL)
    String deptName;

    @Schema(description = "부서약어명")
    @NotBlank(message = NOT_NULL)
    String shortName;

    @Schema(description = "순번")
    @NotNull(message = NOT_NULL)
    int orders;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;
}
