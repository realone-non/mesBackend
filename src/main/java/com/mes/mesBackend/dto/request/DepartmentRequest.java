package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
public class DepartmentRequest {
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int deptCode; // 부서코드
    @NotBlank(message = NOT_NULL)
    String deptName;    // 부서명
    @NotBlank(message = NOT_NULL)
    String shortName;   // 부서약어명
    @NotNull(message = NOT_NULL)
    int orders;         // 순번
    @NotNull(message = NOT_NULL)
    boolean useYn = true;      // 사용여부
}
