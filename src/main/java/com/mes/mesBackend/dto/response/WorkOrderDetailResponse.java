package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.*;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "작업지시 상세")
public class WorkOrderDetailResponse {
    @Schema(description = "설비 LOT 고유아이디")
    Long equipmentLotId;

    @Schema(description = "작업지시 고유아이디")
    Long workOrderId;

    @Schema(description = "작업자")
    String userKorName;

    @Schema(description = "작업일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime workDateTime;

    @Schema(description = "작업수량")
    int productionAmount;

    @Schema(description = "양품수량")
    int stockAmount;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "공정 고유아이디")
    Long workProcessId;

    @Schema(description = "공정")
    String workProcess;
}
