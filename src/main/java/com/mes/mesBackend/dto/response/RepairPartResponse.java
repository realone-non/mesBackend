package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 17-2. 설비고장 수리내역 등록 수리부품정보
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "수리부품정보")
public class RepairPartResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "수리부품")
    String repairPart;

    @Schema(description = "수리부품명")
    String repairPartName;

    @Schema(description = "수량")
    int amount;

    @Schema(description = "비고")
    String note;
}
