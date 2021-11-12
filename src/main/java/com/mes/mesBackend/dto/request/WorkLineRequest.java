package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@ApiModel(description = "작업라인")
public class WorkLineRequest {

    @ApiModelProperty(value = "라인코드 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workLineCode;           // 라인코드

    @ApiModelProperty(value = "작업라인명 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String workLineName;          // 작업라인명

    @ApiModelProperty(value = "작업장 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workCenter;             // 작업장

    @ApiModelProperty(value = "작업공정 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workProcess;            // 작업공정

    @ApiModelProperty(value = "POP 시작 FORMID NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String popStartFormid;      // POP 시작 FORMID

    @ApiModelProperty(value = "일 가동시간 NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int time;                   // 일 가동시간

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
