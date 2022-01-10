package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

// 17-1. 설비점검 실적 등록 상세
@Getter
@Setter
@Schema(description = "17-1. 설비점검 실적 상세")
public class EquipmentCheckDetailRequest {
    @Schema(description = "고유아이디(설비보전항목)")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long equipmentMaintenanceId;

    @Schema(description = "점검내용")
    @NotBlank(message = NOT_EMPTY)
    String checkContent;

    @Schema(description = "판정기준")
    @NotBlank(message = NOT_EMPTY)
    String criteriaStandard;

    @Schema(description = "판정방법")
    @NotBlank(message = NOT_EMPTY)
    String criteriaMethod;

    @Schema(description = "상한값")
    @NotNull(message = NOT_NULL)
    float usl;

    @Schema(description = "하한값")
    @NotNull(message = NOT_NULL)
    float lsl;

    @Schema(description = "결과값")
    @NotBlank(message = NOT_EMPTY)
    String result;

    @Schema(description = "등록유형")
    @NotBlank(message = NOT_EMPTY)
    String registerType;
}
