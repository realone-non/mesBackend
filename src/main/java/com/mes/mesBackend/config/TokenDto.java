package com.mes.mesBackend.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Token")
@Builder
@NoArgsConstructor
public class TokenDto {
    String grantType;
    String accessToken;
    Long accessTokenExpiresIn;
    String refreshToken;
}
