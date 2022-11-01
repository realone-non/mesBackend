package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.helper.ClientIpHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class ClientIpHelperImpl implements ClientIpHelper {
    private static final String [] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    // clientIP 조회
    @Override
    public String getClientIP(HttpServletRequest request) {
        for (String ipHeader : IP_HEADERS) {
            String header = request.getHeader(ipHeader);
            if (header == null || header.isEmpty()) {
                continue;
            }
            return header.split("\\s*,\\s*")[0];
        }
        return request.getRemoteAddr();
    }
}
