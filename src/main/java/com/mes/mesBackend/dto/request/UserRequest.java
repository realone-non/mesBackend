package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.exception.Message.NOT_ZERO;

@Getter
@Setter
@ApiModel(description = "직원")
public class UserRequest {
    @ApiModelProperty(value = "사번 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String userCode;     // 사번

    @ApiModelProperty(value = "비밀번호 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String password;        // 비번

    @ApiModelProperty(value = "이름 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String korName;    // 이름

    @ApiModelProperty(value = "부서 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long departmentId;  // 부서

    @ApiModelProperty(value = "직위 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String position;    // 직위

    @ApiModelProperty(value = "직무 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String jop;         // 직무

    @ApiModelProperty(value = "연락처")
    String telNumber;   // 연락처

    @ApiModelProperty(value = "영문이름")
    String engName;     // 영문이름

    @ApiModelProperty(value = "메일 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String mail;        // 메일

    @ApiModelProperty(value = "휴대폰 번호(카카오)")
    @NotBlank(message = NOT_EMPTY)
    String phoneNumber; // 휴대폰 번호(카카오)

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;  // 사용

    @ApiModelProperty(value = "유저권한레벨 NOT NULL")
    @NotNull(message = NOT_NULL)
    int level;          // 유저권한레벨

    @ApiModelProperty(value = "메모")
    String description;
}
