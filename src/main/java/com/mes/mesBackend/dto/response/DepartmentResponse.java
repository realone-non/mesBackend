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
@ApiModel(description = "부서")
public class DepartmentResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "부서코드")
    int deptCode;

    @ApiModelProperty(value = "부서명")
    String deptName;

    @ApiModelProperty(value = "부서약어명")
    String shortName;

    @ApiModelProperty(value = "순번")
    int orders;

    @ApiModelProperty(value = "사용여부")
    boolean useYn = true;
}
