package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD_HH_MM;

// 7-2. LOT Tracking
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "LOT Tracking")
public class LotTrackingResponse {
    @Schema(description = "고유아이디")
    Long lotMasterId;

    @Schema(description = "LOT NO")
    String lotNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "등록유형")
    EnrollmentType enrollmentType;

    @Schema(description = "발생일자")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
    LocalDateTime createdDate;

    @Schema(description = "투입수량")
    int inputAmount;
}
