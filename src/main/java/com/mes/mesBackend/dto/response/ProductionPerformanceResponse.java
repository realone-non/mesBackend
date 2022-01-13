package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.*;

// 8-6. 생산실적 관리
@Getter
@Setter
@Schema(description = "생산실적 관리")
@JsonInclude(NON_NULL)
public class ProductionPerformanceResponse {
    @Schema(description = "고유아이디")
    Long id;

    // productionPerformance.workOrderDetail.produceOrder.contract.contractNo
    @Schema(description = "수주번호")
    String contractNo;

    // productionPerformance.workOrderDetail.produceOrder.produceOrderNo
    @Schema(description = "제조오더번호")
    String produceOrderNo;

    // productionPerformance.workOrderDetail.produceOrder.contract.client.clientName
    @Schema(description = "거래처")
    String clientName;

    // productionPerformance.workOrderDetail.produceOrder.contractItem.item.itemName
    @Schema(description = "품명")
    String itemName;

    // productionPerformance.workOrderDetail.produceOrder.contractItem.periodDate
    @Schema(description = "납기일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate periodDate;

    // productionPerformance.workOrderDetail.user.korName
    @Schema(description = "담당자")
    String korName;

    // productionPerformance.workOrderDetail.produceOrder.contractItem.amount
    @Schema(description = "수주량")
    int contractItemAmount;

    // productionPerformance.productionAmount
    @Schema(description = "생산량")
    int productionAmount;

    @Schema(description = "자재입고")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime materialInput;

    @Schema(description = "원료혼합")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime materialMixing;

    @Schema(description = "충진")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime filling;

    @Schema(description = "캡조립")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime capAssembly;

    @Schema(description = "라벨링")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime labeling;

    @Schema(description = "포장")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime packaging;

    @Schema(description = "출하")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime shipment;

    @Schema(description = "단가")
    int unitPrice;

    @Schema(description = "금액")
    int price;
}
