package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "작업지시 정보")
@JsonInclude(NON_NULL)
public class WorkOrderResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "지시번호")
    String orderNo;

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    @Schema(description = "작업라인")
    WorkLineResponse.idAndName workLine;

    @Schema(description = "지시수량")
    int orderAmount;

    @Schema(description = "생산담당자")
    UserResponse.idAndKorName user;

    @Schema(description = "단위")
    UnitResponse.idAndName unit;

    @Schema(description = "준비시간")
    int readyTime;

    @Schema(description = "UPH")
    int uph = 1;

    @Schema(description = "소요시간")
    int costTime;

    @Schema(description = "작업예정일")
    LocalDate expectedWorkDate;

    @Schema(description = "예정시간")
    String expectedWorkTime;

    @Schema(description = "지시상태")
    OrderState orderState;

    @Schema(description = "검사의뢰")
    TestType testType;

    @Schema(description = "검사유형")
    TestProcessResponse testProcess;

    @Schema(description = "최종공정")
    boolean lastProcessYn;

    @Schema(description = "생산수량")
    int productionAmount = 0;

    @Schema(description = "투입인원")
    int inputUser = 0;

    @Schema(description = "비고")
    String note;
}
