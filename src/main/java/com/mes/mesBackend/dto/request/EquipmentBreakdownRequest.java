package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.BreakReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

// 17-2. 설비고장 수리내역 등록 설비고장
@Getter
@Setter
@Schema(description = "설비 고장")
public class EquipmentBreakdownRequest {
    @Schema(description = "고장발생일")
    @DateTimeFormat(pattern = YYYY_MM_DD)
    @NotNull(message = NOT_NULL)
    LocalDate breakDownDate;

    @Schema(description = "설비 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long equipmentId;

    @Schema(description = "신고일시", example = "yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime reportDate;

    @Schema(description = "요청시고장유형")
    @NotBlank(message = NOT_EMPTY)
    String requestBreakType;

    @Schema(description = "요청비고")
    String breakNote;

    @Schema(description = "고장유형")
    @NotNull(message = NOT_NULL)
    BreakReason breakReason;

    @Schema(description = "고장원인")
    @NotBlank(message = NOT_EMPTY)
    String causeOfFailure;

    @Schema(description = "보전원 도착일시")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime arrivalDate;

    @Schema(description = "수리시작일시")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime repairStartDate;

    @Schema(description = "수리종료일시")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime repairEndDate;

    @Schema(description = "비고")
    String note;

    @Schema(description = "작업장 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workCenterId;
}
