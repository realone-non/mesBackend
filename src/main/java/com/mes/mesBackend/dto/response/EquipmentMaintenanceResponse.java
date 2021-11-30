package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "설비 보전항목")
public class EquipmentMaintenanceResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "보전항목코드")
    String maintenanceCode;

    @Schema(description = "보전항목명")
    String maintenanceName;

    @Schema(description = "사용")
    boolean useYn;
}
