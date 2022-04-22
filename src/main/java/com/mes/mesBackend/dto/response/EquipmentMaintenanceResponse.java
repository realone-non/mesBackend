package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "설비 보전항목")
@JsonInclude(NON_NULL)
public class EquipmentMaintenanceResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "보전항목코드")
    String maintenanceCode;

    @Schema(description = "보전항목명")
    String maintenanceName;

    @Schema(description = "사용")
    boolean useYn;

    @Schema(description = "상위항목")
    String parent;

    @Schema(description = "상위항목명")
    String parentName;
}
