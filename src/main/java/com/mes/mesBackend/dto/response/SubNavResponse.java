package com.mes.mesBackend.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel(description = "서브 네비게이션")
public class SubNavResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "네비게이션 이름")
    String name;

    @ApiModelProperty(value = "유저레벨")
    int level;

    @ApiModelProperty(value = "출력순번")
    int orders;
}
