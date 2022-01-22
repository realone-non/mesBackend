package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.entity.enumeration.InputTestDivision.*;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM;

// 14-4. 검사대기 현황
// 15-4. 검사대기 현황
// 16-5. 검사대기 현황
@Getter
@Setter
@Schema(description = "검사대기 현황")
@JsonInclude(NON_NULL)
public class InputTestScheduleResponse {
    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "검사유형")
    TestType testType;

    @Schema(description = "LOT 유형")
    String lotType;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "요청일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = "Asia/Seoul")
    LocalDateTime requestDate;

    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "검사요청유형")
    TestType testRequestType;

    @Schema(description = "구매입고 납기일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate purchaseInputPeriodDate;

    @Schema(description = "외주입고 납기일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate outsourcingPeriodDate;

    @Schema(description = "창고")
    String wareHouseName;

    @Schema(description = "거래처")
    String clientName;

    @Schema(description = "요청번호(검사의뢰 고유아이디)")
    Long inputTestRequestId;

    public InputTestScheduleResponse division(InputTestDivision inputTestDivision) {
        if (inputTestDivision.equals(PART)) {
            setOutsourcingPeriodDate(null);
        } else if (inputTestDivision.equals(OUT_SOURCING)){
            setPurchaseInputPeriodDate(null);
        } else if (inputTestDivision.equals(PRODUCT)) {
            setOutsourcingPeriodDate(null);
            setPurchaseInputPeriodDate(null);
        }
        return this;
    }
}
