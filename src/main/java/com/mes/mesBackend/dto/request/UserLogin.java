package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "직원 로그인")
public class UserLogin {
    String userCode;
    String password;

}
