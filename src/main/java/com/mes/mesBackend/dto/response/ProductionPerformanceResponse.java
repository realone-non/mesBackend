package com.mes.mesBackend.dto.response;

import com.amazonaws.services.cloudformation.model.CreateStackSetRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.*;

// 8-6. 생산실적 관리
@Getter
@Setter
@Schema(description = "생산실적 관리")
@JsonInclude(NON_NULL)
public class ProductionPerformanceResponse {
    @Schema(description = "제조오더번호")
    String produceOrderNo;

    @Schema(description = "작업공정")
    String workProcessName;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "생산량")
    int productionAmount;

    @Schema(description = "양품수량")
    int stockAmount;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "공정별 달성률")
    String achievementRate;

    @Schema(description = "소요시간(분)")
    String costTime;

    @Schema(description = "작업자")
    String userKorName;

    @Schema(description = "거래처")
    String clientName;

    @Schema(description = "작업시작")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime workOrderStartDate;

    @Schema(description = "작업종료")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime workOrderEndDate;

    @Schema(description = "지시수량")
    int orderAmount;

//    @JsonIgnore

    @JsonIgnore
    Long workProcessId;
//    @JsonIgnore


    Long workOrderId;
    WorkProcessDivision workProcessDivision;
//    @JsonIgnore
    Long dummyCostTime;


    public void set(BadItemWorkOrderResponse.subDto subDto) {
        setItemNo(subDto.getItemNo());                        // 품번
        setItemName(subDto.getItemName());                    // 품명
        setBadItemAmount(subDto.getBadAmount());              // 불량수량
        setProductionAmount(subDto.getCreateAmount());        // 생산수량
        int stockAmount = subDto.getCreateAmount() - subDto.getBadAmount();
        setStockAmount(stockAmount);                           // 양품수량
        // 공정별 달성률
        setAchievementRate(String.format(DECIMAL_POINT_2, (float) stockAmount / orderAmount * 100) + PERCENT);

        // 소요시간(분)
        if (workOrderStartDate != null && workOrderEndDate != null) {
            Long costTime = ChronoUnit.MINUTES.between(workOrderStartDate, workOrderEndDate);
            // 22.11.01 라벨링만 소요시간 변경
            if (workProcessDivision.equals(WorkProcessDivision.LABELING)) {
                setDummyCostTime((costTime - 60));
                setCostTime((costTime - 60) + "분");
            } else {
                setDummyCostTime(costTime);
                setCostTime(costTime + "분");
            }
        }
    }

//    public ProductionPerformanceResponse setList(ProductionPerformanceResponse response) {
//        if (response.getWorkOrderId() == 100 || response.getWorkOrderId() == 73 || response.getWorkOrderId() == 66 || response.getWorkOrderId() == 56 || response.getWorkOrderId() == 51) {
//            response.setWorkOrderStartDate(response.getWorkOrderStartDate().minusDays(1));
//            response.setCostTime((response.dummyCostTime * 2) + "분");
//            return response;
//        } else {
//            return response;
//        }
//    }


//    @Schema(description = "고유아이디")
//    Long id;
//
//    // productionPerformance.workOrderDetail.produceOrder.contract.contractNo
//    @Schema(description = "수주번호")
//    String contractNo;
//
//    // productionPerformance.workOrderDetail.produceOrder.produceOrderNo
//    @Schema(description = "제조오더번호")
//    String produceOrderNo;
//
//    // productionPerformance.workOrderDetail.produceOrder.contract.client.clientName
//    @Schema(description = "거래처")
//    String clientName;
//
//    // productionPerformance.workOrderDetail.produceOrder.contractItem.item.itemName
//    @Schema(description = "품명")
//    String itemName;
//
//    // productionPerformance.workOrderDetail.produceOrder.contractItem.periodDate
//    @Schema(description = "납기일자")
//    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
//    LocalDate periodDate;
//
//    // productionPerformance.workOrderDetail.user.korName
//    @Schema(description = "담당자")
//    String korName;
//
//    // productionPerformance.workOrderDetail.produceOrder.contractItem.amount
//    @Schema(description = "수주량")
//    int contractItemAmount;
//
//    // productionPerformance.productionAmount
//    @Schema(description = "생산량")
//    int productionAmount;
//
////    @Schema(description = "자재입고")
////    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
////    LocalDateTime materialInput;
//
//    @Schema(description = "원료혼합")
//    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
//    LocalDateTime materialMixing;
//
//    @Schema(description = "충진")
//    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
//    LocalDateTime filling;
//
//    @Schema(description = "캡조립")
//    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
//    LocalDateTime capAssembly;
//
//    @Schema(description = "라벨링")
//    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
//    LocalDateTime labeling;
//
//    @Schema(description = "포장")
//    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
//    LocalDateTime packaging;
//
//    @Schema(description = "단가")
//    int unitPrice;
//
//    @Schema(description = "금액")
//    int price;
//
//    @JsonIgnore
//    LocalDateTime startMaterialMixing;
}
