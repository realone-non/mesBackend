package com.mes.mesBackend.auth;

import com.mes.mesBackend.exception.CustomJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수핼

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // /auth 경로는 필터가 적용되지 않는다.
//        String path = request.getRequestURI();
//        if (path.startsWith("/auth/")) {
//            filterChain.doFilter(request, response);
//        }
        String token = request.getHeader(HEADER);
        try {
            if (token != null && jwtTokenProvider.validateToken(token, "accessToken")) {
                Authentication authentication = jwtTokenProvider.getAuthenticationFromAccessToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (CustomJwtException e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }
}
