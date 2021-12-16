package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.PayType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "출하")
@JsonInclude(NON_NULL)
public class ShipmentResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "출하번호")
    String shipmentNo;

    @Schema(description = "거래처")
    ClientResponse.CodeAndName client;

    @Schema(description = "출하일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate shipmentDate;

    @Schema(description = "담당자")
    UserResponse.idAndKorName user;

    @Schema(description = "출하창고")
    WareHouseResponse.idAndName wareHouse;

    @Schema(description = "화폐")
    CurrencyResponse.idAndUnit currency;

    @Schema(description = "거래처 담당자")
    String clientManager;

    @Schema(description = "결제조건")
    PayType payType;

    @Schema(description = "부가세적용")
    boolean surtax;

    @Schema(description = "운송조건")
    String transportCondition;

    @Schema(description = "Forwader")
    String forwader;

    @Schema(description = "비고")
    String note;
}