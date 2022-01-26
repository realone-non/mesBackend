package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.CalibrationMethod;
import com.mes.mesBackend.entity.enumeration.CalibrationResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

// 17-5. 계측기 검교정 실적 등록
@Getter
@Setter
@Schema(description = "계측기 검교정 실적")
public class MeasureCalibrationRequest {
    @Schema(description = "계측기 고유아이디")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long measureId;

    @Schema(description = "검교정방법 [IN_HOUSE: 사내R&R, OUT_COMPANY: 사외]")
    @NotNull(message = NOT_NULL)
    CalibrationMethod calibrationMethod;

    @Schema(description = "검교정 의뢰처(부서) id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long requestDepartmentId;

    @Schema(description = "요청일자")
    @NotNull(message = NOT_NULL)
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate requestDate;

    @Schema(description = "검교정일자")
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate calibrationDate;

    @Schema(description = "검교정 결과 [PASS: 합격, FAIL: 불합격, RETEST: 재검정]")
    CalibrationResult calibrationResult;

    @Schema(description = "검교정비용")
    int price;

    @Schema(description = "비고")
    String note;
}
