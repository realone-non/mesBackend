package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;

// 17-2. 설비고장 수리내역 등록 수리항목
@Getter
@Setter
@Schema(description = "수리항목")
public class RepairItemRequest {
    @Schema(description = "수리코드")
    @NotBlank(message = NOT_EMPTY)
    String repairCode;

    @Schema(description = "수리내용")
    @NotBlank(message = NOT_EMPTY)
    String repairContent;
}
