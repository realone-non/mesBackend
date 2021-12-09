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
    String contractNo;

    @Schema(description = "거래처")
    ClientResponse.CodeAndName client;

    @Schema(description = "수주일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate contractDate;

    @Schema(description = "고객발주일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate clientOrderDate;

    @Schema(description = "생산유형")
    ProductionType productionType;

    @Schema(description = "고객발주번호")
    String clientOrderNo;

    @Schema(description = "담당자")
    UserResponse.idAndKorName user;

    @Schema(description = "화폐")
    CurrencyResponse.idAndUnit currency;

    @Schema(description = "부가세적용")
    String surtax;

    @Schema(description = "출고창고")
    WareHouseResponse.idAndName outputWareHouse;

    @Schema(description = "납기일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate periodDate;

    @Schema(description = "변경사유")
    String changeReason;

    @Schema(description = "결제완료")
    boolean paymentYn;

    @Schema(description = "지불조건")
    String payCondition;

    @Schema(description = "Forwader")
    String forwader;

    @Schema(description = "운송조건")
    String transportCondition;

    @Schema(description = "Shipment Service")
    String shipmentService;

    @Schema(description = "Shipment WK")
    String shipmentWk;

    @Schema(description = "비고")
    String note;

    @Getter
    @Setter
    @Schema(description = "수주")
    public static class idAndClientOrderNo {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "고객발주번호")
        String clientOrderNo;
    }
}
