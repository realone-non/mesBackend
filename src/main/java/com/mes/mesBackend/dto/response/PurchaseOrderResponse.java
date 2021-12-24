package com.mes.mesBackend.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "구매발주")
@JsonInclude(NON_NULL)
public class PurchaseOrderResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "발주번호")
    String purchaseOrderNo;

    @Schema(description = "발주일자")
    LocalDate purchaseOrderDate;

    @Schema(description = "거래처 고유아이디")
    Long clientId;
    @Schema(description = "거래처")
    String clientCode;
    @Schema(description = "거래처명")
    String clientName;


    @Schema(description = "담당자 고유아이디")
    Long userId;
    @Schema(description = "담당자")
    String userName;

    @Schema(description = "입고창고 고유아이디")
    Long wareHouseId;
    @Schema(description = "입고창고")
    String wareHouseName;

    @Schema(description = "화폐 고유아이디")
    Long currencyId;
    @Schema(description = "화폐")
    String currencyUnit;
    @Schema(description = "환율")
    float currencyExchangeRate;

    @Schema(description = "부가세적용")
    boolean surtaxYn;

    @Schema(description = "구매납기일자")
    LocalDate periodDate;   // 9-1. 구매납기일자

    @Schema(description = "수주정보")
    String contractInfo;        // 수주정보

    @Schema(description = "지시상태")
    OrderState orderState;

    @Schema(description = "비고")
    String note;

    @Schema(description = "운송조건")
    String transportCondition;

    @Schema(description = "별첨")
    String addendum;

    @Schema(description = "운송유형")
    String transportType;

    @Schema(description = "지불조건")
    String payCondition;

    @Schema(description = "Requested Shipping(w)")
    String requestedShipping;

    @Schema(description = "특이사항")
    String specialNote;

    public PurchaseOrderResponse orderStateCondition() {
        return this.orderState.equals(OrderState.COMPLETION) ? this : null;
    }

    public void setOrderStateAndPeriodDateAndContractInfo(OrderState orderState, LocalDate periodDate) {
        this.orderState = orderState;
        this.periodDate = periodDate;
    }
}
