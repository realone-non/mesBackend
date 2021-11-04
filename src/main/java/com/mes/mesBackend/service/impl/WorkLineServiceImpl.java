package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.request.WorkLineRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.dto.response.WorkLineResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkLineCodeRepository;
import com.mes.mesBackend.repository.WorkLineRepository;
import com.mes.mesBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkLineServiceImpl implements WorkLineService {

    @Autowired
    WorkLineRepository workLineRepository;

    @Autowired
    WorkLineCodeRepository workLineCodeRepository;

    @Autowired
    WorkCenterService workCenterService;

    @Autowired
    WareHouseService wareHouseService;

    @Autowired
    WorkProcessService workProcessService;

    @Autowired
    ClientService clientService;

    @Autowired
    ModelMapper mapper;


    // 작업라인 생성
    @Override
    public WorkLineResponse createWorkLine(WorkLineRequest workLineRequest) throws NotFoundException {
        WorkLineCode workLineCode = getWorkLineCodeOrThrow(workLineRequest.getWorkLineCode());
        WorkCenter workCenter = workCenterService.getWorkCenterOrThrow(workLineRequest.getWorkCenter());
        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(workLineRequest.getWorkProcess());

        WorkLine workLine = mapper.toEntity(workLineRequest, WorkLine.class);
        workLine.addMapping(workLineCode, workCenter, workProcess);
        workLineRepository.save(workLine);
        return mapper.toResponse(workLine, WorkLineResponse.class);
    }


    // 작업라인 단일 조회
    @Override
    public WorkLineResponse getWorkLine(Long id) throws NotFoundException {
        WorkLine workLine = getWorkLineOrThrow(id);
        return mapper.toResponse(workLine, WorkLineResponse.class);
    }

    // 작업라인 페이징 조회
    @Override
    public Page<WorkLineResponse> getWorkLines(Pageable pageable) {
        Page<WorkLine> workLines = workLineRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(workLines, WorkLineResponse.class);
    }

    // 작업라인 수정
    @Override
    public WorkLineResponse updateWorkLine(Long id, WorkLineRequest workLineRequest) throws NotFoundException {
        WorkLineCode newWorkLineCode = getWorkLineCodeOrThrow(workLineRequest.getWorkLineCode());
        WorkCenter newWorkCenter = workCenterService.getWorkCenterOrThrow(workLineRequest.getWorkCenter());
        WorkProcess newWorkProcess = workProcessService.getWorkProcessOrThrow(workLineRequest.getWorkProcess());

        WorkLine findWorkLine = getWorkLineOrThrow(id);
        WorkLine newWorkLine = mapper.toEntity(workLineRequest, WorkLine.class);
        findWorkLine.put(newWorkLine, newWorkLineCode, newWorkCenter, newWorkProcess);
        workLineRepository.save(findWorkLine);
        return mapper.toResponse(findWorkLine, WorkLineResponse.class);
    }

    // 작업라인 삭제
    @Override
    public void deleteWorkLine(Long id) throws NotFoundException {
        WorkLine workLine = getWorkLineOrThrow(id);
        workLine.delete();
        workLineRepository.save(workLine);
    }

    // 작업라인 조회 및 예외
    @Override
    public WorkLine getWorkLineOrThrow(Long id) throws NotFoundException {
        WorkLine workLine = workLineRepository.findByIdAndDeleteYnFalse(id);
        if (workLine == null)
            throw new NotFoundException("workLine does not exist. input id: " + id);
        return workLine;
    }


    // 라인코드 생성
    @Override
    public CodeResponse createWorkLineCode(CodeRequest codeRequest) {
        WorkLineCode workLineCode = mapper.toEntity(codeRequest, WorkLineCode.class);
        workLineCodeRepository.save(workLineCode);
        return mapper.toResponse(workLineCode, CodeResponse.class);
    }

    // 라인코드 단일 조회
    @Override
    public CodeResponse getWorkLineCode(Long id) throws NotFoundException {
        return mapper.toResponse(getWorkLineCodeOrThrow(id), CodeResponse.class);
    }

    // 라인코드 리스트 조회
    @Override
    public List<CodeResponse> getWorkLineCodes() {
        return mapper.toListResponses(workLineCodeRepository.findAll(), CodeResponse.class);
    }

    // 라인코드 삭제
    @Override
    public void deleteWorkLineCode(Long id) throws NotFoundException, BadRequestException {
        throwIfWorkLineCodeExist(id);
        workLineCodeRepository.deleteById(id);
    }

    // 라인코드 조회 및 예외
    private WorkLineCode getWorkLineCodeOrThrow(Long id) throws NotFoundException {
        return workLineCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("workLineCode does not exist. input id: " + id));
    }

    // workLineCode 삭제 시 workLine에 해당하는 workLineCode가 있으면 예외
    private void throwIfWorkLineCodeExist(Long workLineCodeId) throws NotFoundException, BadRequestException {
        WorkLineCode workLineCode = getWorkLineCodeOrThrow(workLineCodeId);
        boolean existByWorkLineCode = workLineRepository.existsByWorkLineCodeAndDeleteYnFalse(workLineCode);
        if (existByWorkLineCode)
            throw new BadRequestException("code exist in the workLine. input code id: " + workLineCodeId);
    }
}
