package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 8-5. 불량 등록
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "불량유형")
public class BadItemEnrollmentResponse {
    @Schema(description = "불량유형 고유아이디")
    Long badItemId;

    @Schema(description = "작업공정")
    String workProcessName;

    @Schema(description = "불량유형명")
    String badItemName;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "LOT 번호")
    String lotNo;
}
