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
@Schema(description = "품목그룹")
public class ItemGroupResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "상위그룹코드")
    String topGroupCode;

    @Schema(description = "상위그룹명")
    String topGroupName;

    @Schema(description = "그룹명")
    String groupName;

    @Schema(description = "기본어명")
    String defaultName;

    @Schema(description = "순번")
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

    @Getter
    @Setter
    public static class idAndGroupName {
        Long id;
        String groupName;
    }

    public void modifiedLog(ModifiedLog modifiedLog) {
        setUserCode(modifiedLog.getUserCode());
        setUpdateDate(modifiedLog.getModifiedDate());
        setUserLevel(modifiedLog.getUserLevel());
    }
}
