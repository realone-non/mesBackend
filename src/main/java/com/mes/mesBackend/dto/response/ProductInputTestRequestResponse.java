package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM;

@Getter
@Setter
@Schema(description = "16-1. 검사의뢰 등록")
@JsonInclude(NON_NULL)
public class ProductInputTestRequestResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "검사유형")
    TestType testType;

    @Schema(description = "LOT 고유아이디")
    Long lotId;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "지시번호")
    String workOrderNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "제조사 품번")
    String itemManufacturerPartNo;

    @Schema(description = "거래처")
    String clientName;

    @Schema(description = "창고")
    String wareHouseName;

    @Schema(description = "품목형태")
    String itemForm;        // 품목형태

    @Schema(description = "요청유형")
    TestType requestType;

    @Schema(description = "요청일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = "Asia/Seoul")
    LocalDateTime requestDate;


    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "검사수량")
    int testAmount;
}
