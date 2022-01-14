package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "외주현황 조회")
public class OutsourcingStatusResponse {
    @Schema(description = "외주처")
    String clientName;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "입고 수량")
    long inputAmount;

    @Schema(description = "입고 반품수량")
    long stockReturnAmount;

    @Schema(description = "불량 수량")
    long badItemAmount;

    @Schema(description = "불량 반품 수량")
    long badItemReturnAmount;

    @Schema(description = "재고 수량")
    long stockAmount;
}
