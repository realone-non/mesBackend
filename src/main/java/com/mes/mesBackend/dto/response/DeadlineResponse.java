package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "수주 마감일자")
public class DeadlineResponse {
    @Schema(description = "수주 고유아이디")
    Long contractId;

    @Schema(description = "수주번호")
    String contractNo;

    @Schema(description = "마감일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate deadlineDate;
}
