package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "14-2.검사 정보 상세")
public class InputTestDetailRequest {
    @Schema(description = "검사일시")
    @NotNull(message = NOT_NULL)
    LocalDate testDate;

    @Schema(description = "검사수량")
    @NotNull(message = NOT_NULL)
    int testAmount;

    @Schema(description = "양품수량")
    @NotNull(message = NOT_NULL)
    int fairQualityAmount;

    @Schema(description = "부적합수량")
    @NotNull(message = NOT_NULL)
    int incongruityAmount;

    @Schema(description = "검사결과")
    @NotNull(message = NOT_NULL)
    boolean testResult;

//    @Schema(description = "입고창고 id")
//    @NotNull(message = NOT_NULL)
//    @Min(value = ONE_VALUE, message = NOT_ZERO)
//    Long warehouseId;

    @Schema(description = "검사자 id")
    @NotNull(message = NOT_NULL)
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long userId;

    @Schema(description = "비고")
    String note;
}
