package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "코드")
public class CodeResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "코드")
    String code;

    @Schema(description = "코드명")
    String codeName;

    @Schema(description = "설명")
    String description;

    @Schema(description = "사용여부")
    boolean useYn;

    @Getter
    @Setter
    public static class idAndCode {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "코드")
        String code;
    }
}
