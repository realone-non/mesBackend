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
@Schema(description = "작업장별 세부 점검항목")
public class WorkCenterCheckDetailResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "점검코드")
    String checkCode;

    @Schema(description = "점검항목")
    String checkCategory;

    @Schema(description = "점검내용")
    String checkContent;

    @Schema(description = "판정기준")
    String checkCriteria;

    @Schema(description = "판정방법")
    String checkMethod;

    @Schema(description = "입력방법")
    String inputType;

    @Schema(description = "숫자입력포맷")
    String inputNumberFormat;

    @Schema(description = "상한값")
    String usl;

    @Schema(description = "하한값")
    String lsl;

    @Schema(description = "표시순서")
    int orders;

    @Schema(description = "사용여부")
    boolean useYn;

    // ============ 수정 기록
    @Schema(description = "사번")
    String userCode;
    @Schema(description = "수정일자")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS, timezone = ASIA_SEOUL)
    LocalDateTime updateDate;
    @Schema(description = "유저권한레벨")
    int userLevel;

    // ============ 생성 기록
    @Schema(description = "사번")
    String insertUserCode;
    @Schema(description = "생성일자")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS, timezone = ASIA_SEOUL)
    LocalDateTime insertDate;
    @Schema(description = "생성 유저권한레벨")
    int insertUserLevel;

    // 수정 기록
    public void modifiedLog(ModifiedLog modifiedLog) {
        setUserCode(modifiedLog.getUserCode());
        setUpdateDate(modifiedLog.getDate());
        setUserLevel(modifiedLog.getUserLevel());
    }

    // 생성 기록
    public void insertLog(ModifiedLog modifiedLog) {
        setInsertUserCode(modifiedLog.getUserCode());
        setInsertDate(modifiedLog.getDate());
        setInsertUserLevel(modifiedLog.getUserLevel());
    }
}
