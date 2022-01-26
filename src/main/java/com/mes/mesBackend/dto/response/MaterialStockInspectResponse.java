package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "부품재고실사의뢰")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialStockInspectResponse {
    @Schema(description = "요청번호, ID")
    Long id;

    @Schema(description = "창고이름")
    String wareHouse;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "LOT유형")
    String lotType;

    @Schema(description = "LOT번호")
    String lotNo;

    @Schema(description = "DB수량")
    int dbAmount;

    @Schema(description = "실사수량")
    int inspectAmount;

    @Schema(description = "차이량")
    int differenceAmount;

    @Schema(description = "승인자 ID")
    Long userId;

    @Schema(description = "승인자 이름")
    String userName;

    @Schema(description = "승인일시")
    LocalDate approvalDate;

    @Schema(description = "품목 계정")
    String itemAccount;

    @Schema(description = "비고")
    String note;
}
