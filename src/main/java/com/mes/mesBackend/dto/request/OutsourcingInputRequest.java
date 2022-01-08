package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "외주 입고 정보")
public class OutsourcingInputRequest {

    @Schema(description = "외주 생산 의뢰 정보 ID")
    Long requestId;

    @Schema(description = "입고일시")
    LocalDate inputDate;

    @Schema(description = "미입고수량")
    int noInputAmount;

    @Schema(description = "입고수량")
    int inputAmount;

    @Schema(description = "입고창고 ID")
    Long warehouseId;

    @Schema(description = "검사의뢰유형")
    TestType testRequestType;

    @Schema(description = "비고")
    String note;
}
