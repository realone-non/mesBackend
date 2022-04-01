package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "구매입고 LOT 정보")
@JsonInclude(NON_NULL)
public class PurchaseInputDetailResponse {
    @Schema(description = "구매입고상세 고유아이디")
    Long id;

    @Schema(description = "LOT 고유아이디")
    Long lotId;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "입고수량")
    int inputAmount;

    @Schema(description = "입고금액")
    int inputPrice;

    @Schema(description = "부가세")
    double vat;

    @Schema(description = "검사유형")
    TestType testType;      // item.testType

    @Schema(description = "제조일자")
    LocalDate manufactureDate;

    @Schema(description = "유효일자")
    LocalDate validDate;

    @Schema(description = "검사기준")
    String testCriteria;

    @Schema(description = "검사방법")
    InspectionType inspectionType;     // item.inspectionType

    @Schema(description = "긴급여부")
    boolean inputTestYn;

    @Schema(description = "시험성적서")
    boolean testReportYn;

    @Schema(description = "COC")
    boolean coc;

    @Schema(description = "거래처 LOT 번호")
    String clientLotNo;

    @Schema(description = "LOT 타입")
    String lotType;
}
