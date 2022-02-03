package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "사용한 원자재 등록")
public class PopBomDetailItemResponse {
    @Schema(description = "bomMaster 품목 고유아이디")
    Long bomMasterItemId;

    @Schema(description = "bomMaster 품번")
    String bomMasterItemNo;

    @Schema(description = "bomMaster 품명")
    String bomMasterItemName;

    @Schema(description = "bomMaster 등록수량")
    int bomMasterItemAmount;

    @Schema(description = "bomDetail 품목 고유아이디")
    Long bomDetailItemId;

    @Schema(description = "bomDetail 품번")
    String bomDetailItemNo;

    @Schema(description = "bomDetail 품명")
    String bomDetailItemName;

    @Schema(description = "bomDetail 소진량")
    int bomDetailExhaustAmount;

    @Schema(description = "bomDetail 소진유무")
    boolean bomDetailExhaustYn;
}
