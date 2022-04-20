package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WorkDocumentRequest;
import com.mes.mesBackend.dto.response.WorkDocumentResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.WorkDocument;
import com.mes.mesBackend.entity.WorkLine;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.impl.S3UploaderImpl;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkDocumentRepository;
import com.mes.mesBackend.service.ItemService;
import com.mes.mesBackend.service.WorkDocumentService;
import com.mes.mesBackend.service.WorkLineService;
import com.mes.mesBackend.service.WorkProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkDocumentServiceImpl implements WorkDocumentService {
    private final WorkDocumentRepository workDocumentRepository;
    private final ModelMapper mapper;
    private final WorkProcessService workProcessService;
    private final WorkLineService workLineService;
    private final ItemService itemService;
    private final S3UploaderImpl s3Service;

    // 작업표준서 생성
    @Override
    public WorkDocumentResponse createWorkDocument(WorkDocumentRequest workDocumentRequest) throws NotFoundException {
        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(workDocumentRequest.getWorkProcess());
        WorkLine workLine = workLineService.getWorkLineOrThrow(workDocumentRequest.getWorkLine());
        Item item = itemService.getItemOrThrow(workDocumentRequest.getItem());
        WorkDocument workDocument = mapper.toEntity(workDocumentRequest, WorkDocument.class);
        workDocument.addJoin(workProcess, workLine, item);
        workDocumentRepository.save(workDocument);
        return mapper.toResponse(workDocument, WorkDocumentResponse.class);
    }

    // 작업표준서 단일 조회
    @Override
    public WorkDocumentResponse getWorkDocument(Long id) throws NotFoundException {
        WorkDocument workDocument = getWorkDocumentOrThrow(id);
        return mapper.toResponse(workDocument, WorkDocumentResponse.class);
    }

    // 작업표준서 단일 조회 및 예외
    private WorkDocument getWorkDocumentOrThrow(Long id) throws NotFoundException {
        return workDocumentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workDocument does not exist. input id: " + id));
    }

    // 작업표준서 리스트 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    @Override
    public List<WorkDocumentResponse> getWorkDocuments(Long itemGroupId, Long itemAccountId, String itemNo, String itemName) {
        List<WorkDocument> workDocuments = workDocumentRepository.findAllByCondition(itemGroupId, itemAccountId, itemNo, itemName);
        return mapper.toListResponses(workDocuments, WorkDocumentResponse.class);
    }

    // 작업표준서 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
//    @Override
//    public Page<WorkDocumentResponse> getWorkDocuments(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Pageable pageable) {
//        Page<WorkDocument> workDocuments = workDocumentRepository.findAllByCondition(itemGroupId, itemAccountId, itemNo, itemName, pageable);
//        return mapper.toPageResponses(workDocuments, WorkDocumentResponse.class);
//    }

    // 작업표준서 수정
    @Override
    public WorkDocumentResponse updateWorkDocument(Long id, WorkDocumentRequest workDocumentRequest) throws NotFoundException {
        WorkProcess newWorkProcess = workProcessService.getWorkProcessOrThrow(workDocumentRequest.getWorkProcess());
        WorkLine newWorkLine = workLineService.getWorkLineOrThrow(workDocumentRequest.getWorkLine());
        Item newItem = itemService.getItemOrThrow(workDocumentRequest.getItem());
        WorkDocument findWorkDocument = getWorkDocumentOrThrow(id);
        WorkDocument newWorkDocument = mapper.toEntity(workDocumentRequest, WorkDocument.class);
        findWorkDocument.update(newWorkDocument, newWorkProcess, newWorkLine, newItem);
        workDocumentRepository.save(findWorkDocument);
        return mapper.toResponse(findWorkDocument, WorkDocumentResponse.class);
    }

    // 작업표준서 삭제
    @Override
    public void deleteWorkDocument(Long id) throws NotFoundException {
        WorkDocument workDocument = getWorkDocumentOrThrow(id);
        workDocument.delete();
        workDocumentRepository.save(workDocument);
    }

    // 작업표준서 파일 추가
    // work-document/workDocumentId/파일명(날짜시간)
    @Override
    public WorkDocumentResponse createFileToWorkDocument(Long id, MultipartFile file) throws NotFoundException, BadRequestException, IOException {
        WorkDocument workDocument = getWorkDocumentOrThrow(id);
        String fileName = "work-document/" + workDocument.getId() + "/";
        workDocument.addFile(s3Service.upload(file, fileName));
        workDocumentRepository.save(workDocument);
        return mapper.toResponse(workDocument, WorkDocumentResponse.class);
    }

    // 작업표준서 파일 삭제
    @Override
    public void deleteFileToWorkDocument(Long id) throws NotFoundException {
        WorkDocument workDocument = getWorkDocumentOrThrow(id);
        workDocument.setFileNameUrl(null);
        workDocumentRepository.save(workDocument);
    }
}
