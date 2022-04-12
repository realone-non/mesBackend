package com.mes.mesBackend.auth;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 유저 정보는 있으나 자원에 접근할 수 있는 권한이 없는경우 403
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한 없음");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON.toString());
        JSONObject json = new JSONObject();
        String message = "해당 리소스에 접근할 수 있는 권한이 없습니다.";

        json.put("timeStamp", LocalDateTime.now().toString());
        json.put("httpStatusCode", HttpServletResponse.SC_FORBIDDEN);
        json.put("message", message);
        json.put("httpStatus", HttpStatus.UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.print(json);
    }
}
