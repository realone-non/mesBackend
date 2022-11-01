package com.mes.mesBackend.helper;

import javax.servlet.http.HttpServletRequest;

public interface ClientIpHelper {
    // clientIP 조회
    String getClientIP(HttpServletRequest request);
}
