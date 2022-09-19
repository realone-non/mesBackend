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

    private static final String HEADER = AUTHORIZATION;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        StringBuffer requestURL = request.getRequestURL();

        String xForwardedForHeader = request.getHeader("X-FORWARDED-FOR");
        String userAgent = request.getHeader("User-Agent");
        System.out.println("userAgent::::::: >>>>>>>> " + userAgent);
        String xforwardedHeader;
        if (xForwardedForHeader == null) {
            xforwardedHeader = request.getRemoteAddr();
            System.out.println("remoteAddr::::::: >>>>>>>> " + xforwardedHeader);
        } else {
            xforwardedHeader = xForwardedForHeader;
            System.out.println("xForwardedForHeader::::::: >>>>>>>> " + xforwardedHeader);
        }

        String hostName = InetAddress.getLocalHost().getHostName();
        byte[] address = InetAddress.getLocalHost().getAddress();
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println("getHostName::::::: >>>>>>>> " + hostName);
        System.out.println("getAddress::::::: >>>>>>>> " + address);
        System.out.println("getHostAddress::::::: >>>>>>>> " + hostAddress);

        String hostName1 = InetAddress.getLoopbackAddress().getHostName();
        byte[] address1 = InetAddress.getLoopbackAddress().getAddress();
        String hostAddress1 = InetAddress.getLoopbackAddress().getHostAddress();
        System.out.println("getLoopbackAddress::getHostName::::: >>>>>>>> " + hostName1);
        System.out.println("getLoopbackAddress::getAddress::::: >>>>>>>> " + address1);
        System.out.println("getLoopbackAddress::getHostAddress::::: >>>>>>>> " + hostAddress1);

        try {
            if (request.getHeader(HEADER) == null) {
                throw new CustomJwtException("JWT token is null or empty. requestURL: " + requestURL + ", xforwardedHeader: " + xforwardedHeader + ", userAgent: " + userAgent +  "hostName: " + hostName + ", getLocalHost address: " + address + ", getLocalHost hostAddress " + hostAddress + ", getLoopbackAddress hostName: " + hostName1 + ", getLoopbackAddress address " + address1 + ", getLoopbackAddress hostAddress: " + hostAddress1);
            }
            String token = request.getHeader(HEADER).substring(7);
            // 유효한 자격증명을 제공하지 않고 접근 할때 401
            jwtTokenProvider.validateToken(token, "accessToken");
        } catch (CustomJwtException e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}