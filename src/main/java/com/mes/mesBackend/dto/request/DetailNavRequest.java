package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Setter
@Getter
@ApiModel(description = "디테일 네비게이션")
public class DetailNavRequest {
    @ApiModelProperty(value = "네비게이션 이름")
    @NotBlank(message = NOT_NULL)
    String name;

    @ApiModelProperty(value = "유저레벨")
    @NotNull(message = NOT_NULL)
    int level;

    @ApiModelProperty(value = "출력순번")
    @NotNull(message = NOT_NULL)
    int orders;

    @ApiModelProperty(value = "경로주소")
    @NotBlank(message = NOT_NULL)
    String path;

    @ApiModelProperty(value = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
