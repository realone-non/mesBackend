package com.mes.mesBackend.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "작업장별 점검항목")
public class WorkCenterCheckResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "작업장코드")
    WorkCenterResponse.idAndName workCenter;      // 작업장코드

    @ApiModelProperty(value = "점검유형")
    CheckTypeResponse checkType;                  // 점검유형
}
