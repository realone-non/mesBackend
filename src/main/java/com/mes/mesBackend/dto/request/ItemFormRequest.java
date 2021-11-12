package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@ApiModel(description = "품목형태")
public class ItemFormRequest {

    @ApiModelProperty(value = "품목형태")
    @NotBlank(message = NOT_NULL)
    String form;
}
