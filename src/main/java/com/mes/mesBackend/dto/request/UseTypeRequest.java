package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;

@Getter
@Setter
@ApiModel(description = "용도 유형")
public class UseTypeRequest {
    @ApiModelProperty(value = "용도 유형 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String useType;
}
