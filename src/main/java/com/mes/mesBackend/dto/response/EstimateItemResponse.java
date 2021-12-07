package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "견적 품목")
public class EstimateItemResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품목")
    ItemResponse.noAndNameAndStandardAndUnitAndPrice item;

    @Schema(description = "수량")
    int amount;

    @Schema(description = "금액")
    int price;

    @Schema(description = "비고")
    String note;
}
