package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;

@Getter
@Setter
public class LotTypeRequest {
    @ApiModelProperty(value = "Lot 유형 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String lotType;
}
