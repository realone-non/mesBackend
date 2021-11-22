package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "계측기")
public class MeasureResponse {
    @Schema(description = "GAUGE코드")
    String gaugeCode;       // GAUGE코드

    @Schema(description = "GAUGE명")
    String gaugeName;       // GAUGE명

    @Schema(description = "GAUGE유형")
    Long gaugeType;       // GAUGE유형

    @Schema(description = "규격&모델")
    String model;           // 규격&모델

    @Schema(description = "사용부서")
    Long department;      // 사용부서

    @Schema(description = "구매일자")
    LocalDateTime purchaseDate;     // 구매일자

    @Schema(description = "생산업체명")
    String maker;               // 생산업체명

    @Schema(description = "검교정주기")
    Long calibrationCycle;      // 검교정주기

    @Schema(description = "최종 검교정일자")
    LocalDateTime calibrationLastDate;      // 최종 검교정일자

    @Schema(description = "최종 검교정일자")
    LocalDateTime calibrationNextDate;      // 최종 검교정일자

    @Schema(description = "사용여부")
    boolean useYn;  // 사용여부
}
