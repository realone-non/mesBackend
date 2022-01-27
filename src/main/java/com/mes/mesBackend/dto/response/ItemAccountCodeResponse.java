package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description= "품목계정코드")
public class ItemAccountCodeResponse {
    @Schema(description = "품목계정 코드 고유아이디")
    Long itemAccountCodeId;

    @Schema(description = "품목계정 고유아이디")
    Long itemAccountId;

    @Schema(description = "분류")
    String detail;

    @Schema(description = "기호")
    String code;
}
