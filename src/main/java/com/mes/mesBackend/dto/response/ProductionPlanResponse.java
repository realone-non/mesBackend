package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

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
    Long costTime;

    @Schema(description = "지시수량")
    int orderAmount;

    @Schema(description = "수주번호")
    String contractNo;

    @Schema(description = "납기일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate periodDate;

    @Schema(description = "수주처")
    String cName;

    @Schema(description = "지시상태 [완료: COMPLETION, 진행중: ONGOING, 예정: SCHEDULE, 취소: CANCEL]")
    OrderState orderState;

    @Schema(description = "생산수량")
    int productionAmount;

    @Schema(description = "작업라인")
    String workLineName;

    @JsonIgnore
    LocalDateTime startDateTime;

    @JsonIgnore
    LocalDateTime endDateTime;

    @JsonIgnore
    WorkProcessDivision workProcessDivision;

    @JsonIgnore
    Long itemId;

    @JsonIgnore
    Long workProcessId;

    public void setCostTime() {
        if (startDateTime != null && endDateTime != null) {
            long costTime = ChronoUnit.MINUTES.between(startDateTime, endDateTime);
            setCostTime(costTime);
        }
    }

    public void setItems(Item item) {
        setItemNo(item.getItemNo());
        setItemName(item.getItemName());
        setItemAccount(item.getItemAccount().getAccount());
    }
}
