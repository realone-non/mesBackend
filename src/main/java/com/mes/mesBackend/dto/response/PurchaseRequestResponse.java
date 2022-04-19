package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "구매요청")
@JsonInclude(NON_NULL)
public class PurchaseRequestResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "제조오더 고유아이디")
    Long produceOrderId;

    @Schema(description = "수주처")
    String contractClient;

    @Schema(description = "제조오더번호")
    String produceOrderNo;

    @Schema(description = "제조오더번호 + 수주품목")
    String produceOrderNoAndItemName;

    @Schema(description = "품목 고유아이디")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "제조사 품번")
    String itemManufacturerPartNo;

    @Schema(description = "구매단위")
    String itemUnitCodeName;

    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    @Schema(description = "요청일자")
    LocalDate requestDate;

    @Schema(description = "요청수량")
    int requestAmount;

    @Schema(description = "발주수량")
    int orderAmount;

    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    @Schema(description = "구매납기일자(구매요청 납기일자)")
    LocalDate purchasePeriodDate;

    @Schema(description = "검사유형")
    TestType testType;  // item.testType

    @Schema(description = "제조사")
    String itemManufacturerName;

    @Schema(description = "비고")
    String note;

    @Schema(description = "모델 품번")
    String modelItemNo;

    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    @Schema(description = "납기일자(수주의 납기일자)")
    LocalDate periodDate;

    @Schema(description = "수입검사여부")
    boolean inputTestYn;

    @JsonIgnore
    String contractItemItemName;
}
