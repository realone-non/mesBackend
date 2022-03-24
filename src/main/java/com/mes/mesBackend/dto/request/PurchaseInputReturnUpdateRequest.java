package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.NOT_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

// 9-6. 구매입고 반품 등록
@Getter
@Setter
@Schema(description = "구매입고 반품 등록")
public class PurchaseInputReturnUpdateRequest {
    @Schema(description = "반품일시")
    @NotNull(message = NOT_NULL)
    @DateTimeFormat(pattern = YYYY_MM_DD)
    LocalDate returnDate;

    @Schema(description = "반품수량")
    @NotNull(message = NOT_NULL)
    int returnAmount;

//    @Schema(description = "반품가능수량")
//    @NotNull(message = NOT_NULL)
//    int returnPossibleAmount;

    @Schema(description = "비고")
    String note;
}
