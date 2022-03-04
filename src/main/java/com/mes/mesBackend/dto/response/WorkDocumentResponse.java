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
@Schema(description = "작업표준서")
@JsonInclude(NON_NULL)
public class WorkDocumentResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    @Schema(description = "작업라인")
    WorkLineResponse.idAndName workLine;

    @Schema(description = "품목")
    ItemResponse.noAndName item;

    @Schema(description = "순번")
    int orders;

    @Schema(description = "파일명")
    String fileNameUrl;

    @Schema(description = "비고")
    String note;

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
