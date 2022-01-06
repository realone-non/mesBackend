package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "외주생산 원재료 출고대상 정보")
public class OutsourcingMaterialReleaseRequest {

    @Schema(description = "BomItem Detail id")
    Long itemDetailId;

    @Schema(description = "출고요청량")
    int outputRequestAmount;

    @Schema(description = "출고요청량")
    int outputAmount;

    @Schema(description = "외주생산의뢰 id")
    Long requestId;
}
