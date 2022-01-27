package com.mes.mesBackend.dto.response;

import com.mes.mesBackend.entity.enumeration.StockInspectionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "부품재고실사의뢰")
public class MaterialStockInspectRequestResponse {
    @Schema(description = "요청번호, ID")
    Long id;

    @Schema(description = "실사일자")
    LocalDate inspectDate;

    @Schema(description = "비고")
    String note;

    @Schema(description = "창고ID")
    Long warehouseId;

    @Schema(description = "창고유형")
    String warehouseType;

    @Schema(description = "창고")
    String warehouse;

    @Schema(description = "품목그룹 ID")
    Long itemAccountId;

    @Schema(description = "품목그룹")
    String itemAccount;

    @Schema(description = "재고실사기준 [전수: ALL, 품목계정: ITEM_ACCOUNT]")
    StockInspectionType stockInspectionType;

}
