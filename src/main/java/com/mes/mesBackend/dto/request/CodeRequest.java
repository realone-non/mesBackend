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
@ApiModel(description = "코드")
public class CodeRequest {
    @ApiModelProperty(value = "코드 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String code;

    @ApiModelProperty(value = "코드명 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String codeName;

    @ApiModelProperty(value = "설명")
    String description;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
