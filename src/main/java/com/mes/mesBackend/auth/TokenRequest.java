package com.mes.mesBackend.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "토큰")
public class TokenRequest {
    String refreshToken;
    String accessToken;
}
