package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

// 9-6. 구매입고 반품 등록
@Getter
@Setter
@Schema(description = "구매입고 반품 등록")
public class PurchaseInputReturnCreateRequest {
    @Schema(description = "lot master id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long lotMasterId;

    @Schema(description = "반품일시")
    @NotNull(message = NOT_NULL)
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate returnDate;

    @Schema(description = "반품수량")
    @NotNull(message = NOT_NULL)
    int returnAmount;

    @Schema(description = "반품가능수량")
    @NotNull(message = NOT_NULL)
    int returnPossibleAmount;

    @Schema(description = "비고")
    String note;

    @Schema(description = "반품구분(true: 정상품, false: 불량품")
    @NotNull(message = NOT_NULL)
    boolean returnDivision;
}
