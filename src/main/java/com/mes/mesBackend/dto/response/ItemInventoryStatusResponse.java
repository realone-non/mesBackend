package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// 품목계정별 품목 재고 현황
@Getter
@Setter
@Schema(description = "품목계정 별 품목 재고 현황")
public class ItemInventoryStatusResponse {
    @Schema(description = "품목 고유아이디")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "재고수량")
    int stockAmount;
}
