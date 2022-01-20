package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 11-4. 재고현황 조회, 13-1. 재고현황 조회
@Getter
@Setter
@Schema(description = "부품 창고, 제품 창고 재고 현황 조회")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehouseInventoryResponse {
    @Schema(description = "품목코드")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "창고ID")
    Long warehouseId;

    @Schema(description = "재고수량")
    int amount;
}
