package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@ApiModel(description = "거래처 타입")
public class ClientTypeRequest {

    @ApiModelProperty(value = "타입 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String name;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;  // 사용여부
}
