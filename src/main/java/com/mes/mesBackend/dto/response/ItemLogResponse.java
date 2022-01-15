package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Getter
@Setter
@Schema(description = "수불부")
public class ItemLogResponse {
    @Schema(description = "창고")
    String warehouse;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "전일재고")
    int beforeStock;

    @Schema(description = "입고수량")
    int storeAmount;

    @Schema(description = "생산수량")
    int createdAmount;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "투입수량")
    int inputAmount;

    @Schema(description = "출하수량")
    int shipmentAmount;

    @Schema(description = "재고실사수량")
    int stockRealAmount;

    @Schema(description = "이동수량")
    int moveAmount;

    @Schema(description = "반품수량")
    int returnAmount;

    @Schema(description = "재고수량")
    int stockAmount;
}
