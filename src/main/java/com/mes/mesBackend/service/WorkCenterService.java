package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.request.WorkCenterRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.dto.response.WorkCenterResponse;
import com.mes.mesBackend.entity.WorkCenter;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// 작업장
public interface WorkCenterService {
    // 작업장 생성
    WorkCenterResponse createWorkCenter(WorkCenterRequest workCenterRequest) throws NotFoundException;

    // 작업장 단일 조회
    WorkCenterResponse getWorkCenter(Long workCenterId) throws NotFoundException;

    // 작업장 전체조회
    List<WorkCenterResponse> getWorkCenters();

    // 작업장 페이징조회
//    Page<WorkCenterResponse> getWorkCenters(Pageable pageable);

    // 작업장 수정
    WorkCenterResponse updateWorkCenter(Long workCenterId, WorkCenterRequest workCenterRequest) throws NotFoundException;

    // 작업장 삭제
    void deleteWorkCenter(Long workCenterId) throws NotFoundException;

    // 작업장 조회 및 예외
    WorkCenter getWorkCenterOrThrow(Long id) throws NotFoundException;

    // 코드 생성
    CodeResponse createWorkCenterCode(CodeRequest codeRequest);

    // 코드 리스트 조회
    List<CodeResponse> getWorkCenterCodes();

    // 코드 삭제
    void deleteWorkCenterCode(Long id) throws NotFoundException, BadRequestException;

    // 코드 조회
    CodeResponse getWorkCenterCode(Long id) throws NotFoundException;
}
