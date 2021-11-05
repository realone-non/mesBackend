package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.TestProcessRequest;
import com.mes.mesBackend.dto.response.TestProcessResponse;
import com.mes.mesBackend.entity.TestProcess;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;
// 검사방법
public interface TestProcessService {
    // 검사방법 생성
    TestProcessResponse createTestProcess(TestProcessRequest testProcessRequest);
    // 검사방법 단일 조회
    TestProcessResponse getTestProcess(Long id) throws NotFoundException;
    // 검사방법 리스트 조회
    List<TestProcessResponse> getTestProcesses();
    // 검사방법 수정
    TestProcessResponse updateTestProcess(Long id, TestProcessRequest testProcessRequest) throws NotFoundException;
    // 검사방법 삭제
    void deleteTestProcess(Long id) throws NotFoundException;
    // 검사방법 조회 및 예외
    TestProcess getTestProcessOrThrow(Long id) throws NotFoundException;
}
