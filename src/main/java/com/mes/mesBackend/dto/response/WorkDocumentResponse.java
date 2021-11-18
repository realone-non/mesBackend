package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "작업표준서")
@JsonInclude(NON_NULL)
public class WorkDocumentResponse {
    @Schema(description = "작업공정")
    String workProcess;

    @Schema(description = "작업라인")
    String workLine;

    @Schema(description = "품목 id")
    Long itemId;

    @Schema(description = "생산품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "순번")
    int orders;

    @Schema(description = "파일명")
    String fileNameUrl;

    @Schema(description = "비고")
    String note;

    @Schema(description = "사용여부")
    boolean useYn;
}
