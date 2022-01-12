package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM;

// 17-4. 설비 수리부품 내역 조회
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "설비 수리부품 내역")
public class EquipmentRepairPartResponse {
    @Schema(description = "설비고장 고유아이디")
    Long equipmentBreakdownId;

    @Schema(description = "고장발생일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = "Asia/Seoul")
    LocalDate breakdownDate;

    @Schema(description = "설비코드")
    String equipmentCode;

    @Schema(description = "설비명")
    String equipmentName;

    @Schema(description = "설비유형")
    String equipmentType;

    @Schema(description = "작업장명")
    String workCenterName;

    @Schema(description = "수리시작일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = "Asia/Seoul")
    LocalDateTime repairStartDate;

    @Schema(description = "수리종료일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = "Asia/Seoul")
    LocalDateTime repairEndDate;

    @Schema(description = "수리부품")
    String repairPart;

    @Schema(description = "수리부품명")
    String repairPartName;

    @Schema(description = "수량")
    int amount;

    @Schema(description = "비고")
    String note;
}
