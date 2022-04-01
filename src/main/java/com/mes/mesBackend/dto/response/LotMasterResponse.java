package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.ProcessType;
import com.mes.mesBackend.entity.enumeration.QualityLevel;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM;

// 7-1. LOT 마스터 조회
@Getter
@Setter
@Schema(description = "LOT 마스터 조회")
@JsonInclude(NON_NULL)
public class LotMasterResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "창고")
    String warehouse;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "시리얼번호")
    String serialNo;

    @Schema(description = "LOT 유형")
    String lotType;

    @Schema(description = "등록유형 [불량: ERROR, 구매입고: PURCHASE_INPUT, 생산: PRODUCTION, 분할: SPLIT]")
    EnrollmentType enrollmentType;

    @Schema(description = "공정")
    ProcessType process;

    @Schema(description = "공정용")
    boolean processYn;

    @Schema(description = "재고수량")
    int stockAmount;

    @Schema(description = "생성수량")
    int createdAmount;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "투입수량")
    int inputAmount;

    @Schema(description = "전환수량")
    int changeAmount;

    @Schema(description = "이동수량")
    int transferAmount;

    @Schema(description = "실사수량")
    int inspectAmount;

    @Schema(description = "출하수량")
    int shipmentAmount;

    @Schema(description = "반품수량")
    int returnAmount;

    @Schema(description = "검사요청수량")
    int checkRequestAmount;

    @Schema(description = "검사수량")
    int checkAmount;

    @Schema(description = "품질등급 [LEVEL_A: A등급, LEVEL_B: B등급, LEVEL_C: C등급, LEVEL_D: D등급]")
    QualityLevel qualityLevel;

    @Schema(description = "생성일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime createdDate;

    @JsonIgnore
    int badItemReturnAmount;       // 반품수량
    @JsonIgnore
    int stockReturnAmount;       // 정상품 반품수량

    public void setReturnAmounts() {
        setReturnAmount(this.badItemReturnAmount + this.stockReturnAmount);
    }

    @Getter
    @Setter
    @Schema(description = "LOT 정보")
    public static class idAndLotNo {
        @Schema(description = "LOT 고유아이디")
        Long id;

        @Schema(description = "LOT 번호")
        String lotNo;

        @Schema(description = "품목 고유아이디")
        Long itemId;

        @Schema(description = "품번")
        String itemNo;

        @Schema(description = "품명")
        String itemName;

        @Schema(description = "공정 고유아이디")
        Long workProcessId;

        @Schema(description = "공정")
        WorkProcessDivision workProcessDivision;

        @Schema(description = "재고수량")
        int stockAmount;
    }

    @Getter
    @Setter
    @Schema(description = "LOT 불량수량, 재고수량 정보")
    public static class stockAmountAndBadItemAmount {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "LOT 번호")
        String lotNo;

        @Schema(description = "재고수량")
        int stockAmount;

        @Schema(description = "불량수량")
        int badItemAmount;
    }
}
