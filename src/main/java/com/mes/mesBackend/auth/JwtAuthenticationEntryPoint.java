package com.mes.mesBackend.auth;

import com.mes.mesBackend.exception.CustomJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    private static final String HEADER = "Authorization";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        try {
            if (request.getHeader(HEADER) == null) {
                throw new CustomJwtException("JWT token is null or empty.");
            }
            String token = request.getHeader(HEADER).substring(7);
            // 유효한 자격증명을 제공하지 않고 접근 할때 401
            jwtTokenProvider.validateToken(token, "accessToken");
        } catch (CustomJwtException e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}