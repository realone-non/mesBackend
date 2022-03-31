package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.mes.mesBackend.helper.Constants.DECIMAL_POINT_2;

@Getter
@Setter
@Schema(description = "작업지시 불량률 정보")
public class WorkOrderBadItemStatusResponse {
    @Schema(description = "작업지시 고유아이디")
    Long workOrderId;

    @Schema(description = "작업지시번호")
    String workOrderNo;

    @Schema(description = "작업공정 고유아이디")
    Long workProcessId;

    @Schema(description = "작업공정")
    String workProcessName;

    @Schema(description = "작업자 고유아이디")
    Long userId;

    @Schema(description = "작업자")
    String userKorName;

    @JsonIgnore
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "생산수량")
    int productionAmount;

    @Schema(description = "양품수량")
    int stockAmount;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "불량률")
    String badRatePerProductionAmount;

    public void setRateCalculationAndItem(BadItemWorkOrderResponse.subDto subDto) {
        setItemNo(subDto.getItemNo());
        setItemName(subDto.getItemName());
        setBadItemAmount(subDto.getBadAmount());
        setStockAmount(subDto.getCreateAmount() - subDto.getBadAmount());
        // 둘째짜리까지 보여주고 반올림(String.format 으로 소수점 자르면 자동으로 반올림 됨)
        setBadRatePerProductionAmount(String.format(DECIMAL_POINT_2, (float) badItemAmount / productionAmount * 100));
    }
}
