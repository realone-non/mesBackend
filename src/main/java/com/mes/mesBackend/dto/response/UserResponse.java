package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "직원")
public class UserResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "사번")
    String userCode;     // 사번

    @Schema(description = "이름")
    String korName;    // 이름

    @Schema(description = "부서")
    DepartmentResponse department;      // 부서

    @Schema(description = "직위")
    String position;    // 직위

    @Schema(description = "직무")
    String jop;         // 직무

    @Schema(description = "연락처")
    String telNumber;   // 연락처

    @Schema(description = "영문이름+직위")
    String engNameAndPosition;  // 영문이름+직위

    @Schema(description = "메일")
    String mail;        // 메일

    @Schema(description = "휴대폰 번호(카카오)")
    String phoneNumber; // 휴대폰 번호(카카오)

    @Schema(description = "사용여부")
    boolean useYn = true;  // 사용

    @Schema(description = "유저권한레벨")
    int level;          // 유저권한레벨

    @Schema(description = "메모")
    String description;

    @Getter
    @Setter
    public static class idAndKorName {
        Long id;
        String korName;
    }

    @Getter
    @Setter
    @JsonInclude(NON_NULL)
    @Schema(description = "직원")
    public static class idAndKorNameAndEmail {
        @Schema(description = "고유아이디")
        Long id;
        @Schema(description = "이름")
        String korName;
        @Schema(description = "메일")
        String mail;
    }
}
