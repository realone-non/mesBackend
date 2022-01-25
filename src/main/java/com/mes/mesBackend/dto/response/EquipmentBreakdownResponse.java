package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.BreakReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.*;

// 17-2. 설비고장 수리내역 등록 설비고장
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "설비 고장")
public class EquipmentBreakdownResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "고장발생일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate breakdownDate;

    @Schema(description = "설비 고유아이디")
    Long equipmentId;

    @Schema(description = "설비코드")
    String equipmentCode;

    @Schema(description = "설비명")
    String equipmentName;

    @Schema(description = "설비유형")
    String equipmentType;

    @Schema(description = "신고일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime reportDate;

    @Schema(description = "요청시고장유형")
    String requestBreakType;

    @Schema(description = "요청비고")
    String breakNote;

    @Schema(description = "고장유형 [LIGHT_BREAKDOWN: 경고장, MIDDLE_FAILURE: 중고장, FATAL_BREAKDOWN: 치명고장]")
    BreakReason breakReason;

    @Schema(description = "고장원인")
    String causeOfFailure;

    @Schema(description = "보전원 도착일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime arrivalDate;

    @Schema(description = "수리시작일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime repairStartDate;

    @Schema(description = "수리종료일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime repairEndDate;

    @Schema(description = "비고")
    String note;

    @Schema(description = "수리전 이미지파일")
    List<EquipmentBreakdownFileResponse> beforeFiles;

    @Schema(description = "수리후 이미지파일")
    List<EquipmentBreakdownFileResponse> afterFiles;

    @Schema(description = "작업장 고유아이디")
    Long workCenterId;

    @Schema(description = "작업장명")
    String workCenterName;
}
