package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "품목형태")
public class ItemFormResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품목형태")
    String form;
}
