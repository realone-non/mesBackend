package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "1. 자재입고 -> 구매입고 정보")
@JsonInclude(NON_NULL)
public class PopPurchaseOrderResponse {
    // 구매발주 고유아이디
    @Schema(description = "구매발주 고유아이디")
    Long purchaseOrderId;

    // 발주번호
    @Schema(description = "구매발주 번호")
    String purchaseOrderNo;

    // 거래처
    @Schema(description = "구매발주 거래처")
    String purchaseOrderClient;

    // 발주일자
    @Schema(description = "구매발주 일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate purchaseOrderDate;

    @JsonIgnore
    OrderState orderState;
}
