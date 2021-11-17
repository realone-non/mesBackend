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

    @Schema(description = "품목")
    ItemResponse.toSubItem item;

    @Schema(description = "대체품목")
    ItemResponse.toSubItem subItem;

    @Schema(description = "대체순번")
    int subOrders;

    @Schema(description = "사용여부")
    boolean useYn;
}
