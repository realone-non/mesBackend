package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.mes.mesBackend.helper.Constants.DECIMAL_POINT_2;
import static com.mes.mesBackend.helper.Constants.PERCENT;

@Getter
@Setter
@Schema(description = "작업지시 불량률 상세 정보")
public class WorkOrderBadItemStatusDetailResponse {
    @Schema(description = "불량항목 고유아이디")
    Long badItemId;

    @Schema(description = "불량항목")
    String badItemName;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "생산수량 대비 불량률")
    String badRatePerProductionAmount;

    @Schema(description = "불량수량 대비 유형불량률")
    String badRatePerBadAmount;

    // 퍼센트 계산
    public WorkOrderBadItemStatusDetailResponse setRateCalculation(int allProductionAmount, int allBadItemAmount) {
        setBadRatePerProductionAmount(String.format(DECIMAL_POINT_2, ((float) badItemAmount / allProductionAmount) * 100) + PERCENT);
        setBadRatePerBadAmount(String.format(DECIMAL_POINT_2, ((float) badItemAmount / allBadItemAmount) * 100) + PERCENT);
        return this;
    }
}
