package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkPlaceRequest;
import com.mes.mesBackend.dto.response.WorkPlaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 사업장
public interface WorkPlaceService {
    // 사업장 생성
    WorkPlaceResponse createWorkPlace(WorkPlaceRequest workPlaceRequest);

    // 사업장 단일 조회
    WorkPlaceResponse getWorkPlace(Long id);

    // 사업장 페이징 조회
//    Page<WorkPlaceResponse> getWorkPlaces(Pageable pageable);

    // 사업장 수정
    WorkPlaceResponse updateWorkPlace(Long id, WorkPlaceRequest workPlaceRequest);

    // 사업장 삭제
    void deleteWorkPlace(Long id);
}
