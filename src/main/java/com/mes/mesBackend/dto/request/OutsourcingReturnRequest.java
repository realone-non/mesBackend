package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "외주반품")
public class OutsourcingReturnRequest {
    @Schema(description = "고유아이디, 출고번호")
    Long id;

    @Schema(description = "LOT 마스터 고유아이디")
    Long lotMasterId;

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

    @Schema(description = "정상품/불량품 반품 여부")
    boolean returnDivision;
}
