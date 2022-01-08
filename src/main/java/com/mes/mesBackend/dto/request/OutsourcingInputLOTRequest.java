package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "외주입고LOT정보")
public class OutsourcingInputLOTRequest {
    @Schema(description = "LOT 유형 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long lotType;

    @Schema(description = "LOT번호")
    String lotNo;

    @Schema(description = "입고수량")
    int inputAmount;

    @Schema(description = "검사의뢰유형")
    TestType testRequestType;
}
