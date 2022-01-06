package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM;

@Getter
@Setter
@Schema(description = "14-2.검사등록")
@JsonInclude(NON_NULL)
public class InputTestRequestInfoResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "요청일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = "Asia/Seoul")
    LocalDateTime requestDate;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "등록유형")
    EnrollmentType enrollmentType;

    @Schema(description = "입고번호")
    Long purchaseInputNo;   // purchaseInputId

    @Schema(description = "발주번호")
    String purchaseOrderNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "고객사 품번")
    String itemClientPartNo;

    @Schema(description = "제조사 품번")
    String itemManufacturerPartNo;

    @Schema(description = "고객사")
    String clientName;

    @Schema(description = "제조사")
    String manufacturerName;

    @Schema(description = "품목형태")
    String itemForm;        // 품목형태

    @Schema(description = "검사방법")
    String testProcess;

    @Schema(description = "검사기준")
    String testCriteria;

    @Schema(description = "창고")
    String warehouse;

    @Schema(description = "검사수량")
    int testAmount;

    @Schema(description = "재고수량")
    int stockAmount;

    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "검사유형")
    TestType testType;      // 다시 검토

    @Schema(description = "긴급여부")
    boolean urgentYn;

    @Schema(description = "시험성적서")
    boolean testReportYn;

    @Schema(description = "COC")
    boolean coc;
}
