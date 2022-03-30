package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema
public class RecycleResponse {
    @Schema(description = "공정 고유번호")
    Long workProcessId;

    @Schema(description = "재사용 고유번호")
    Long recycleId;

    @Schema(description = "재사용 제목")
    String recycleName;

    @Schema(description = "LOT번호")
    String lotNo;
}
