package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Getter
@Setter
@Schema(description = "제조오더 디테일")
@JsonInclude(NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProduceOrderDetailResponse {
    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "품목계정")
    String itemAccount;

    @Schema(description = "BOM수량")
    int bomAmount;

    @Schema(description = "투입공정")
    String workProcess;

    @Schema(description = "예약수량")
    int reservationAmount;   // bom의 투입수량*수주 수량

    @Schema(description = "오더단위")
    String orderUnit;
}
