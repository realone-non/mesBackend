package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description= "품목계정코드")
@JsonInclude(NON_NULL)
public class ItemAccountCodeResponse {
    @Schema(description = "품목계정 코드 고유아이디")
    Long id;

    @Schema(description = "품목계정 고유아이디")
    Long itemAccountId;

    @Schema(description = "분류")
    String detail;

    @Schema(description = "기호")
    String code;
}
