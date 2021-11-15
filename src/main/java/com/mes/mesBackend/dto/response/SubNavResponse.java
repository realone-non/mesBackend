package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "서브 네비게이션")
public class SubNavResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "네비게이션 이름")
    String name;

    @Schema(description = "유저레벨")
    int level;

    @Schema(description = "출력순번")
    int orders;
}
