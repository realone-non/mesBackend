package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.ProcessStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.*;

@Getter
@Setter
@Schema(description = "pop-작업지시 상태값 조회")
@JsonInclude(NON_NULL)
public class PopWorkOrderStates {
    @Schema(description = "lotMaster 고유아이디")
    Long lotMasterId;

    @Schema(description = "설비 명")
    String equipmentName;

    @Schema(description = "진행 상태")
    ProcessStatus processStatus;

    @Schema(description = "업데이트 일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS, timezone = ASIA_SEOUL)
    LocalDateTime updateDateTime;

    @Schema(description = "작업 수량")
    int createdAmount;

    @Schema(description = "원료혼합 공정에서 선택한 충진공정 설비")
    Long fillingEquipmentCode;
}
