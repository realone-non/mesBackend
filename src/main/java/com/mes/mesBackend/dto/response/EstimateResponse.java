package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "견적")
public class EstimateResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "견적번호")
    String estimateNo;

    @Schema(description = "거래처")
    ClientResponse.idAndNameAndNoAndCharge client;

    @Schema(description = "담당자")
    UserResponse.idAndKorName user;

    @Schema(description = "견적일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate estimateDate;

    @Schema(description = "화폐 id")
    CurrencyResponse currency;

    @Schema(description = "납기")
    String period;

    @Schema(description = "유효기간")
    int validity;

    @Schema(description = "지불조건")
    String payCondition;

    @Schema(description = "부가세")
    String surtax;

    @Schema(description = "운송조건")
    String transportCondition;

    @Schema(description = "Forwader")
    String forwader;

    @Schema(description = "DESTINATION")
    String destination;

    @Schema(description = "비고")
    String note;
}
