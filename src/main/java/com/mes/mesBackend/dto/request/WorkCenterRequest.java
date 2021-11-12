package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@ApiModel(description = "작업장")
public class WorkCenterRequest {
    @ApiModelProperty(value = "작업장코드 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workCenterCode;

    @ApiModelProperty(value = "작업장명")
    String workCenterName;

    @ApiModelProperty(value = "외주사 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long outCompany;

    @ApiModelProperty(value = "Cost Center")
    String costCenter;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;
}
