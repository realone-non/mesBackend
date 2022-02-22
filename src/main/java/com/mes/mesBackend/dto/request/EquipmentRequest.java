package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "설비")
public class EquipmentRequest {
    @Schema(description = "설비코드")
    String equipmentCode;

    @Schema(description = "설비명")
    @NotBlank(message = NOT_NULL)
    String equipmentName;

    @Schema(description = "설비유형")
    String equipmentType;

    @Schema(description = "규격&모델")
    String model;

    @Schema(description = "구매처 id")
    Long client;

    @Schema(description = "구매일자")
    LocalDateTime purchaseDate;

    @Schema(description = "구입금액")
    int purchaseAmount;

    @Schema(description = "생산업체명")
    String maker;

    @Schema(description = "시리얼번호")
    String serialNo;

    @Schema(description = "생산개시일자")
    LocalDateTime startDate;

    @Schema(description = "작업라인 id")
    Long workLine;

    @Schema(description = "점검주기")
    int checkCycle;

    @Schema(description = "사용")
    @NotNull(message = NOT_NULL)
    boolean useYn;

    @Schema(description = "작업공정")
    Long workProcessId;
}
