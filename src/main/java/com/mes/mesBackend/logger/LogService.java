package com.mes.mesBackend.logger;

import com.mes.mesBackend.auth.JwtTokenProvider;
import com.mes.mesBackend.exception.CustomJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    // 어떤 요청이든 로그 기록

    // 수정, 삭제, 조회 : 행목의 고유값 id
    // 생성 : 유저코드, 생성된 항목의 고유값 id
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public String getUserCodeFromHeader(String header) {
        String token = header.substring(7);
        Authentication authentication = jwtTokenProvider.getAuthenticationFromAccessToken(token);
        return authentication.getName();
    }

    public String getUserCodeFromToken(String token) {
        Authentication authentication = jwtTokenProvider.getAuthenticationFromAccessToken(token);
        return authentication.getName();
    }
}
