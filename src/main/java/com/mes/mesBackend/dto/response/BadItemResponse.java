package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "불량항목")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BadItemResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "불량항목코드")
    String badItemCode;

    @Schema(description = "불량항목명")
    String badItemName;

    @Schema(description = "순번")
    int orders;

    @Schema(description = "사용여부")
    boolean useYn;

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    @Schema(description = "소진 원부자재")
    String exhaustItem;
}
