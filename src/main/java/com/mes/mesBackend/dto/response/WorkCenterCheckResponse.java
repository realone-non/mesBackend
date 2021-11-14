package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "작업장별 점검항목")
public class WorkCenterCheckResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업장코드")
    WorkCenterResponse.idAndName workCenter;      // 작업장코드

    @Schema(description = "점검유형")
    CheckTypeResponse checkType;                  // 점검유형
}
