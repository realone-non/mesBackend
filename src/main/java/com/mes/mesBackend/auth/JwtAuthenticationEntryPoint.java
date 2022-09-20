package com.mes.mesBackend.auth;

import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.helper.ClientIpHelper;
import com.mes.mesBackend.interceptor.Interceptor;
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
import java.net.InetAddress;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;
    @Autowired
    ClientIpHelper clientIpHelper;

    private static final String HEADER = AUTHORIZATION;
    private static final String USER_AGENT = "User-Agent";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        StringBuffer requestURL = request.getRequestURL();

        String clientIP = clientIpHelper.getClientIP(request);
        String userAgent = request.getHeader(USER_AGENT);
        System.out.println(">> clientIP: " + clientIP);
        System.out.println(">> userAgent: " + userAgent);

        try {
            if (request.getHeader(HEADER) == null) {
                throw new CustomJwtException("JWT token is null or empty. requestURL: " + requestURL + ", clientIP: " + clientIP + ", userAgent: " + userAgent);
            }
            String token = request.getHeader(HEADER).substring(7);
            // 유효한 자격증명을 제공하지 않고 접근 할때 401
            jwtTokenProvider.validateToken(token, "accessToken");
        } catch (CustomJwtException e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}