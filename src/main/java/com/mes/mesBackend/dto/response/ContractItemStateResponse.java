package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "수주 상태 조회")
@JsonInclude(NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ContractItemStateResponse {
    @Schema(description = "수주일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate contractDate;     // 수주일자

    @Schema(description = "JOB No")
    String jobNo;

    @Schema(description = "수주번호")
    String contractNo;      // 수주번호

    @Schema(description = "거래처")
    String clientNo;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "고객발주번호")
    String clientOrderNo;

    @Schema(description = "담당자")
    String userName;

    @Schema(description = "수주유형 [DIFFUSION: 방산 , DOMESTIC: 국내, OVERSEAS: 해외 , ODM: ODM]")
    ContractType contractType;

    @Schema(description = "출고창고")
    String outputWareHouse;         // 출고창고

    @Schema(description = "품번")
    String itemNo;      // 품번

    @Schema(description = "품명")
    String itemName;    // 품명

    @Schema(description = "규격")
    String standard;    // 규격

    @Schema(description = "수주단위")
    String unit;

    @Schema(description = "수주수량")
    int amount;     // 수주수량

    @Schema(description = "화폐")
    String currency;          // 화폐

    @Schema(description = "단가")
    int price;

    @Schema(description = "수주금액")
    int contractAmount;

    @Schema(description = "수주금액(원화)")
    int contractAmountWon;

    @Schema(description = "부가세")
    double surtax;

    @Schema(description = "납기일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate periodDate;       // 납기일자

    @Schema(description = "비고")
    String note;        // 비고
}
