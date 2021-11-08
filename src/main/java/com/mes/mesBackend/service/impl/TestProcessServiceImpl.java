package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.TestProcessRequest;
import com.mes.mesBackend.dto.response.TestProcessResponse;
import com.mes.mesBackend.entity.TestProcess;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.TestProcessRepository;
import com.mes.mesBackend.service.TestProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 검사방법
@Service
public class TestProcessServiceImpl implements TestProcessService {
    @Autowired
    TestProcessRepository testProcessRepository;
    @Autowired
    ModelMapper mapper;

    // 검사방법 생성
    @Override
    public TestProcessResponse createTestProcess(TestProcessRequest testProcessRequest) {
        TestProcess testProcess = mapper.toEntity(testProcessRequest, TestProcess.class);
        testProcessRepository.save(testProcess);
        return mapper.toResponse(testProcess, TestProcessResponse.class);
    }

    // 검사방법 단일 조회
    @Override
    public TestProcessResponse getTestProcess(Long id) throws NotFoundException {
        TestProcess testProcess = getTestProcessOrThrow(id);
        return mapper.toResponse(testProcess, TestProcessResponse.class);
    }

    // 검사방법 리스트 조회
    @Override
    public List<TestProcessResponse> getTestProcesses() {
        List<TestProcess> testProcesses = testProcessRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(testProcesses, TestProcessResponse.class);
    }

    // 검사방법 수정
    @Override
    public TestProcessResponse updateTestProcess(Long id, TestProcessRequest testProcessRequest) throws NotFoundException {
        TestProcess findTestProcess = getTestProcessOrThrow(id);
        TestProcess newTestProcess = mapper.toEntity(testProcessRequest, TestProcess.class);
        findTestProcess.put(newTestProcess);
        testProcessRepository.save(findTestProcess);
        return mapper.toResponse(findTestProcess, TestProcessResponse.class);
    }

    // 검사방법 삭제
    @Override
    public void deleteTestProcess(Long id) throws NotFoundException {
        TestProcess testProcess = getTestProcessOrThrow(id);
        testProcess.delete();
        testProcessRepository.save(testProcess);
    }

    // 검사방법 조회 및 예외
    @Override
    public TestProcess getTestProcessOrThrow(Long id) throws NotFoundException {
        return testProcessRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("testProcess does not exist. input id: " + id));
    }
}
