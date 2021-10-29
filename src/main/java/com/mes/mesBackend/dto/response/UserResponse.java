package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
