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
@ApiModel(description = "품목파일")
public class ItemFileRequest {
    @ApiModelProperty(value = "VER")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    int version;

    @ApiModelProperty(value = "등록자")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long user;
}
