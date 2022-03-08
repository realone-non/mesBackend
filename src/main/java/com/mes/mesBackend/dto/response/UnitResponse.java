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
@Schema(description = "단위")
public class UnitResponse {
    @Schema(description = "고유아이디")
    Long id;                         // 단위 고유아이디

    @Schema(description = "단위코드")
    String unitCode;                 // 단위코드

    @Schema(description = "단위명")
    String unitCodeName;             // 단위명

    @Schema(description = "심볼")
    String symbol;                   // 심볼

    @Schema(description = "기본단위")
    String defaultUnit;              // 기본단위

    @Schema(description = "base 대비 율")
    String baseScale;                   // base 대비 율

    @Schema(description = "소수점자리수")
    int decimalPoint;                // 소수점자리수

    @Schema(description = "소진여부")
    boolean exhaustYn;

    @Schema(description = "사용여부")
    boolean useYn = true;            // 사용여부

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
        String unitCodeName;
    }

    public void modifiedLog(ModifiedLog modifiedLog) {
        setUserCode(modifiedLog.getUserCode());
        setUpdateDate(modifiedLog.getDate());
        setUserLevel(modifiedLog.getUserLevel());
    }
}
