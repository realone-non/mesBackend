package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "재고실사 등록")
public class RequestMaterialStockInspect {
    @Schema(description = "재고실사 고유아이디")
    Long id;

    @Schema(description = "실사수량")
    int inspectAmount;

    @Schema(description = "승인자 ID")
    Long userId;

    @Schema(description = "비고")
    String note;
}
