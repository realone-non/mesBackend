package com.mes.mesBackend.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Token")
@NoArgsConstructor
public class TokenResponse {
    String accessToken;
    String refreshToken;

    public TokenResponse putToken(String accessToken, String refreshToken) {
        this.setAccessToken(accessToken);
        this.setRefreshToken(refreshToken);
        return this;
    }
}
