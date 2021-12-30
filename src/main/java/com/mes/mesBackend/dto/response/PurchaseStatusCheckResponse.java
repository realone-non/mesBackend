package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

// 9-4. 구매현황 조회
@Getter
@Setter
@Schema(description = "구매현황 조회")
@JsonInclude(NON_NULL)
public class PurchaseStatusCheckResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "입고일시")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = "Asia/Seoul")
    LocalDateTime inputDate;

    @Schema(description = "거래처")
    String clientCode;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "발주번호")
    String purchaseOrderNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "제조사품번")
    String itemManufacturerPartNo;

    @Schema(description = "입고수량")
    int inputAmount;

    @Schema(description = "입고금액")
    int inputPrice;

    @Schema(description = "발주단위")
    String orderUnitCodeName;

    @Schema(description = "부가세")
    double vat;

    @Schema(description = "LOT 번호")
    String lotNo;
}
