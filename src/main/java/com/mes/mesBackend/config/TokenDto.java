package com.mes.mesBackend.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Token")
@NoArgsConstructor
public class TokenDto {
//    String grantType;
//    Long accessTokenExpiresIn;
    String accessToken;
    String refreshToken;

    public TokenDto putToken(String accessToken, String refreshToken) {
        this.setAccessToken(accessToken);
        this.setRefreshToken(refreshToken);
        return this;
    }
}
