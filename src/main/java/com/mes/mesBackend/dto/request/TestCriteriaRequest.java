package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@ApiModel(description = "검사기준")
public class TestCriteriaRequest {
    @ApiModelProperty(value = "검사기준 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String testCriteria;
}
