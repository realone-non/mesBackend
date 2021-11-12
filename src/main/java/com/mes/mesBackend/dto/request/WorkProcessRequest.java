package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.exception.Message.NOT_ZERO;

@Getter
@Setter
@ApiModel(description = "작업공정")
public class WorkProcessRequest {
    @ApiModelProperty(value = "작업공정코드 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workProcessCode;

    @ApiModelProperty(value = "작업공정명 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String workProcessName;

    @ApiModelProperty(value = "공정검사 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean processTest;

    @ApiModelProperty(value = "공정순번 NOT NULL")
    @NotNull(message = NOT_NULL)
    int orders;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
