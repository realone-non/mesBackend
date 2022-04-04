package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// 매출관련현황
@Getter
@Setter
@Schema(description = "매출관련현황 - 수주")
public class SalesRelatedStatusResponse {
    @Schema(description = "품목 고유아이디")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "1일 ~ 8일")
    int firstWeekAmount;

    @Schema(description = "9일 ~ 16일")
    int secondWeekAmount;

    @Schema(description = "17일 ~ 24일")
    int thirdWeekAmount;

    @Schema(description = "25일 ~ 말일")
    int fourthWeekAmount;
}
