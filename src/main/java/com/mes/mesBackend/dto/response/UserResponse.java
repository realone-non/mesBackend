package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "직원")
public class UserResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "사번")
    String userCode;     // 사번

    @ApiModelProperty(value = "이름")
    String korName;    // 이름

    @ApiModelProperty(value = "부서")
    DepartmentResponse department;      // 부서

    @ApiModelProperty(value = "직위")
    String position;    // 직위

    @ApiModelProperty(value = "직무")
    String jop;         // 직무

    @ApiModelProperty(value = "연락처")
    String telNumber;   // 연락처

    @ApiModelProperty(value = "영문이름+직위")
    String engNameAndPosition;  // 영문이름+직위

    @ApiModelProperty(value = "메일")
    String mail;        // 메일

    @ApiModelProperty(value = "휴대폰 번호(카카오)")
    String phoneNumber; // 휴대폰 번호(카카오)

    @ApiModelProperty(value = "사용여부")
    boolean useYn = true;  // 사용

    @ApiModelProperty(value = "유저권한레벨")
    int level;          // 유저권한레벨

    @ApiModelProperty(value = "메모")
    String description;

    @Getter
    @Setter
    public static class idAndKorName {
        Long id;
        String korName;
    }
}
