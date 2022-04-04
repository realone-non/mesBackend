package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "외주입고정보")
public class OutsourcingInputResponse {
    @Schema(description = "고유번호")
    Long id;

    @JsonInclude(NON_NULL)
    @Schema(description = "외주처")
    String clientName;

    @Schema(description = "생산품번")
    String itemNo;

    @Schema(description = "생산품명")
    String itemName;

    @JsonInclude(NON_NULL)
    @Schema(description = "입고일시")
    LocalDate inputDate;

    @Schema(description = "미입고수량")
    int noInputAmount;

    @Schema(description = "입고수량")
    int inputAmount;

    @JsonInclude(NON_NULL)
    @Schema(description = "입고창고")
    String warehouseName;

    @Schema(description = "수입검사여부")
    boolean inputTestYn;

    @Schema(description = "비고")
    String note;

    @Schema(description = "외주 검사의뢰 등록 품목")
    String outsourcingInputTestItemName;

    public OutsourcingInputResponse setOutSourcingInputTestItemName() {
        setOutsourcingInputTestItemName(itemName + "(" + inputDate + ")");
        return this;
    }
}
