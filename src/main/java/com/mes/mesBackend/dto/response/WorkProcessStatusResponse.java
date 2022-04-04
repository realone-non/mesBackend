package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// 작업공정별 정보
@Getter
@Setter
@Schema(description = "작업공정별 정보")
public class WorkProcessStatusResponse {
    @Schema(description = "작업진행")
    Long ongoingAmount;

    @Schema(description = "작업완료")
    Long completionAmount;

    @Schema(description = "수량")
    int productionAmount;
}
