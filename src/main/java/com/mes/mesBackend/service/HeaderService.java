package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;

import java.util.List;


public interface HeaderService {
    // 헤더 조회
    List<HeaderResponse> getHeaders(String controllerName);

    // 헤더 생성
    HeaderResponse createHeader(HeaderRequest headerRequest);
}
