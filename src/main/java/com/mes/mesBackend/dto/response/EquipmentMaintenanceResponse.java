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
@Schema(description = "설비 보전항목")
@JsonInclude(NON_NULL)
public class EquipmentMaintenanceResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "보전항목코드")
    String maintenanceCode;

    @Schema(description = "보전항목명")
    String maintenanceName;

    @Schema(description = "사용")
    boolean useYn;

    @Schema(description = "상위항목")
    String parent;

    @Schema(description = "상위항목명")
    String parentName;

    // ============ 수정 기록
    @Schema(description = "사번")
    String userCode;
    @Schema(description = "수정일자")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS, timezone = ASIA_SEOUL)
    LocalDateTime updateDate;
    @Schema(description = "유저권한레벨")
    int userLevel;

    public void modifiedLog(ModifiedLog modifiedLog) {
        setUserCode(modifiedLog.getUserCode());
        setUpdateDate(modifiedLog.getDate());
        setUserLevel(modifiedLog.getUserLevel());
    }
}
