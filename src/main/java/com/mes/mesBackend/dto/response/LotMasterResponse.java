package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.QualityLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
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

    @Schema(description = "등록유형")
    EnrollmentType enrollmentType;

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

    @Schema(description = "품질등급")
    QualityLevel qualityLevel;

    @Schema(description = "생성일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = "Asia/Seoul")
    LocalDateTime createdDate;

    @JsonIgnore
    int badItemReturnAmount;       // 반품수량
    @JsonIgnore
    int stockReturnAmount;       // 정상품 반품수량

    public void setReturnAmounts() {
        setReturnAmount(this.badItemReturnAmount + this.stockReturnAmount);
    }
}
