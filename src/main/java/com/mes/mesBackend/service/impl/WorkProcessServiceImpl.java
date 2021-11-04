package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.request.WorkProcessRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.entity.WorkProcessCode;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkProcessCodeRepository;
import com.mes.mesBackend.repository.WorkProcessRepository;
import com.mes.mesBackend.service.WorkProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// 작업공정 & 작업공정 코드 등록
@Service
public class WorkProcessServiceImpl implements WorkProcessService {

    @Autowired
    WorkProcessRepository workProcessRepository;

    @Autowired
    WorkProcessCodeRepository workProcessCodeRepository;

    @Autowired
    ModelMapper mapper;

    // 작업공정 생성
    @Override
    public WorkProcessResponse createWorkProcess(WorkProcessRequest workProcessRequest) throws NotFoundException {
        WorkProcessCode workProcessCode = getWorkProcessCodeOrThrow(workProcessRequest.getWorkProcessCode());
        WorkProcess workProcess = mapper.toEntity(workProcessRequest, WorkProcess.class);
        workProcess.addWorkProcessCode(workProcessCode);
        workProcessRepository.save(workProcess);
        return mapper.toResponse(workProcess, WorkProcessResponse.class);
    }

    // 작업공정 단일 조회
    @Override
    public WorkProcessResponse getWorkProcess(Long workProcessId) throws NotFoundException {
        WorkProcess workProcess = getWorkProcessOrThrow(workProcessId);
        return mapper.toResponse(workProcess, WorkProcessResponse.class);
    }

    // 작업공정 페이징 조회
    @Override
    public Page<WorkProcessResponse> getWorkProcesses(Pageable pageable) {
        Page<WorkProcess> workProcesses = workProcessRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(workProcesses, WorkProcessResponse.class);
    }

    // 작업공정 수정
    @Override
    public WorkProcessResponse updateWorkProcess(Long workProcessId, WorkProcessRequest workProcessRequest) throws NotFoundException {
        WorkProcess findWorkProcess = getWorkProcessOrThrow(workProcessId);
        WorkProcessCode newWorkProcessCode = getWorkProcessCodeOrThrow(workProcessRequest.getWorkProcessCode());
        WorkProcess newWorkProcess = mapper.toEntity(workProcessRequest, WorkProcess.class);
        findWorkProcess.put(newWorkProcess, newWorkProcessCode);
        workProcessRepository.save(findWorkProcess);
        return mapper.toResponse(findWorkProcess, WorkProcessResponse.class);
    }

    // 작업공정 조회 및 예외처리
    @Override
    public WorkProcess getWorkProcessOrThrow(Long id) throws NotFoundException {
        WorkProcess workProcess = workProcessRepository.findByIdAndDeleteYnFalse(id);
        if (id == 0) {
            return null;
        } else if (workProcess == null) {
            throw new NotFoundException("workProcess does not exist. input id: " + id);
        }
        return workProcess;
    }

    // 작업공정 삭제
    @Override
    public void deleteWorkProcess(Long workProcessId) throws NotFoundException {
        WorkProcess workProcess = getWorkProcessOrThrow(workProcessId);
        workProcess.delete();
        workProcessRepository.save(workProcess);
    }

    // 작업공정 코드 생성
    @Override
    public CodeResponse createWorkProcessCode(CodeRequest codeRequest) {
        WorkProcessCode workProcessCode = mapper.toEntity(codeRequest, WorkProcessCode.class);
        WorkProcessCode saveCode = workProcessCodeRepository.save(workProcessCode);
        return mapper.toResponse(saveCode, CodeResponse.class);
    }

    // 작업공정 코드 단일 조회
    @Override
    public CodeResponse getWorkProcessCode(Long id) throws NotFoundException {
        return mapper.toResponse(getWorkProcessCodeOrThrow(id), CodeResponse.class);
    }

    // 작업공정 코드 리스트 조회
    @Override
    public List<CodeResponse> getWorkProcessCodes() {
        return mapper.toListResponses(workProcessCodeRepository.findAll(), CodeResponse.class);
    }

    // 작업공정 코드 삭제
    @Override
    public void deleteWorkProcessCode(Long id) throws NotFoundException, BadRequestException {
        throwIfWorkProcessCodeExist(id);
        workProcessCodeRepository.deleteById(id);
    }

    // 작업공정 코드 조회 및 예외
    private WorkProcessCode getWorkProcessCodeOrThrow(Long id) throws NotFoundException {
        return workProcessCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("workProcessCode does not exist. input id: " + id));
    }

    // workProcessCode 삭제 시 workProcess에 해당하는 workProcessCode가 있으면 예외
    private void throwIfWorkProcessCodeExist(Long workProcessCodeId) throws NotFoundException, BadRequestException {
        WorkProcessCode workProcessCode = getWorkProcessCodeOrThrow(workProcessCodeId);
        boolean existByWorkProcess = workProcessRepository.existsByWorkProcessCodeAndDeleteYnFalse(workProcessCode);
        if (existByWorkProcess)
            throw new BadRequestException("code exists in the workProcess. input code id: " + workProcessCodeId);
    }
}
