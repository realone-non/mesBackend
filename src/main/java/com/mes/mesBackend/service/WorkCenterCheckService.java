package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkCenterCheckDetailRequest;
import com.mes.mesBackend.dto.request.WorkCenterCheckRequest;
import com.mes.mesBackend.dto.response.WorkCenterCheckDetailResponse;
import com.mes.mesBackend.dto.response.WorkCenterCheckResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkCenterCheckService {
    // 작업장별 점검유형 생성
    WorkCenterCheckResponse createWorkCenterCheck(Long workCenterId, Long checkTypeId) throws NotFoundException;
    // 작업장별 점검유형 단일 조회
    WorkCenterCheckResponse getWorkCenterCheck(Long workCenterCheckId) throws NotFoundException;
    // 작업장별 점검유형 리스트 조회/ 검색: 작업장, 점검유형
    List<WorkCenterCheckResponse> getWorkCenterChecks(Long workCenterId, Long checkTypeId) throws NotFoundException;
    // 작업장별 점검유형 페이징 조회/ 검색: 작업장, 점검유형
//    Page<WorkCenterCheckResponse> getWorkCenterChecks(Long workCenterId, Long checkTypeId, Pageable pageable) throws NotFoundException;
    // 작업장별 점검유형 수정
    WorkCenterCheckResponse updateWorkCenterCheck(Long workCenterCheckId, Long workCenterId, Long checkTypeId) throws NotFoundException;
    // 작업장별 점검유형 삭제
    void deleteWorkCenterCheckDetail(Long workCenterCheckId, Long workCenterCheckDetailId) throws NotFoundException;

    /* 작업장별 점검유형 세부항목 */

    // 작업장별 점검유형 세부 생성
    WorkCenterCheckDetailResponse updateWorkCenterCheckDetail(Long workCenterCheckId, Long workCenterCheckDetailId, WorkCenterCheckDetailRequest workCenterCheckDetailRequest) throws NotFoundException;
    // 작업장별 점검유형 세부 단일 조회 api
    void deleteWorkCenterCheck(Long id) throws NotFoundException;
    // 작업장별 점검유형 세부 리스트 조회
    List<WorkCenterCheckDetailResponse> getWorkCenterCheckDetails(Long workCenterCheckId) throws NotFoundException;
    // 작업장별 점검유형 세부 수정
    WorkCenterCheckDetailResponse getWorkCenterCheckDetail(Long workCenterCheckId, Long workCenterCheckDetailId) throws NotFoundException;
    // 작업장별 점검유형 세부 삭제
    WorkCenterCheckDetailResponse createWorkCenterCheckDetail(Long workCenterCheckId, WorkCenterCheckDetailRequest workCenterCheckDetailRequest) throws NotFoundException, BadRequestException;
}
