package com.mes.mesBackend.config;

import com.mes.mesBackend.exception.CustomJwtException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    private static final String HEADER = "Authorization";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        // 유효한 자격증명을 제공하지 않고 접근 할때 401
        String token = request.getHeader(HEADER);
        try {
            jwtTokenProvider.validateToken(token);
        } catch (CustomJwtException e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}