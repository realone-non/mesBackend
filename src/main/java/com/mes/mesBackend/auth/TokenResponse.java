package com.mes.mesBackend.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "Token")
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class TokenResponse {
    String accessToken;
    String refreshToken;
    String korName;

    public TokenResponse putToken(String accessToken, String refreshToken, String korName) {
        this.setAccessToken(accessToken);
        this.setRefreshToken(refreshToken);
        this.setKorName(korName);
        return this;
    }
}
