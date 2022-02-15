package com.mes.mesBackend.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "14-1.검사의뢰 등록")
@JsonInclude(NON_NULL)
public class InputTestRequestUpdateRequest {
    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "검사방법")
    InspectionType inspectionType;

    @Schema(description = "검사완료요청일")
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate testCompletionRequestDate;
}
