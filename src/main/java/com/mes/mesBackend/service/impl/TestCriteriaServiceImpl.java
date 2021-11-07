package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.TestCriteriaRequest;
import com.mes.mesBackend.dto.response.TestCriteriaResponse;
import com.mes.mesBackend.entity.TestCriteria;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.TestCriteriaRepository;
import com.mes.mesBackend.service.TestCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// 검사기준
@Service
public class TestCriteriaServiceImpl implements TestCriteriaService {
    @Autowired
    TestCriteriaRepository testCriteriaRepository;
    @Autowired
    ModelMapper mapper;

    // 검사기준 생성
    @Override
    public TestCriteriaResponse createTestCriteria(TestCriteriaRequest testCriteriaRequest) {
        TestCriteria testCriteria = mapper.toEntity(testCriteriaRequest, TestCriteria.class);
        testCriteriaRepository.save(testCriteria);
        return mapper.toResponse(testCriteria, TestCriteriaResponse.class);
    }

    // 검사기준 단일 조회
    @Override
    public TestCriteriaResponse getTestCriteria(Long id) throws NotFoundException {
        TestCriteria testCriteria = getTestCriteriaOrThrow(id);
        return mapper.toResponse(testCriteria, TestCriteriaResponse.class);
    }

    // 검사기준 리스트 조회
    @Override
    public List<TestCriteriaResponse> getTestCriterias() {
        List<TestCriteria> testCriterias = testCriteriaRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(testCriterias, TestCriteriaResponse.class);
    }

    // 검사기준 수정
    @Override
    public TestCriteriaResponse updateTestCriteria(Long id, TestCriteriaRequest testCriteriaRequest) throws NotFoundException {
        TestCriteria findTestCriteria = getTestCriteriaOrThrow(id);
        TestCriteria newTestCriteria = mapper.toEntity(testCriteriaRequest, TestCriteria.class);
        findTestCriteria.put(newTestCriteria);
        testCriteriaRepository.save(findTestCriteria);
        return mapper.toResponse(findTestCriteria, TestCriteriaResponse.class);
    }

    // 검사기준 삭제
    @Override
    public void deleteTestCriteria(Long id) throws NotFoundException {
        TestCriteria testCriteria = getTestCriteriaOrThrow(id);
        testCriteria.delete();
        testCriteriaRepository.save(testCriteria);
    }

    // 검사기준 조회 및 예외
    @Override
    public TestCriteria getTestCriteriaOrThrow(Long id) throws NotFoundException {
        return testCriteriaRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("testCriteria does not exist. input id: " + id));
    }
}
