package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkDocumentRequest;
import com.mes.mesBackend.dto.response.WorkDocumentResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface WorkDocumentService {
    // 작업표준서 생성
    WorkDocumentResponse createWorkDocument(WorkDocumentRequest workDocumentRequest) throws NotFoundException;
    // 작업표준서 단일 조회
    WorkDocumentResponse getWorkDocument(Long id) throws NotFoundException;
    // 작업표준서 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    Page<WorkDocumentResponse> getWorkDocuments(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Pageable pageable);
    // 작업표준서 수정
    WorkDocumentResponse updateWorkDocument(Long id, WorkDocumentRequest workDocumentRequest) throws NotFoundException;
    // 작업표준서 삭제
    void deleteWorkDocument(Long id) throws NotFoundException;
    // 작업표준서 파일 추가
    WorkDocumentResponse createFileToWorkDocument(Long id, MultipartFile file) throws NotFoundException, BadRequestException, IOException;
}
