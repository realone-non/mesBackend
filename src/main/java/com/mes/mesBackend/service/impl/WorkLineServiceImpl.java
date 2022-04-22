package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WorkLineRequest;
import com.mes.mesBackend.dto.response.WorkLineResponse;
import com.mes.mesBackend.entity.WorkCenter;
import com.mes.mesBackend.entity.WorkLine;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkLineRepository;
import com.mes.mesBackend.service.WorkCenterService;
import com.mes.mesBackend.service.WorkLineService;
import com.mes.mesBackend.service.WorkProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkLineServiceImpl implements WorkLineService {
    private final WorkLineRepository workLineRepository;
    private final WorkCenterService workCenterService;
    private final WorkProcessService workProcessService;
    private final ModelMapper mapper;


    // 작업라인 생성
    @Override
    public WorkLineResponse createWorkLine(WorkLineRequest workLineRequest) throws NotFoundException {
        WorkCenter workCenter = workLineRequest.getWorkCenter() != null ? workCenterService.getWorkCenterOrThrow(workLineRequest.getWorkCenter()) : null;
        WorkProcess workProcess = workLineRequest.getWorkProcess() != null ? workProcessService.getWorkProcessOrThrow(workLineRequest.getWorkProcess()) : null;

        WorkLine workLine = mapper.toEntity(workLineRequest, WorkLine.class);
        workLine.addMapping(workCenter, workProcess);
        workLineRepository.save(workLine);
        return mapper.toResponse(workLine, WorkLineResponse.class);
    }


    // 작업라인 단일 조회
    @Override
    public WorkLineResponse getWorkLine(Long id) throws NotFoundException {
        WorkLine workLine = getWorkLineOrThrow(id);
        return mapper.toResponse(workLine, WorkLineResponse.class);
    }

    // 작업라인 전체 조회
    @Override
    public List<WorkLineResponse> getWorkLines() {
        List<WorkLine> workLines = workLineRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc();
        return mapper.toListResponses(workLines, WorkLineResponse.class);
    }

    // 작업라인 페이징 조회
//    @Override
//    public Page<WorkLineResponse> getWorkLines(Pageable pageable) {
//        Page<WorkLine> workLines = workLineRepository.findAllByDeleteYnFalse(pageable);
//        return mapper.toPageResponses(workLines, WorkLineResponse.class);
//    }

    // 작업라인 수정
    @Override
    public WorkLineResponse updateWorkLine(Long id, WorkLineRequest workLineRequest) throws NotFoundException {
        WorkCenter newWorkCenter = workLineRequest.getWorkCenter() != null ? workCenterService.getWorkCenterOrThrow(workLineRequest.getWorkCenter()) : null;
        WorkProcess newWorkProcess = workLineRequest.getWorkProcess() != null ? workProcessService.getWorkProcessOrThrow(workLineRequest.getWorkProcess()) : null;

        WorkLine findWorkLine = getWorkLineOrThrow(id);
        WorkLine newWorkLine = mapper.toEntity(workLineRequest, WorkLine.class);

        findWorkLine.put(newWorkLine, newWorkCenter, newWorkProcess);
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
        return workLineRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workLine does not exist. input id: " + id));
    }


//    // 라인코드 생성
//    @Override
//    public CodeResponse createWorkLineCode(CodeRequest codeRequest) {
//        WorkLineCode workLineCode = mapper.toEntity(codeRequest, WorkLineCode.class);
//        workLineCodeRepository.save(workLineCode);
//        return mapper.toResponse(workLineCode, CodeResponse.class);
//    }
//
//    // 라인코드 단일 조회
//    @Override
//    public CodeResponse getWorkLineCode(Long id) throws NotFoundException {
//        return mapper.toResponse(getWorkLineCodeOrThrow(id), CodeResponse.class);
//    }
//
//    // 라인코드 리스트 조회
//    @Override
//    public List<CodeResponse> getWorkLineCodes() {
//        return mapper.toListResponses(workLineCodeRepository.findAll(), CodeResponse.class);
//    }
//
//    // 라인코드 삭제
//    @Override
//    public void deleteWorkLineCode(Long id) throws NotFoundException, BadRequestException {
//        throwIfWorkLineCodeExist(id);
//        workLineCodeRepository.deleteById(id);
//    }
//
//    // 라인코드 조회 및 예외
//    private WorkLineCode getWorkLineCodeOrThrow(Long id) throws NotFoundException {
//        return workLineCodeRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("workLineCode does not exist. input id: " + id));
//    }
//
//    // workLineCode 삭제 시 workLine에 해당하는 workLineCode가 있으면 예외
//    private void throwIfWorkLineCodeExist(Long workLineCodeId) throws NotFoundException, BadRequestException {
//        WorkLineCode workLineCode = getWorkLineCodeOrThrow(workLineCodeId);
//        boolean existByWorkLineCode = workLineRepository.existsByWorkLineCodeAndDeleteYnFalse(workLineCode);
//        if (existByWorkLineCode)
//            throw new BadRequestException("code exist in the workLine. input code id: " + workLineCodeId);
//    }
}
