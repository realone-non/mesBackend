package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.*;

// 9-3. 발주현황조회
@Getter
@Setter
@Schema(description = "발주현황조회")
@JsonInclude(NON_NULL)
public class PurchaseOrderStatusResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "발주번호")
    String purchaseOrderNo;

    @Schema(description = "거래처")
    String clientCode;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "제조사 품번")
    String itemManufacturerPartNo;

    @Schema(description = "발주수량")
    int orderAmount;

    @Schema(description = "발주단위")
    String orderUnitCodeName;

    @Schema(description = "단가")
    int unitPrice;

    @Schema(description = "발주금액")
    int orderPrice;

    @Schema(description = "VAT")
    double vat;

    @Schema(description = "구매납기일자")
    LocalDate orderPeriodDate;

    @Schema(description = "담당자")
    String userName;

    @Schema(description = "비고")
    String note;

    @Schema(description = "입고창고")
    String wareHouseName;

    @Schema(description = "화폐")
    String currencyUnit;

    @Schema(description = "취소수량")
    int cancelAmount;

    @Schema(description = "수주정보")
    String contractInfo;        // 보류

    @Schema(description = "지시상태")
    OrderState orderState;

    @Schema(description = "수주일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS, timezone = ASIA_SEOUL)
    LocalDateTime createdDate;
}
