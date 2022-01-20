package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "헤더")
public class HeaderResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "헤더")
    String header;

    @Schema(description = "컨트롤러 이름")
    String controllerName;

    @Schema(description = "컬럼명")
    String columnName;

    @Schema(description = "순서")
    int seq;

    @Schema(description = "필수여부")
    boolean isRequire;

    @Schema(description = "그리드 옵션")
    GridOptionResponse gridOptionResponse;
}
