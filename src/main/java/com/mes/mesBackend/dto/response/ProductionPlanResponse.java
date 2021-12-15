package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "생산계획 수립")
@JsonInclude(NON_NULL)
public class ProductionPlanResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "지시번호")
    String orderNo;

    @Schema(description = "작업순번")
    int orders;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "품목계정")
    String itemAccount;

    @Schema(description = "작업라인")
    String workLine;

    @Schema(description = "작업예정일")
    LocalDate expectedWorkDate;

    @Schema(description = "예정시간")
    String expectedWorkTime;

    @Schema(description = "준비시간")
    int readyTime;

    @Schema(description = "UPH")
    int uph = 1;

    @Schema(description = "소요시간")
    int costTime;

    @Schema(description = "지시수량")
    int orderAmount;

    @Schema(description = "수주번호")
    String contractNo;

    @Schema(description = "납기일자")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate periodDate;

    @Schema(description = "수주처")
    String cName;

    @Schema(description = "지시상태")
    InstructionStatus instructionStatus;

    @Schema(description = "생산수량")
    int productionAmount;
}
