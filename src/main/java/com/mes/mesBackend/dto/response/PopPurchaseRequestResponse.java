package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "1. 자재입고 -> 자재입고 / 구매입고 정보")
@JsonInclude(NON_NULL)
public class PopPurchaseRequestResponse {
    // 구매요청 고유아이디
    @Schema(description = "구매요청 고유아이디")
    Long purchaseRequestId;

    // 품목 고유아이디
    @Schema(name = "품목 고유아이디")
    Long itemId;

    // 품번
    @Schema(name = "품번")
    String itemNo;

    // 품명
    @Schema(description = "품명")
    String itemName;

    // 요청수량
    @Schema(description = "요청수량")
    int purchaseRequestAmount;

    // 입고된 수량
    @Schema(description = "입고수량")
    int purchaseInputAmount;
}
