package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CodeMasterRequest;
import com.mes.mesBackend.dto.request.CodeMasterUpdateRequest;
import com.mes.mesBackend.dto.request.SubCodeMasterRequest;
import com.mes.mesBackend.dto.response.CodeMasterResponse;
import com.mes.mesBackend.dto.response.SubCodeMasterResponse;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CodeMasterService {
    // 코드마스터 생성
    CodeMasterResponse createCodeMaster(CodeMasterRequest codeMasterRequest);

    // 코드마스터 전체 조회
    // mainCode, codeName 조건검색
    Page<CodeMasterResponse> getCodeMasters(String mainCode, String codeName, Pageable pageable);

    // 부코드 마스터 조회
    List<SubCodeMasterResponse> getSubCodeMasters(Long id) throws NotFoundException;

    // 코드마스터 수정
    CodeMasterResponse updateCodeMaster(Long id, CodeMasterUpdateRequest codeMasterUpdateRequest) throws NotFoundException;

    // 부코드마스터 수정
    SubCodeMasterResponse updateSubCodeMaster(Long subCodeMasterId, SubCodeMasterRequest subCodeMasterRequest);

    // 부코트마스터 삭제
    void deleteSubCodeMaster(Long id);

    // 코드마스터 삭제
    void deleteCodeMaster(Long id) throws NotFoundException;

    // 부코드마스터 생성
    SubCodeMasterResponse createSubCodeMaster(Long codeMasterId, SubCodeMasterRequest subCodeMasterRequest) throws NotFoundException;

    CodeMasterResponse getCodeMaster(Long id) throws NotFoundException;
}