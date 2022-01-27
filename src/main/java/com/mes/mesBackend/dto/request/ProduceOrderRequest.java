package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "제조오더")
public class ProduceOrderRequest {
    @Schema(description = "수주 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long contract;

    @Schema(description = "수주 품목 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long contractItem;

    @Schema(description = "착수예정일")
    @NotNull(message = NOT_NULL)
    LocalDate expectedStartedDate;

    @Schema(description = "완료예정일")
    @NotNull(message = NOT_NULL)
    LocalDate expectedCompletedDate;

    @Schema(description = "지시상태 [완료: COMPLETION, 진행중: ONGOING, 예정: SCHEDULE, 취소: CANCEL]")
    @NotNull(message = NOT_NULL)
    OrderState orderState;

    @Schema(description = "")
    @NotNull(message = NOT_NULL)
    int rate = 0;

    @Schema(description = "비고")
    String note;
}
