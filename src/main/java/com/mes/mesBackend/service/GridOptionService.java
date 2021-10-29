package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface GridOptionService {

    // 단일
    GridOptionResponse createGridOption(Long headerId, GridOptionRequest gridOptionRequest, Long userId) throws NotFoundException;

    // 헤더, 그리드 옵션 동시 조회
    List<HeaderResponse> getHeaderGridOptions(Long userId, String controllerName) throws NotFoundException;
}
