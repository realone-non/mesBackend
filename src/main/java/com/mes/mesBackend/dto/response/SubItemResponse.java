package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "대체품")
public class SubItemResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품목 고유아이디")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "대체품 고유아이디")
    Long subItemId;

    @Schema(description = "대체품번")
    String subItemNo;

    @Schema(description = "대체품명")
    String subItemName;

    @Schema(description = "대체순번")
    int subOrders;

    @Schema(description = "사용여부")
    boolean useYn;
}
