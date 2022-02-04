package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "LOT NO 목록 검색")
public class PopBomDetailLotMasterResponse {
    @Schema(description = "lotMaster 고유아이디")
    Long lotMasterId;

    @Schema(description = "lot no")
    String lotNo;

    @Schema(description = "잔여수량")
    int stockAmount;

    @Schema(description = "단위")
    String unitCodeName;

    @Schema(description = "소진유무")
    boolean exhaustYn;

    @Schema(description = "소진량")
    int exhaustAmount;
}
