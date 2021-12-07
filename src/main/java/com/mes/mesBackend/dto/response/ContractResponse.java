package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.ProductionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "수주")
@JsonInclude(NON_NULL)
public class ContractResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "수주번호")
    String contractNo;      // 수주번호

    @Schema(description = "거래처")
    ClientResponse.CodeAndName client;          // 고객사, 고객사명

    @Schema(description = "수주일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate contractDate;     // 수주일자

    @Schema(description = "고객발주일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate clientOrderDate;      // 고객발주일자

    @Schema(description = "생산유형")
    ProductionType productionType;  // 생산유형

    @Schema(description = "고객발주번호")
    String clientOrderNo;       // 고객발주번호

    @Schema(description = "담당자")
    UserResponse.idAndKorName user;              // 담당자

    @Schema(description = "화폐")
    CurrencyResponse.idAndUnit currency;          // 화폐

    @Schema(description = "부가세적용")
    String surtax;              // 부가세적용

    @Schema(description = "출고창고")
    WareHouseResponse.idAndName outputWareHouse;         // 출고창고

    @Schema(description = "납기일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate periodDate;               // 납기일자

    @Schema(description = "변경사유")
    String changeReason;        // 변경사유

    @Schema(description = "결제완료")
    boolean paymentYn;          // 결제완료

    @Schema(description = "지불조건")
    String payCondition;        // 지불조건

    @Schema(description = "Forwader")
    String forwader;                // Forwader

    @Schema(description = "운송조건")
    String transportCondition;      // 운송조건

    @Schema(description = "Shipment Service")
    String shipmentService;         // Shipment Service

    @Schema(description = "Shipment WK")
    String shipmentWk;              // Shipment WK

    @Schema(description = "비고")
    String note;                    // 비고

    @Getter
    @Setter
    @Schema(description = "수주")
    public static class idAndClientOrderNo {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "고객발주번호")
        String clientOrderNo;       // 고객발주번호
    }
}
