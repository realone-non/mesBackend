package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.exception.Message.NOT_NULL;

// 18-3. 사용자 등록
@Getter
@Setter
@Schema(description = "사용자 등록 (생성)")
public class UserRegistrationRequest {
    @Schema(description = "사번")
    @NotBlank(message = NOT_EMPTY)
    String userCode;     // 사번

    @Schema(description = "이름")
    @NotBlank(message = NOT_EMPTY)
    String korName;    // 이름

    @Schema(description = "메일")
    @NotBlank(message = NOT_EMPTY)
    String mail;        // 메일


//    @Schema(description = "부서 id")
//    @Min(value = ONE_VALUE, message = NOT_ZERO)
//    @NotNull(message = NOT_NULL)
//    Long department;  // 부서

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;  // 사용

    @Getter
    @Setter
    @Schema(description = "사용자 등록 (수정)")
    public static class Update {
        @Schema(description = "이름")
        @NotBlank(message = NOT_EMPTY)
        String korName;    // 이름

        @Schema(description = "메일")
        @NotBlank(message = NOT_EMPTY)
        String mail;        // 메일

        @Schema(description = "사용여부")
        @NotNull(message = NOT_NULL)
        boolean useYn = true;  // 사용
    }
}
