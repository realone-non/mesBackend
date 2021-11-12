package com.mes.mesBackend.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "창고유형")
public class WareHouseTypeResponse {

    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "창고유형")
    String name;
}
