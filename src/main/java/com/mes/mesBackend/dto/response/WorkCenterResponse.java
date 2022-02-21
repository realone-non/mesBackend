package com.mes.mesBackend.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "작업장")
public class WorkCenterResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업장명")
    String workCenterName;

    @Schema(description = "외주사(거래처)")
    ClientResponse.idAndName outCompany;

    @Schema(description = "Cost Center")
    String costCenter;

    @Schema(description = "사용여부")
    boolean useYn = true;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String workCenterName;             // 작업장명
    }
}
