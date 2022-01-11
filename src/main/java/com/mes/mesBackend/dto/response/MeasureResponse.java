package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "계측기")
public class MeasureResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "GAUGE코드")
    String gaugeCode;

    @Schema(description = "GAUGE명")
    String gaugeName;

    @Schema(description = "GAUGE유형")
    GaugeTypeResponse gaugeType;

    @Schema(description = "규격&모델")
    String model;

    @Schema(description = "사용부서")
    DepartmentResponse.idAndName department;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "구매일자")
    LocalDateTime purchaseDate;

    @Schema(description = "생산업체명")
    String maker;

    @Schema(description = "검교정주기")
    Long calibrationCycle;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "최종 검교정일자")
    LocalDateTime calibrationLastDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "차기 검교정일자")
    LocalDateTime calibrationNextDate;

    @Schema(description = "사용여부")
    boolean useYn;  // 사용여부
}
