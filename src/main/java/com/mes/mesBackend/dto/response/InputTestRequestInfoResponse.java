package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.entity.enumeration.InputTestDivision.*;
import static com.mes.mesBackend.helper.Constants.*;

// 14-2. 검사등록
// 15-2. 검사등록
@Getter
@Setter
@Schema(description = "검사등록")
@JsonInclude(NON_NULL)
public class InputTestRequestInfoResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "요청일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime requestDate;

    @JsonIgnore
    Long lotMasterId;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "등록유형 [불량: ERROR, 구매입고: PURCHASE_INPUT, 생산: PRODUCTION, 분할: SPLIT]")
    EnrollmentType enrollmentType;

    @Schema(description = "구매입고 입고번호")
    Long purchaseInputNo;   // purchaseInputId

    @Schema(description = "외주입고 입고번호")
    Long outsourcingInputNo;

    @Schema(description = "구매입고 발주번호")
    String purchaseOrderNo;

    @Schema(description = "외주입고 발주번호")
    Long outsourcingProductionRequestNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "품목 규격")
    String itemStandard;

//    @Schema(description = "고객사 품번")
//    String itemClientPartNo;

    @Schema(description = "제조사 품번")
    String itemManufacturerPartNo;

//    @Schema(description = "고객사")
//    String clientName;

    @Schema(description = "제조사")
    String manufacturerName;

    @Schema(description = "품목형태")
    String itemForm;

    @Schema(description = "검사기준")
    String testCriteria;

    @Schema(description = "창고")
    String warehouse;

    @Schema(description = "검사수량")
    int testAmount;

//    @Schema(description = "재고수량")
//    int stockAmount;      0118 없애라고 하심

    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "긴급여부")
    Boolean urgentYn;

    @Schema(description = "시험성적서")
    Boolean testReportYn;

    @Schema(description = "COC")
    Boolean coc;

    @Schema(description = "검사완료요청일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate testCompletionRequestDate;

    @Schema(description = "지시번호")
    String workOrderNo;

    @Schema(description = "요청유형")
    TestType testType;   // item.testType

    @Schema(description = "검사방법")
    InspectionType inspectionType;  // inputTestRequest.inspectionType

    public InputTestRequestInfoResponse division(InputTestDivision inputTestDivision) {
        if (inputTestDivision.equals(PART)) {
            setOutsourcingInputNo(null);
            setOutsourcingProductionRequestNo(null);
            setTestCompletionRequestDate(null);
            setWorkOrderNo(null);
        }
        else if (inputTestDivision.equals(OUT_SOURCING)){
            setPurchaseInputNo(null);
            setPurchaseOrderNo(null);
            setTestCompletionRequestDate(null);
            setWorkOrderNo(null);
            setUrgentYn(null);
            setTestReportYn(null);
            setCoc(null);
        } else if (inputTestDivision.equals(PRODUCT)) {
            setOutsourcingProductionRequestNo(null);
            setOutsourcingInputNo(null);
            setPurchaseInputNo(null);
            setPurchaseOrderNo(null);
            setItemManufacturerPartNo(null);
            setLotMasterId(null);
            setUrgentYn(null);
            setTestReportYn(null);
            setCoc(null);
            setTestType(null);
            setInspectionType(null);
        }
        return this;
    }
}
