package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.StockInspectionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "재고실사의뢰 등록")
public class MaterialStockInspectRequestRequest {
    @Schema(description = "비고")
    String note;

    @Schema(description = "창고ID")
    Long warehouseId;

    @Schema(description = "품목그룹ID")
    Long itemAccountId;

    @Schema(description = "재고실사기준 [전수: ALL, 품목계정: ITEM_ACCOUNT]")
    StockInspectionType inspectionType;
}
