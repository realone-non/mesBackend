package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkLineRequest;
import com.mes.mesBackend.dto.response.WorkLineResponse;
import com.mes.mesBackend.entity.WorkLine;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface WorkLineService {

    // 작업라인 생성
    WorkLineResponse createWorkLine(WorkLineRequest workLineRequest) throws NotFoundException;
    // 작업라인 단일 조회
    WorkLineResponse getWorkLine(Long id) throws NotFoundException;
    // 작업라인 전체 조회
    List<WorkLineResponse> getWorkLines();
    // 작업라인 페이징 조회
//    Page<WorkLineResponse> getWorkLines(Pageable pageable);
    // 작업라인 수정
    WorkLineResponse updateWorkLine(Long id, WorkLineRequest workLineRequest) throws NotFoundException;
    // 작업라인 삭제
    void deleteWorkLine(Long id) throws NotFoundException;
    // 작업라인 조회 및 예외
    WorkLine getWorkLineOrThrow(Long id) throws NotFoundException;

//    // 라인코드 생성
//    CodeResponse createWorkLineCode(CodeRequest codeRequest);
//    // 라인코드 단일 조회
//    CodeResponse getWorkLineCode(Long id) throws NotFoundException;
//    // 라인코드 리스트 조회
//    List<CodeResponse> getWorkLineCodes();
//    // 라인코드 삭제
//    void deleteWorkLineCode(Long id) throws NotFoundException, BadRequestException;
}
