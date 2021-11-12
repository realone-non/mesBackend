package com.mes.mesBackend.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "헤더")
public class HeaderResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "헤더")
    String header;

    @ApiModelProperty(value = "컨트롤러 이름")
    String controllerName;

    @ApiModelProperty(value = "컬럼명")
    String columnName;

    @ApiModelProperty(value = "순서")
    int seq;

    @ApiModelProperty(value = "그리드 옵션")
    GridOptionResponse gridOptionResponse;
}
