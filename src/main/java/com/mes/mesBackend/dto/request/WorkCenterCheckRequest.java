package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkCenterCheckRequest {
    Long workCenter;      // 작업장코드
    Long checkType;        // 점검유형
}
