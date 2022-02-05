package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "분할 lot")
public class PopLotMasterResponse {
    @Schema(description = "분할 lot 고유아이디")
    Long lotMasterId;

    @Schema(description = "분할 lot no")
    String lotNo;

    @Schema(description = "분할 lot 재고수량")
    String stockAmount;
}
