package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "작업공정")
public class WorkProcessResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업공정코드")
    CodeResponse.idAndCode workProcessCode;        // 작업공정코드

    @Schema(description = "작업공정명")
    String workProcessName;     // 작업공정명

    @Schema(description = "공정검사")
    boolean processTest;        // 공정검사

    @Schema(description = "공정순번")
    int orders;              // 공정순번

    @Schema(description = "사용여부")
    boolean useYn;              // 사용여부

    @JsonIgnore
    boolean recycleYn;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String workProcessName;     // 작업공정명
    }
}
