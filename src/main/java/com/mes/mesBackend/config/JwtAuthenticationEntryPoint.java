package com.mes.mesBackend.config;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String HEADER = "Authorization";
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 유효한 자격증명을 제공하지 않고 접근 할때 401
        String token = response.getHeader(HEADER);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = "잘못된 접근입니다.";
        json.put("timeStamp", LocalDateTime.now().toString());
        json.put("httpStatusCode", HttpServletResponse.SC_UNAUTHORIZED);
        json.put("message", message);
        json.put("httpStatus", HttpStatus.UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.print(json);
    }
}
