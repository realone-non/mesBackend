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
@ApiModel(description = "국가코드")
public class CountryCodeRequest {

    @ApiModelProperty(value = "국가코드")
    @NotBlank(message = NOT_NULL)
    String name;

    @ApiModelProperty(value = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;  // 사용여부
}
