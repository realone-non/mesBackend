package com.mes.mesBackend.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(description = "검사의뢰 등록")
@JsonInclude(NON_NULL)
public class InputTestRequestCreateRequest {
    @Schema(description = "lot id")
    Long lotId;

    @Schema(description = "요청유형")   // 입력받지 않음
    TestType requestType;       // 14-1, 15-1.  해당 item 에 있는 수입검사(자동검사|수동검사) / 16-1. item 의 출하검사

    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "검사유형")
    TestType testType;

    @Schema(description = "검사완료요청일")
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate testCompletionRequestDate;
}