package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.*;

// 8-5. 불량 등록
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "불량유형")
public class BadItemWorkOrderResponse {
    @Schema(description = "작업지시 고유아이디")
    Long workOrderId;

    @Schema(description = "작지번호")
    String workOrderNo;

    @Schema(description = "작업공정 고유아이디")
    Long workProcessId;

    @Schema(description = "작업공정")
    String workProcessName;

    @Schema(description = "작업라인")
    String workLineName;

    @JsonIgnore
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "작업일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime workDateTime;

    @Schema(description = "불량수량")
    int badAmount;

    @Schema(description = "작업자")
    String userKorName;

    @Schema(description = "수주번호")
    String contractNo;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "생산수량")
    int productionAmount;

    @Getter
    @Setter
    public static class subDto {
        int badAmount;
        int createAmount;
        String itemNo;
        String itemName;
    }
}
