package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.RecycleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema
public class RecycleRequest {
    @Schema(description = "공정 고유 번호")
    Long workProcessId;

    @Schema(description = "재사용 제목")
    String recycleName;

    @Schema(description = "재사용 타입")
    RecycleType recycleType;

    @Schema(description = "사용여부")
    boolean useYn;

}
