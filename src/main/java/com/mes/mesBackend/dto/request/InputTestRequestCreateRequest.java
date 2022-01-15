package com.mes.mesBackend.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "14-1.검사의뢰 등록")
@JsonInclude(NON_NULL)
public class InputTestRequestCreateRequest {
    @Schema(description = "lot id")
    Long lotId;

    @Schema(description = "요청유형")
    TestType requestType;

    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "검사유형")
    TestType testType;
}