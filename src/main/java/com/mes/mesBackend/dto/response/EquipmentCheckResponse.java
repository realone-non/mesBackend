package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 17-1. 설비정보
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "17-1. 설비정보")
public class EquipmentCheckResponse {
    @Schema(description = "설비 고유아이디")
    Long id;

    @Schema(description = "등록일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDateTime enrollmentDate;   // 설비 createdDate

    @Schema(description = "설비코드")
    String equipmentCode;

    @Schema(description = "설비명")
    String equipmentName;

    @Schema(description = "작업장")
    String workCenterName;

    @Schema(description = "작업공정")
    String workProcessName;

    @Schema(description = "작업라인명")
    String workLineName;
}
