package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// pop-작업지시 상세 정보
@Getter
@Setter
@Schema(description = "pop-작업지시 상세 정보")
@JsonInclude(NON_NULL)
public class PopWorkOrderDetailResponse {
    @Schema(description = "bomItemDetail id")
    Long bomDetailId;

    @Schema(description = "품명")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "품목계정")
    String itemAccount;

    @Schema(description = "bom 수량")
    int bomAmount;

    @Schema(description = "예약수량")
    int reservationAmount;

    @JsonIgnore
    int contractItemAmount;     // 수주수량

    // 예약수량: bom 의 투입수량 * 수주 수량
//    public void setReservationAmount(int contractItemAmount) {
//        setReservationAmount(this.bomAmount * contractItemAmount);
//    }
}
