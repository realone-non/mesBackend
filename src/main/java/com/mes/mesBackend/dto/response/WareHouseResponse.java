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
@ApiModel(description = "창고")
public class WareHouseResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "창고코드")
    String wareHouseCode;

    @ApiModelProperty(value = "창고명")
    String wareHouseName;

    @ApiModelProperty(value = "창고유형")
    WareHouseTypeResponse wareHouseType;

    @ApiModelProperty(value = "창고그룹")
    String wareHouseGroup;

    @ApiModelProperty(value = "사용여부")
    boolean useYn;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String wareHouseName;   // 창고명
    }
}
