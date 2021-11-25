package com.mes.mesBackend.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDto {
    String refreshToken;
    String accessToken;
}
