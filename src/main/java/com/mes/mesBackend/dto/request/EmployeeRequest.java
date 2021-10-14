package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.Department;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
    String empCode;     // 사번
    String korName;    // 이름
    Long deptId;  // 부서
    String position;    // 직위
    String jop;         // 직무
    String telNumber;   // 연락처
    String engName;     // 영문이름
    String mail;        // 메일
    String phoneNumber; // 휴대폰 번호(카카오)
    boolean useYn = true;  // 사용
}
