package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// 수리코드
@Getter
@Setter
@Schema(description = "수리코드")
public class RepairCodeResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "수리코드")
    String repairCode;

    @Schema(description = "수리내용")
    String repairContent;
}
