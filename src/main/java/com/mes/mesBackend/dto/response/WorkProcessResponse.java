package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.ModifiedLog;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
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
@Schema(description = "작업공정")
public class WorkProcessResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업공정명")
    String workProcessName;     // 작업공정명

    @Schema(description = "공정검사")
    boolean processTest;        // 공정검사

    @Schema(description = "공정순번")
    int orders;              // 공정순번

    @Schema(description = "사용여부")
    boolean useYn;              // 사용여부

    @JsonIgnore
    boolean recycleYn;

    @Schema(description = "공정 구분")
    WorkProcessDivision workProcessDivision;

    // ============ 수정 기록
    @Schema(description = "사번")
    String userCode;
    @Schema(description = "수정일자")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS, timezone = ASIA_SEOUL)
    LocalDateTime updateDate;
    @Schema(description = "유저권한레벨")
    int userLevel;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String workProcessName;     // 작업공정명
    }

    public void modifiedLog(ModifiedLog modifiedLog) {
        setUserCode(modifiedLog.getUserCode());
        setUpdateDate(modifiedLog.getModifiedDate());
        setUserLevel(modifiedLog.getUserLevel());
    }
}
