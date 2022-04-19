package com.mes.mesBackend.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mysql.cj.xdevapi.DatabaseObjectDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "재사용 LOT")
@JsonInclude(NON_NULL)
public class RecycleLotResponse {
    @Schema(description = "LOT아이디")
    Long id;

    @Schema(description = "LOT번호")
    String lotNo;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "수량")
    int stockAmount;

    @Schema(description = "공정")
    String workProcess;

    @Schema(description = "라벨프린트 출력 여부")
    boolean labelPrintYn;
}
