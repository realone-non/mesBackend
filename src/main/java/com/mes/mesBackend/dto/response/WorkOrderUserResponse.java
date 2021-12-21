package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @JsonIgnore
    LocalDateTime scheduleDateTime;

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

    // scheduleDateTime, startDateTime, endDateTime check
    public WorkOrderUserResponse dateTimeCheckingConverter() {
        if (scheduleDateTime != null && startDateTime == null && endDateTime == null) {
            return null;
        } else if (scheduleDateTime != null && startDateTime != null && endDateTime != null) {
            if (scheduleDateTime.isAfter(startDateTime) || scheduleDateTime.isAfter(endDateTime)) {
                return null;
            } else if (startDateTime.isAfter(endDateTime)) {
                this.setEndDateTime(null);
            }
            return this;
        }
        return this;
    }

    // 지시상태 조회
    public WorkOrderUserResponse orderStateCondition(OrderState orderState) {
        if (orderState != null) {
            if (orderState == OrderState.ONGOING) {
                if (startDateTime != null && endDateTime == null) {
                    return this;
                } else {
                    return null;
                }
            } else if (orderState == OrderState.COMPLETION) {
                if (startDateTime != null && endDateTime != null) {
                    return this;
                } else {
                    return null;
                }
            } else if (orderState == OrderState.SCHEDULE) {
                return null;
            }
        }
        return this;
    }
}
