package com.mes.mesBackend.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "작업장")
public class WorkCenterResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "작업장코드")
    CodeResponse.idAndCode workCenterCode;

    @ApiModelProperty(value = "작업장명")
    String workCenterName;

    @ApiModelProperty(value = "외주사(거래처)")
    ClientResponse.idAndName outCompany;

    @ApiModelProperty(value = "Cost Center")
    String costCenter;

    @ApiModelProperty(value = "사용여부")
    boolean useYn = true;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String workCenterName;             // 작업장명
    }
}
