package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "수주 품목")
@JsonInclude(NON_NULL)
public class ContractItemResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품목 id")
    ItemResponse.noAndNameAndStandardAndUnitAndPrice item;      // 품번, 품명, 규격, 수주단위, 단가

    @Schema(description = "수주수량")
    int amount;     // 수주수량

    @Schema(description = "수주금액")
    int contractAmount;

    @Schema(description = "수주금액(원화)")
    int contractAmountWon;

    @Schema(description = "부가세")
    double surtax;

    @Schema(description = "수주유형")
    ContractType contractType;

    @Schema(description = "납기일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate periodDate;       // 납기일자

    @Schema(description = "고객발주번호")
    String clientOrderNo;

    @Schema(description = "규격화 품번")
    String standardItemNo;      // 규격화 품번

    @Schema(description = "비고")
    String note;        // 비고


    @Getter
    @Setter
    @Schema(description = "수주 품목")
    @JsonInclude(NON_NULL)
    public static class toProduceOrder {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "품번")
        String itemNo;

        @Schema(description = "품명")
        String itemName;

        @Schema(description = "수주수량")
        int amount;     // 수주수량
    }

}
