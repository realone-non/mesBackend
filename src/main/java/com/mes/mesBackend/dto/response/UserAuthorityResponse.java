package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 18-2. 권한등록
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "권한등록-사용자정보")
public class UserAuthorityResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "사번")
    String userCode;

    @Schema(description = "이름")
    String korName;

    @Schema(description = "부서명")
    String deptName;

    @Schema(description = "권한")
    UserType userType;
}
