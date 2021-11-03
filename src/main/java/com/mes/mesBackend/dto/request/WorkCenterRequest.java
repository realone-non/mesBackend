package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkCenterRequest {
    Long workCenterCode;        // 작업장 코드 id
    String workCenterName;      // 작업장명
    Long outCompany;            // 외주사(거래처)
    String costCenter;          // Cost Center
    boolean useYn = true;   // 사용여부
}
