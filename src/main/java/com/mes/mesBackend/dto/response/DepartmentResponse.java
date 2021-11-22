package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "부서")
public class DepartmentResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "부서코드")
    int deptCode;

    @Schema(description = "부서명")
    String deptName;

    @Schema(description = "부서약어명")
    String shortName;

    @Schema(description = "순번")
    int orders;

    @Schema(description = "사용여부")
    boolean useYn;


    @Getter
    @Setter
    @Schema(description = "부서")
    public static class idAndName {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "부서명")
        String deptName;
    }
}
