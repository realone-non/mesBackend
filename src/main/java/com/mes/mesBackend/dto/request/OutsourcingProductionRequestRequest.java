package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.exception.Message.NOT_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "외주생산의뢰정보")
public class OutsourcingProductionRequestRequest {
//    @Schema(description = "BOM")
//    Long bomId;

    @Schema(description = "품목 고유아이디")
    @NotNull(message = NOT_NULL)
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long itemId;

    @Schema(description = "생산수량")
    @NotNull(message = NOT_NULL)
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    int productionAmount;

    @Schema(description = "자재출고 요청일자")
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate materialRequestDate;

    @Schema(description = "납기일시")
    @NotNull(message = NOT_NULL)
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate periodDate;

    @Schema(description = "수입검사여부")
    @NotNull(message = NOT_NULL)
    boolean inputTestYn;

    @Schema(description = "비고")
    String note;
}
