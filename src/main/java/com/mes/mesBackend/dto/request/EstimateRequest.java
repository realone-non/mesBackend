package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "견적")
public class EstimateRequest {

    @Schema(description = "거래처 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long client;

    @Schema(description = "견적일자")
    @NotNull(message = NOT_NULL)
    LocalDate estimateDate;

    @Schema(description = "화폐 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long currency;

    @Schema(description = "납기")
    @NotBlank(message = NOT_EMPTY)
    String period;

    @Schema(description = "유효기간")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int validity;

    @Schema(description = "지불조건")
    @NotBlank(message = NOT_EMPTY)
    String payCondition;

    @Schema(description = "부가세")
    @NotBlank(message = NOT_EMPTY)
    String surtax;

    @Schema(description = "운송조건")
    String transportCondition;      // 운송조건

    @Schema(description = "Forwader")
    String forwader;

    @Schema(description = "DESTINATION")
    String destination;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
