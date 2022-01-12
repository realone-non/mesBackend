package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.CalibrationMethod;
import com.mes.mesBackend.entity.enumeration.CalibrationResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 17-5. 계측기 검교정 실적 등록
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "계측기 검교정 실적")
public class MeasureCalibrationResponse {
    @Schema(description = "계측기 검교정 실적 고유아이디")
    Long id;

    @Schema(description = "계측기 고유아이디")
    Long measureId;

    @Schema(description = "계측기코드")
    String gaugeCode;

    @Schema(description = "계측기명")
    String gaugeName;

    @Schema(description = "계측기유형")
    String gaugeType;

    @Schema(description = "검교정방법")
    CalibrationMethod calibrationMethod;

    @Schema(description = "검교정 의뢰처 id")
    Long requestDepartmentId;

    @Schema(description = "검교정 의뢰처")
    String requestDepartmentName;

    @Schema(description = "요청일자")
    LocalDate requestDate;

    @Schema(description = "검교정일자")
    LocalDate calibrationDate;

    @Schema(description = "검교정 결과")
    CalibrationResult calibrationResult;

    @Schema(description = "검교정비용")
    int price;

    @Schema(description = "성적서")
    String reportFileUrl;

    @Schema(description = "비고")
    String note;

    @Schema(description = "사용부서")
    String userDepartmentName;
}
