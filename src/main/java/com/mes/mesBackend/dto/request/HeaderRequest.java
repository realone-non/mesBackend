package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Setter
@Getter
@ApiModel(description = "헤더")
public class HeaderRequest {
    @ApiModelProperty(value = "헤더")
    @NotBlank(message = NOT_NULL)
    String header;

    @ApiModelProperty(value = "컨트롤러 이름")
    @NotBlank(message = NOT_NULL)
    String controllerName;

    @ApiModelProperty(value = "컬럼명")
    @NotBlank(message = NOT_NULL)
    String columnName;

    @ApiModelProperty(value = "순서")
    @NotNull(message = NOT_NULL)
    @Min(value = ID_VALUE, message = NOT_ZERO)
    int seq;
}
