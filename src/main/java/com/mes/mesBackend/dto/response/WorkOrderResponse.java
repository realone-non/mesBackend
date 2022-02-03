package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    @Schema(description = "고유아이디")
    Long workProcessId;

    @Schema(description = "작업공정명")
    String workProcessName;

    @Schema(description = "고유아이디")
    Long workLineId;

    @Schema(description = "작업라인명")
    String workLineName;

    @Schema(description = "지시수량")
    int orderAmount;

    @Schema(description = "생산담당자 id")
    Long userId;

    @Schema(description = "생산담당자 이름")
    String userKorName;

    @Schema(description = "단위명")
    String unitCodeName;

    @Schema(description = "준비시간")
    int readyTime;

    @Schema(description = "UPH")
    int uph = 1;

    @Schema(description = "소요시간")
    Long costTime;

    @Schema(description = "작업예정일")
    LocalDate expectedWorkDate;

    @Schema(description = "예정시간")
    String expectedWorkTime;

    @Schema(description = "지시상태 [완료: COMPLETION, 진행중: ONGOING, 예정: SCHEDULE, 취소: CANCEL]")
    OrderState orderState;

    @Schema(description = "검사의뢰")
    TestType testType;

    @Schema(description = "검사유형 고유아이디")
    Long testProcessId;

    @Schema(description = "검사방법")
    String testProcess;

    @Schema(description = "최종공정")
    boolean lastProcessYn;

    @Schema(description = "생산수량")
    int productionAmount = 0;

    @Schema(description = "투입인원")
    int inputUser = 0;

    @Schema(description = "비고")
    String note;

    @JsonIgnore
    LocalDateTime startDateTime;
    @JsonIgnore
    LocalDateTime endDateTime;
    @JsonIgnore
    Long produceOrderItemId;        // 제조오더의 item 고유아이디

    // costTime 변경
    public void setCostTime() {
        if (startDateTime != null && endDateTime != null) {
            long costTime = ChronoUnit.MINUTES.between(startDateTime, endDateTime);
            setCostTime(costTime);
        }
    }
}
