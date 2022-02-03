package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "외주반품")
public class OutsourcingReturnRequest {
    @Schema(description = "고유아이디, 출고번호")
    Long id;

    @NotNull(message = NOT_NULL)
    @Schema(description = "LOT 마스터 고유아이디")
    Long lotMasterId;

    @NotNull(message = NOT_NULL)
    @Schema(description = "반품일시")
    LocalDate returnDate;

    @Schema(description = "정상품반품수량")
    int stockReturnAmount;

    @Schema(description = "불량품반품수량")
    int badItemReturnAmount;

    @Schema(description = "정상품가능수량")
    int stockAmount;

    @Schema(description = "불량품가능수량")
    int badItemAmount;

    @Schema(description = "비고")
    String note;

    @NotNull(message = NOT_NULL)
    @Schema(description = "정상품/불량품 반품 여부")
    boolean returnDivision;
}
