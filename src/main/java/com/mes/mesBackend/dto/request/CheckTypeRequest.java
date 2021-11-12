package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@ApiModel(description = "점검유형")
public class CheckTypeRequest {
    @ApiModelProperty(value = "점검유형")
    @NotBlank(message = NOT_NULL)
    String checkType;
}
