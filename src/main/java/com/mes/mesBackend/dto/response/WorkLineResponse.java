package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.ModifiedLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM_SS;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "작업라인")
public class WorkLineResponse {
    @Schema(description = "고유아이디")
    Long id;

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

    // ============ 수정 기록1
    @Schema(description = "사번")
    String userCode;
    @Schema(description = "수정일자")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS, timezone = ASIA_SEOUL)
    LocalDateTime updateDate;
    @Schema(description = "유저권한레벨")
    int userLevel;


    @Getter
    @Setter
    @JsonInclude(NON_NULL)
    @Schema(description = "작업라인")
    public static class workLineAndWorkCenterAndWorkProcess {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "작업라인명")
        String workLineName;                    // 작업라인명

        @Schema(description = "작업장")
        String workCenterName;          // 작업장

//        @Schema(description = "작업공정")
//        String workProcessName;        // 작업공정
    }

    @Getter
    @Setter
    @Schema(description = "작업라인")
    public static class idAndName {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "작업라인명")
        String workLineName;
    }

    public void modifiedLog(ModifiedLog modifiedLog) {
        setUserCode(modifiedLog.getUserCode());
        setUpdateDate(modifiedLog.getDate());
        setUserLevel(modifiedLog.getUserLevel());
    }
}
