package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;

@Getter
@Setter
@ApiModel(description = "창고유형")
public class WareHouseTypeRequest {
    @ApiModelProperty(value = "창고유형 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String name;    // 창고유형
}
