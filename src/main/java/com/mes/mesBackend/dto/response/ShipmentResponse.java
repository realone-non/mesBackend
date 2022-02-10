package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.Contract;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "출하")
@JsonInclude(NON_NULL)
public class ShipmentResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "출하번호")
    String shipmentNo;

    @Schema(description = "거래처 id")
    Long clientId;

    @Schema(description = "거래처코드")
    String clientCode;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "출하일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate shipmentDate;

    @Schema(description = "담당자")
    String userManager;

    @Schema(description = "출하창고")
    String wareHouseName;

    @Schema(description = "화폐")
    String currency;

    @Schema(description = "현재 환율")
    float exchangeRate;

    @Schema(description = "거래처 담당자")
    String clientManager;

    @Schema(description = "결제조건")
    String payType;

    @Schema(description = "부가세적용")
    boolean surtax;

    @Schema(description = "운송조건")
    String transportCondition;

    @Schema(description = "Forwader")
    String forwader;

    @Schema(description = "비고")
    String note;

    @JsonIgnore
    Long userId;

    @JsonIgnore
    Long currencyId;

    public ShipmentResponse addContractInfo(Contract contract) {
        if (contract != null) {
            setUserId(contract.getUser().getId());
            setUserManager(contract.getUser().getKorName());                        // 담당자
            setWareHouseName(contract.getOutputWareHouse().getWareHouseName());    // 출하창고
            setCurrencyId(contract.getCurrency().getId());                          // 화폐 id
            setCurrency(contract.getCurrency().getCurrency());                     // 화폐
            setExchangeRate(contract.getCurrency().getExchangeRate());              // 환율
            setPayType(contract.getPayType().getPayType());                                // 결제조건
            setSurtax(contract.isSurtax());                                        // 부가세적용
            setTransportCondition(contract.getTransportCondition());               // 운송조건
            setForwader(contract.getForwader());                                   // Forwader
        }
        return this;
    }

    public ShipmentResponse currencyIdEq(Long currencyId) {
        if (getCurrencyId().equals(currencyId)) {
            return this;
        }
        return null;
    }

    public ShipmentResponse userIdEq(Long userId) {
        if (getUserId().equals(userId)) {
            return this;
        }
        return null;
    }
}
