package com.mes.mesBackend.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "토큰")
public class TokenRequestDto {
    String refreshToken;
    String accessToken;
}
