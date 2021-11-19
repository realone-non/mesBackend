package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "불량항목")
public class BadItemResponse {

    @Schema(description = "불량항목코드")
    String badItemCode;

    @Schema(description = "불량항목명")
    String badItemName;

    @Schema(description = "순번")
    int orders;

    @Schema(description = "사용여부")
    boolean useYn;
}
