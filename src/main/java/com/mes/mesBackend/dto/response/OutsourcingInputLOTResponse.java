package com.mes.mesBackend.dto.response;

import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "외주 입고 LOT정보")
public class OutsourcingInputLOTResponse {
    @Schema(description = "외주 입고 ID")
    Long id;

    @Schema(description = "LOT ID")
    Long lotId;

    @Schema(description = "LOT유형")
    String lotType;

    @Schema(description = "LOT번호")
    String lotNo;

    @Schema(description = "입고수량")
    int inputAmount;

    @Schema(description = "검사의뢰유형")
    TestType testRequestType;

    @Schema(description = "수입검사여부")
    Boolean inputTestYn;

    @Schema(description = "입고창고ID")
    Long warehouseId;

    @Schema(description = "입고창고")
    String warehouseName;
}
