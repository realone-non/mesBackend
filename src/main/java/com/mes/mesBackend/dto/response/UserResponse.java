package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class UserResponse {
    Long id;
    String userCode;     // 사번
    String korName;    // 이름
    DepartmentResponse department;      // 부서
    String position;    // 직위
    String jop;         // 직무
    String telNumber;   // 연락처
    String engNameAndPosition;  // 영문이름+직위
    String mail;        // 메일
    String phoneNumber; // 휴대폰 번호(카카오)
    boolean useYn = true;  // 사용
    int level;          // 유저권한레벨
    String description;

    @Getter
    @Setter
    public static class idAndKorName {
        Long id;
        String korName;
    }
}
