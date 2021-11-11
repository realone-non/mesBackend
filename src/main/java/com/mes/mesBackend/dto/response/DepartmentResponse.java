package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class DepartmentResponse {
    Long id;
    int deptCode; // 부서코드
    String deptName;    // 부서명
    String shortName;   // 부서약어명
    int orders;         // 순번
    boolean useYn = true;      // 사용여부
}
