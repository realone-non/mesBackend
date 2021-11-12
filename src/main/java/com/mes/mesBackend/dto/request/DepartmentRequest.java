package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@ApiModel(description = "부서")
public class DepartmentRequest {

    @ApiModelProperty(value = "부서코드 NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int deptCode;

    @ApiModelProperty(value = "부서명 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String deptName;

    @ApiModelProperty(value = "부서약어명 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String shortName;

    @ApiModelProperty(value = "순번 NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int orders;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;
}
