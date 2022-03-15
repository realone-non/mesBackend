package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "설비 보전항목")
public class EquipmentMaintenanceRequest {

    @Schema(description = "보전항목코드")
    @NotBlank(message = NOT_NULL)
    String maintenanceCode;

    @Schema(description = "보전항목명")
    @NotBlank(message = NOT_NULL)
    String maintenanceName;

    @Schema(description = "사용")
    @NotNull(message = NOT_NULL)
    boolean useYn;

    @Schema(description = "상위항목")
    String parent;

    @Schema(description = "상위항목명")
    String parentName;
}
