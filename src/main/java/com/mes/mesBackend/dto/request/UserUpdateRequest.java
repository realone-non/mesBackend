package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "직원 수정")
public class UserUpdateRequest {
    @Schema(description = "이름")
    @NotBlank(message = NOT_EMPTY)
    String korName;    // 이름

    @Schema(description = "부서 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long department;  // 부서

    @Schema(description = "직위")
    @NotBlank(message = NOT_EMPTY)
    String position;    // 직위

    @Schema(description = "직무")
    @NotBlank(message = NOT_EMPTY)
    String jop;         // 직무

    @Schema(description = "연락처")
    String telNumber;   // 연락처

    @Schema(description = "영문이름")
    String engName;     // 영문이름

    @Schema(description = "메일")
    @NotBlank(message = NOT_EMPTY)
    String mail;        // 메일

    @Schema(description = "휴대폰 번호(카카오)")
    @NotBlank(message = NOT_EMPTY)
    String phoneNumber; // 휴대폰 번호(카카오)

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;  // 사용

    @Schema(description = "유저권한레벨")
    @NotNull(message = NOT_NULL)
    int level;          // 유저권한레벨

    @Schema(description = "메모")
    String description;
}
