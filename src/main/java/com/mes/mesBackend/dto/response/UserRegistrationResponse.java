package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 18-3. 사용자 등록
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "사용자 등록")
public class UserRegistrationResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "사번")
    String userCode;

    @Schema(description = "이름")
    String korName;

    @Schema(description = "부서 고유아이디")
    Long deptId;

    @Schema(description = "부서코드")
    int deptCode;

    @Schema(description = "부서명")
    String deptName;

    @Schema(description = "사용여부")
    boolean useYn;
}
