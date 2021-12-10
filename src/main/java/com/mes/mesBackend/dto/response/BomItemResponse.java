package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "BOM 품목")
public class BomItemResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "레벨")
    int level;

    @Schema(description = "품번, 품명, 제조사, 제조사품번, 단위, 위치, 단가, 품목계정")
    ItemResponse.itemToBomResponse item;

    @Schema(description = "수량")
    int amount;

    @Schema(description = "구매처")
    ClientResponse.idAndName toBuy;

    @Schema(description = "금액")
    int price;      // 금액

    @Schema(description = "공정")
    WorkProcessResponse.idAndName workProcess;

    @Schema(description = "사용")
    boolean useYn;

    @Schema(description = "단위")
    UnitResponse.idAndName unit;

    @Schema(description = "비고")
    String note;
}
