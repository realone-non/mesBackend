package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;

// 수리코드
@Getter
@Setter
@Schema(description = "수리코드")
public class RepairCodeRequest {
    @Schema(description = "수리코드")
    @NotBlank(message = NOT_EMPTY)
    String repairCode;

    @Schema(description = "수리내용")
    @NotBlank(message = NOT_EMPTY)
    String repairContent;
}
