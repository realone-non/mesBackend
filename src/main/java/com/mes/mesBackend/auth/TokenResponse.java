package com.mes.mesBackend.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.UserType;
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
    UserType userType;
    int level;

    public TokenResponse putToken(String accessToken, String refreshToken, String korName, UserType userType) {
        this.setAccessToken(accessToken);
        this.setRefreshToken(refreshToken);
        this.setKorName(korName);
        this.setUserType(userType);
        this.setLevel(userType.getLevel());
        return this;
    }
}
