package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.RecycleType;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema
public class RecycleRequest {
    @Schema(description = "공정 구분 값")
    WorkProcessDivision workProcessDivision;

    @Schema(description = "재사용 제목")
    String recycleName;

    @Schema(description = "재사용 타입")
    RecycleType recycleType;

    @Schema(description = "사용여부")
    boolean useYn;

}
