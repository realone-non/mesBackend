package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "외주생산의뢰정보")
public class OutsourcingProductionResponse {
    @Schema(description = "고유번호")
    Long id;

    @Schema(description = "외주처")
    String clientName;

    @Schema(description = "생산품번")
    String itemNo;

    @Schema(description = "생산품명")
    String itemName;

    @Schema(description = "BOM 번호")
    int bomNo;

    @Schema(description = "생산요청일자")
    LocalDate productionDate;

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
