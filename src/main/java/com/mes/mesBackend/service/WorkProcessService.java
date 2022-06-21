package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkProcessRequest;
import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// 작업공정 & 작업공정 코드 등록
public interface WorkProcessService {
    // 작업공정 생성
    WorkProcessResponse createWorkProcess(WorkProcessRequest workProcessRequest) throws NotFoundException;

    // 작업공정 단일 조회
    WorkProcessResponse getWorkProcess(Long workProcessId) throws NotFoundException;

    // 작업공정 조회
    List<WorkProcessResponse> getWorkProcesses();

    // 작업공정 페이징 조회
//    Page<WorkProcessResponse> getWorkProcesses(Pageable pageable);

    // 작업공정 수정
    WorkProcessResponse updateWorkProcess(Long workProcessId, WorkProcessRequest workProcessRequest) throws NotFoundException;

    WorkProcess getWorkProcessOrThrow(Long id) throws NotFoundException;

    // 작업공정 삭제
    void deleteWorkProcess(Long workProcessId) throws NotFoundException;

//    // 작업공정 코드 생성
//    CodeResponse createWorkProcessCode(CodeRequest codeRequest);
//
//    // 작업공정 코드 단일 조회
//    CodeResponse getWorkProcessCode(Long id) throws NotFoundException;
//
//    // 작업공정 코드 리스트 조회
//    List<CodeResponse> getWorkProcessCodes();
//
//    // 작업공정 코드 삭제
//    void deleteWorkProcessCode(Long id) throws NotFoundException, BadRequestException;
}
