package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//11-4. 재고현황 조회
@Getter
@Setter
@Schema(description = "재고현황 조회")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialStockReponse {
    @Schema(description = "품목 ID")
    Long itemId;

    @Schema(description = "창고 ID")
    Long warehouseId;

    @Schema(description = "재고량")
    int amount;

    @Schema(description = "입고단가")
    int inputUnitPrice;
}
