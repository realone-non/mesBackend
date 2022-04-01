package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// 생산현황, 수주현황, 출하현황, 출하완료
@Getter
@Setter
@Schema(description = "생산현황, 수주현황, 출하현황, 출하완료")
public class OperationStatusResponse {
    @Schema(description = "생산현황(작업 오더 수)")
    int ongoingProduceOrderAmount;      // 진행중인 제조오더만 갯수

    @Schema(description = "수주현황")
    int contractAmount;      // 납기일자 오늘보다 지난거면 조회 X

    @Schema(description = "금일출하예정")
    int shipmentSchedule;     // 출하일자가 오늘까지 인 출하만

    @Schema(description = "출하완료")
    int shipmentCompletionAmount;   // 출하일자가 오늘이면서 상태값 COMPLETION
}
