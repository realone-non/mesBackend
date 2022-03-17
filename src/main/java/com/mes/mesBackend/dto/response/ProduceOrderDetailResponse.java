package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.DECIMAL_POINT_2;


@Getter
@Setter
@Schema(description = "제조오더 디테일")
@JsonInclude(NON_NULL)
public class ProduceOrderDetailResponse {

    Long bomItemDetailId;
    @Schema(description = "품목 id")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "품목계정")
    String itemAccount;

    @Schema(description = "BOM수량")
    float bomAmount;

    @Schema(description = "투입공정")
    String workProcess;

    @Schema(description = "예약수량")
    String reservationAmount;

    @Schema(description = "오더단위")
    String orderUnit;

    @JsonIgnore
    GoodsType goodsType;

    // 예약수량 메서드
    // bom의 투입수량 * 수주 수량
    public void setReservationAmount(float bomAmount, int contractItemAmount) {
        setReservationAmount(String.format(DECIMAL_POINT_2, bomAmount * contractItemAmount));
    }
}
