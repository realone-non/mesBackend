package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.entity.enumeration.InputTestDivision.*;
import static com.mes.mesBackend.helper.Constants.*;

// 14-3. 검사실적조회
// 15-3. 검사실적조회
@Getter
@Setter
@Schema(description = "검사실적조회")
@JsonInclude(NON_NULL)
public class InputTestPerformanceResponse {
    @Schema(description = "검사등록 고유아이디")
    Long inputTestDetailId;

    @Schema(description = "요청일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime requestDate;

    @Schema(description = "검사일시")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate testDate;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "품목형태")
    String itemForm;

    @Schema(description = "구매입고 발주번호")
    String purchaseOrderNo;

    @Schema(description = "외주입고 발주번호")
    Long outsourcingOrderNo;

    @Schema(description = "LOT 번호")
    String lotNo;

//    @Schema(description = "고객사 품번")
//    String itemClientPartNo;

    @Schema(description = "제조사 품번")
    String itemManufacturerPartNo;

    @Schema(description = "제조사")
    String manufacturerName;

//    @Schema(description = "고객사")
//    String clientName;

    @Schema(description = "검사기준")
    String testCriteria;

    @Schema(description = "검사수량")
    int testAmount;

    @Schema(description = "양품수량")
    int fairQualityAmount;

    @Schema(description = "부적합수량")
    int incongruityAmount;

    @Schema(description = "검사자")
    String user;

    @Schema(description = "시험성적서")
    Boolean testReportYn;

    @Schema(description = "COC")
    Boolean cocYn;

    @Schema(description = "검사성적서 파일 url")
    String testReportFileUrl;

    @Schema(description = "COC 파일 url")
    String cocFileUrl;

    @Schema(description = "비고")
    String note;

    @Schema(description = "LOT 유형")
    String lotType;

    @Schema(description = "입고창고")
    String wareHouseName;

    @Schema(description = "검사유형")
    TestType testType;   // item.testType

    @Schema(description = "검사방법")
    InspectionType inspectionType;  // inputTestRequest.inspectionType

    public InputTestPerformanceResponse division(InputTestDivision inputTestDivision) {
        if (inputTestDivision.equals(PART)) {
            setOutsourcingOrderNo(null);
            setLotType(null);
            setTestType(null);
            setWareHouseName(null);
        }
        else if (inputTestDivision.equals(OUT_SOURCING)) {
            setPurchaseOrderNo(null);
            setLotType(null);
            setTestType(null);
            setWareHouseName(null);
        } else if (inputTestDivision.equals(PRODUCT)) {
            setRequestDate(null);
            setItemForm(null);
            setOutsourcingOrderNo(null);
            setPurchaseOrderNo(null);
            setItemManufacturerPartNo(null);
            setInspectionType(null);
            setTestCriteria(null);
            setTestReportFileUrl(null);
            setCocFileUrl(null);
        }
        return this;
    }
}
