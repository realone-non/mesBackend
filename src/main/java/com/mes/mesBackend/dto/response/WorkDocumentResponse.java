package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "작업표준서")
@JsonInclude(NON_NULL)
public class WorkDocumentResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    @Schema(description = "작업라인")
    WorkLineResponse.idAndName workLine;

    @Schema(description = "품목")
    ItemResponse.noAndName item;

    @Schema(description = "순번")
    int orders;

    @Schema(description = "파일명")
    String fileNameUrl;

    @Schema(description = "비고")
    String note;

    @Schema(description = "사용여부")
    boolean useYn;
}
