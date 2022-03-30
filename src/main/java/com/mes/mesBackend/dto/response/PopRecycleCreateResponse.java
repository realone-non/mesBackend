package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "(pop)재사용 생성 리턴")
public class PopRecycleCreateResponse {
    @Schema(description = "LOT번호")
    String lotNo;

    @Schema(description = "재사용수")
    int recycleAmount;
}
