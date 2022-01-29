package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "(pop)재사용")
public class PopRecycleResponse {
    @Schema(description = "품목 고유번호")
    Long id;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "불량수")
    int badAmount;

    @Schema(description = "재사용수")
    int recycleAmount;
}
