package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "작업지시 정보")
public class WorkOrderCreateRequest {
    @Schema(description = "작업공정 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workProcess;

    @Schema(description = "작업라인 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workLine;

    @Schema(description = "지시수량 id")
    @NotNull(message = NOT_NULL)
    int orderAmount;

    @Schema(description = "직원 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long user;

    @Schema(description = "단위 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long unit;

    @Schema(description = "준비시간")
    @NotNull(message = NOT_NULL)
    int readyTime = 0;

    @Schema(description = "UPH")
    @NotNull(message = NOT_NULL)
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    int uph = 1;

    @Schema(description = "작업예정일")
    @NotNull(message = NOT_NULL)
    LocalDate expectedWorkDate;

    @Schema(description = "예정시간")
    @NotBlank(message = NOT_EMPTY)
    String expectedWorkTime;

    @Schema(description = "검사의뢰")
    @NotNull(message = NOT_NULL)
    TestType testType;

    @Schema(description = "검사유형")
    @NotNull(message = NOT_NULL)
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long testProcess;

    @Schema(description = "최종공정")
    @NotNull(message = NOT_NULL)
    boolean lastProcessYn;

    @Schema(description = "생산수량")
    @NotNull(message = NOT_NULL)
    int productionAmount = 0;

    @Schema(description = "투입인원")
    @NotNull(message = NOT_NULL)
    int inputUser = 0;

    @Schema(description = "비고")
    String note;
}
