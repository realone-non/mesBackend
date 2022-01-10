package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 17-1. 설비점검 실적 등록 상세
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "17-1. 설비점검 실적 상세")
public class EquipmentCheckDetailResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "고유아이디(설비보전항목)")
    Long maintenanceId;

    @Schema(description = "점검항목(설비보전항목 보전항목명)")
    String maintenanceName;

    @Schema(description = "점검내용")
    String checkContent;

    @Schema(description = "판정기준")
    String criteriaStandard;

    @Schema(description = "판정방법")
    String criteriaMethod;

    @Schema(description = "상한값")
    float usl;

    @Schema(description = "하한값")
    float lsl;

    @Schema(description = "결과값")
    String result;

    @Schema(description = "등록유형")
    String registerType;
}
