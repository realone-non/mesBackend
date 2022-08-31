package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// pop-작업지시 목록(공정)
@Getter
@Setter
@Schema(description = "pop-작업지시 목록(공정")
@JsonInclude(NON_NULL)
public class PopWorkOrderResponse {
    @Schema(description = "작업지시 고유아이디")
    Long workOrderId;

    @Schema(description = "작업지시 번호")
    String workOrderNo;

    @Schema(description = "품목 고유아이디")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "지시수량")
    int orderAmount;

    @Schema(description = "완료수량(생산수량)")
    int productAmount;

    // (양품수량 추가)
    @Schema(description = "양품수량")
    int stockAmount;

    @Schema(description = "단위코드")
    String unitCode;

    @Schema(description = "지시상태")
    OrderState orderState;

    @JsonIgnore
    Long produceOrderItemId;
}
