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
@ApiModel(description = "작업공정")
public class WorkProcessResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "작업공정코드")
    CodeResponse.idAndCode workProcessCode;        // 작업공정코드

    @ApiModelProperty(value = "작업공정명")
    String workProcessName;     // 작업공정명

    @ApiModelProperty(value = "공정검사")
    boolean processTest;        // 공정검사

    @ApiModelProperty(value = "공정순번")
    int orders;              // 공정순번

    @ApiModelProperty(value = "사용여부")
    boolean useYn;              // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String workProcessName;     // 작업공정명
    }
}
