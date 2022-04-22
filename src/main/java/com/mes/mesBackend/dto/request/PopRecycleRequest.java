package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "(POP)재사용 등록")
public class PopRecycleRequest {
    @Schema(description = "품목 고유번호")
    Long itemId;

    @Schema(description = "재사용 고유번호")
    Long recycleId;

    @Schema(description = "수량")
    int amount;

    @Schema(description = "공정")
    WorkProcessDivision workProcessDivision;
}
