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
    @Schema(description = "입고수량")
    int inputAmount;
}
