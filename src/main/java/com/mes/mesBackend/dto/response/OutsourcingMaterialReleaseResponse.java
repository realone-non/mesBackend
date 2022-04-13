package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "품목별 검사항목")
public class OutsourcingMaterialReleaseResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품명 ID")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "소요량")
    float amount;

    @Schema(description = "출고요청량")
    int outputRequestAmount;

    @Schema(description = "출고량")
    int outputAmount;

    @Schema(description = "외주생산의뢰 ID")
    Long requestId;

    @JsonIgnore
    Long requestItemId;
}
