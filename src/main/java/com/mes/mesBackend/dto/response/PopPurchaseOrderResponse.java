package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

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

    @Schema(description = "발주 상태")
    OrderState orderState;
}
