package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "라벨 프린트")
@JsonInclude(NON_NULL)
public class LabelPrintResponse {
    @Schema(description = "LOT번호")
    String lotNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "바코드 번호")
    String barcodeNumber;

    @Schema(description = "수량")
    int amount;
}
