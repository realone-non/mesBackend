package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.TestCriteriaRequest;
import com.mes.mesBackend.dto.response.TestCriteriaResponse;
import com.mes.mesBackend.entity.TestCriteria;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;
// 검사기준
public interface TestCriteriaService {
    // 검사기준 생성
    TestCriteriaResponse createTestCriteria(TestCriteriaRequest testCriteriaRequest);
    // 검사기준 단일 조회
    TestCriteriaResponse getTestCriteria(Long id) throws NotFoundException;
    // 검사기준 리스트 조회
    List<TestCriteriaResponse> getTestCriterias();
    // 검사기준 수정
    TestCriteriaResponse updateTestCriteria(Long id, TestCriteriaRequest testCriteriaRequest) throws NotFoundException;
    // 검사기준 삭제
    void deleteTestCriteria(Long id) throws NotFoundException;
    // 검사기준 조회 및 예외
    TestCriteria getTestCriteriaOrThrow(Long id) throws NotFoundException;
}
