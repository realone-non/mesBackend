package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkCenterCheckResponse {
    Long id;
    WorkCenterResponse.idAndName workCenter;      // 작업장코드
    CheckTypeResponse checkType;                  // 점검유형
}
