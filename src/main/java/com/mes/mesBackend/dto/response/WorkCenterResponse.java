package com.mes.mesBackend.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class WorkCenterResponse {
    Long id;
    CodeResponse.idAndCode workCenterCode;       // 작업장코드
    String workCenterName;             // 작업장명
    ClientResponse.idAndName outCompany;     // 외주사(거래처)
    String costCenter;                 // Cost Center
    boolean useYn = true;              // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String workCenterName;             // 작업장명
    }
}
