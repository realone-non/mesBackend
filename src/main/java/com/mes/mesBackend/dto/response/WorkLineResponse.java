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
@ApiModel(description = "작업라인")
public class WorkLineResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "라인코드")
    CodeResponse.idAndCode workLineCode;              // 라인코드

    @ApiModelProperty(value = "작업라인명")
    String workLineName;                    // 작업라인명

    @ApiModelProperty(value = "작업장")
    WorkCenterResponse.idAndName workCenter;          // 작업장

    @ApiModelProperty(value = "작업공정")
    WorkProcessResponse.idAndName workProcess;        // 작업공정

    @ApiModelProperty(value = "POP 시작 FORMID")
    String popStartFormid;                  // POP 시작 FORMID

    @ApiModelProperty(value = "일 가동시간")
    int time;                               // 일 가동시간

    @ApiModelProperty(value = "사용여부")
    boolean useYn;
}
