package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface GridOptionService {

    // 디증 생성
    List<GridOptionResponse> createGridOptions(Long userId, List<GridOptionRequest> gridOptionRequest);

    // 단일
    GridOptionResponse createGridOption(Long headerId, String controllerName, GridOptionRequest gridOptionRequest) throws NotFoundException;

    // 다중 조회 (유저Id, controllerName)
//    List<GridOptionResponse> getGrids(String controllerName, Long userId);
}
