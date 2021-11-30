package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "계측기")
public class MeasureRequest {
    @Schema(description = "GAUGE코드")
    @NotBlank(message = NOT_EMPTY)
    String gaugeCode;       // GAUGE코드

    @Schema(description = "GAUGE명")
    @NotBlank(message = NOT_EMPTY)
    String gaugeName;       // GAUGE명

    @Schema(description = "GAUGE유형")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long gaugeType;       // GAUGE유형

    @Schema(description = "규격&모델")
    @NotBlank(message = NOT_EMPTY)
    String model;           // 규격&모델

    @Schema(description = "사용부서")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long department;      // 사용부서

    @Schema(description = "구매일자")
    @NotNull(message = NOT_NULL)
    LocalDateTime purchaseDate;     // 구매일자

    @Schema(description = "생산업체명")
    @NotBlank(message = NOT_EMPTY)
    String maker;               // 생산업체명

    @Schema(description = "검교정주기")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long calibrationCycle;      // 검교정주기

    @Schema(description = "최종 검교정일자")
    @NotNull(message = NOT_NULL)
    LocalDateTime calibrationLastDate;      // 최종 검교정일자

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;  // 사용여부
}
