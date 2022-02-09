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
import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "외주생산의뢰정보")
public class OutsourcingProductionRequestRequest {
    @Schema(description = "BOM")
    Long bomId;

    @Schema(description = "생산수량")
    int productionAmount;

    @Schema(description = "자재출고 요청일자")
    LocalDate materialRequestDate;

    @Schema(description = "납기일시")
    LocalDate periodDate;

    @Schema(description = "수입검사여부")
    boolean inputTestYn;

    @Schema(description = "비고")
    String note;
}
