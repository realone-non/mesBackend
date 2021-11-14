package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "작업라인")
public class WorkLineResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "라인코드")
    CodeResponse.idAndCode workLineCode;              // 라인코드

    @Schema(description = "작업라인명")
    String workLineName;                    // 작업라인명

    @Schema(description = "작업장")
    WorkCenterResponse.idAndName workCenter;          // 작업장

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;        // 작업공정

    @Schema(description = "POP 시작 FORMID")
    String popStartFormid;                  // POP 시작 FORMID

    @Schema(description = "일 가동시간")
    int time;                               // 일 가동시간

    @Schema(description = "사용여부")
    boolean useYn;
}
