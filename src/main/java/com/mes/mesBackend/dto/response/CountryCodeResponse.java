package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "국가코드")
public class CountryCodeResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "국가코드")
    String name;

    @Schema(description = "사용여부")
    boolean useYn;
}
