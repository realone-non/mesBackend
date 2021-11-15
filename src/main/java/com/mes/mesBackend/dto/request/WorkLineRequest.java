package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "작업라인")
public class WorkLineRequest {

    @Schema(description = "라인코드 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workLineCode;           // 라인코드

    @Schema(description = "작업라인명")
    @NotBlank(message = NOT_EMPTY)
    String workLineName;          // 작업라인명

    @Schema(description = "작업장 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workCenter;             // 작업장

    @Schema(description = "작업공정 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workProcess;            // 작업공정

    @Schema(description = "POP 시작 FORMID")
    @NotBlank(message = NOT_EMPTY)
    String popStartFormid;      // POP 시작 FORMID

    @Schema(description = "일 가동시간")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int time;                   // 일 가동시간

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
