package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 17-2. 설비고장 수리내역 등록 수리항목
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "수리항목")
public class RepairItemResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "수리코드")
    RepairCodeResponse repairCode;
}
