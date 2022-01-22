package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.entity.enumeration.InputTestDivision.OUT_SOURCING;
import static com.mes.mesBackend.entity.enumeration.InputTestDivision.PRODUCT;

@Getter
@Setter
@Schema(description = "14-2.검사 정보 상세")
@JsonInclude(NON_NULL)
public class InputTestDetailResponse {
    @Schema(description = "검사번호(고유아이디)")
    Long id;

    @Schema(description = "검사일시")
    LocalDate testDate;

    @Schema(description = "검사수량")
    int testAmount;

    @Schema(description = "양품수량")
    int fairQualityAmount;

    @Schema(description = "부적합수량")
    int incongruityAmount;

    @Schema(description = "검사결과")
    boolean testResult;

    @Schema(description = "입고창고 id")
    Long warehouseId;

    @Schema(description = "입고창고")
    String warehouse;

    @Schema(description = "검사성적서 파일 url")
    String testReportFileUrl;

    @Schema(description = "COC 파일 url")
    String cocFileUrl;

    @Schema(description = "검사자 id")
    Long userId;

    @Schema(description = "검사자")
    String user;

    @Schema(description = "비고")
    String note;

    public InputTestDetailResponse division(InputTestDivision inputTestDivision) {
        if (inputTestDivision.equals(PRODUCT) || inputTestDivision.equals(OUT_SOURCING)) {
            setCocFileUrl(null);
            setTestReportFileUrl(null);
        }
        return this;
    }
}
