package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "제조오더")
@JsonInclude(NON_NULL)
public class ProduceOrderResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "제조오더번호")
    String produceOrderNo;

    @Schema(description = "수주품목(품번,품명,수주수량,수주유형)")
    ContractItemResponse.toProduceOrder contractItem;

    @Schema(description = "수주(수주번호,수주처,납기일자)")
    ContractResponse.toProduceOrder contract;

    @Schema(description = "착수예정일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = "Asia/Seoul")
    LocalDate expectedStartedDate;

    @Schema(description = "완료예정일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = "Asia/Seoul")
    LocalDate expectedCompletedDate;

    @Schema(description = "지시상태 [완료: COMPLETION, 진행중: ONGOING, 예정: SCHEDULE, 취소: CANCEL]")
    OrderState orderState = OrderState.SCHEDULE;

    @Schema(description = "보정율")
    int rate = 0;

    @Schema(description = "비고")
    String note;
}
