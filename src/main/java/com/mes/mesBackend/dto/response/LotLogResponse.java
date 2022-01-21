package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "lot log")
public class LotLogResponse {
    @Schema(description = "고유아이디")
    Long lotLogId;

    @Schema(description = "생성날짜")
    LocalDateTime createdDate;

    @Schema(description = "작업공정 id")
    Long workProcessId;

    @Schema(description = "작업공정")
    String workProcessName;

    @Schema(description = "작업지시 id")
    Long workOrderId;

    @Schema(description = "작지번호")
    String workOrderNo;

    @Schema(description = "lotMaster id")
    Long lotMasterId;

    @Schema(description = "LOT NO")
    String lotNo;
}
