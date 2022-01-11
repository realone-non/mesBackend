package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;


// 17-2. 설비고장 수리내역 등록 수리부품정보
@Getter
@Setter
@Schema(description = "수리부품정보")
public class RepairPartRequest {
    @Schema(description = "수리부품")
    @NotBlank(message = NOT_EMPTY)
    String repairPart;

    @Schema(description = "수리부품명")
    @NotBlank(message = NOT_EMPTY)
    String repairPartName;

    @Schema(description = "수량")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int amount;

    @Schema(description = "비고")
    String note;
}
