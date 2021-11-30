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
    @NotBlank(message = NOT_NULL)
    String equipmentCode;

    @Schema(description = "설비명")
    @NotBlank(message = NOT_NULL)
    String equipmentName;

    @Schema(description = "설비유형")
    String equipmentType;

    @Schema(description = "규격&모델")
    @NotBlank(message = NOT_NULL)
    String model;

    @Schema(description = "구매처 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long client;

    @Schema(description = "구매일자")
    @NotNull(message = NOT_NULL)
    LocalDateTime purchaseDate;

    @Schema(description = "구입금액")
    @NotNull(message = NOT_NULL)
    int purchaseAmount;

    @Schema(description = "생산업체명")
    @NotBlank(message = NOT_NULL)
    String maker;

    @Schema(description = "시리얼번호")
    String serialNo;

    @Schema(description = "생산개시일자")
    @NotNull(message = NOT_NULL)
    LocalDateTime startDate;

    @Schema(description = "작업라인 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workLine;

    @Schema(description = "점검주기")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int checkCycle;

    @Schema(description = "사용")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
