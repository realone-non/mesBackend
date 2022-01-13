package com.mes.mesBackend.dto.response;

import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "외주반품")
public class OutsourcingReturnResponse {
    @Schema(description = "고유아이디, 출고번호")
    Long id;

    @Schema(description = "외주처")
    String clientName;

    @Schema(description = "LOT 마스터 고유아이디")
    Long lotMasterId;

    @Schema(description = "자재품번")
    String itemNo;

    @Schema(description = "자재품명")
    String itemName;

    @Schema(description = "LOT번호")
    String lotNo;

    @Schema(description = "LOT유형")
    String lotType;

    @Schema(description = "반품일시")
    LocalDate returnDate;

    @Schema(description = "반품수량")
    int returnAmount;

    @Schema(description = "정상품수량")
    int stockAmount;

    @Schema(description = "불량수량")
    int badAmount;

    @Schema(description = "입고창고")
    String wareHouse;

    @Schema(description = "비고")
    String note;

    @Schema(description = "정상품/불량품 반품 여부")
    boolean returnDivision;
}
