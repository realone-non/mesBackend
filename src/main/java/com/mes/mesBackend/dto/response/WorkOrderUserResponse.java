package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM;

@Getter
@Setter
@Schema(description = "작업자 투입")
@JsonInclude(NON_NULL)
public class WorkOrderUserResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업자 고유아이디")
    Long userId;

    @Schema(description = "작업자Id")
    String userCode;     // 사번

    @Schema(description = "작업자명")
    String korName;    // 이름

    @Schema(description = "시작일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime startDateTime;

    @Schema(description = "종료일시")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime endDateTime;

    @Schema(description = "소요시간")
    Long costTime;

    @Schema(description = "작업라인")
    String workLine;

    @Schema(description = "비고")
    String note;

    @Schema(description = "수주번호")
    String contractNo;

    @Schema(description = "제조오더번호")
    String produceOrderNo;

    @JsonIgnore
    WorkProcessDivision workProcessDivision;

    @JsonIgnore
    Long itemId;

    @JsonIgnore
    Long itemAccountId;

    @JsonIgnore
    Long workProcessId;

    // costTime(소요시간)
    public void putCostTime() {
        if (this.startDateTime != null && this.endDateTime != null) {
            long costTime = ChronoUnit.MINUTES.between(this.startDateTime, this.endDateTime);
            setCostTime(costTime);
        }
    }

    public WorkOrderUserResponse setItems(Item item) {
        setItemAccountId(item.getItemAccount().getId());
        return this;
    }
}
