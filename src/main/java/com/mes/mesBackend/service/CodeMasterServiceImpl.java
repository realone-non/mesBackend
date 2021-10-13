package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CodeMasterRequest;
import com.mes.mesBackend.dto.request.CodeMasterUpdateRequest;
import com.mes.mesBackend.dto.request.SubCodeMasterRequest;
import com.mes.mesBackend.dto.response.CodeMasterResponse;
import com.mes.mesBackend.dto.response.SubCodeMasterResponse;
import com.mes.mesBackend.entity.CodeMaster;
import com.mes.mesBackend.entity.SubCodeMaster;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.CodeMasterRepository;
import com.mes.mesBackend.repository.SubCodeMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeMasterServiceImpl implements CodeMasterService {

    @Autowired CodeMasterRepository codeMasterRepository;
    @Autowired SubCodeMasterRepository subCodeMasterRepository;
    @Autowired Mapper mapper;

    // 코드마스터 아이디로 조회
    private CodeMaster findByIdAndDeleteYnTrue(Long id) {
        return codeMasterRepository.findByIdAndDeleteYnFalse(id);
    }

    // 코드마스터 생성
    public CodeMasterResponse createCodeMaster(CodeMasterRequest codeMasterRequest) {
        List<SubCodeMasterRequest> subCodeMasterRequests = codeMasterRequest.getSubCodeMasterRequest();
        CodeMaster codeMaster = mapper.toEntity(codeMasterRequest, CodeMaster.class);
        CodeMaster saveCodeMaster = codeMasterRepository.save(codeMaster);
        CodeMasterResponse saveCodeMasterResponse = mapper.toResponse(saveCodeMaster, CodeMasterResponse.class);

        // 부코드 생성
        List<SubCodeMasterResponse> subCodeMasterResponses =  createSubCodeMasters(subCodeMasterRequests, saveCodeMaster);
        saveCodeMasterResponse.setSubCodeMasterResponse(subCodeMasterResponses);
        return saveCodeMasterResponse;
    }

    // 부코드 생성
    private List<SubCodeMasterResponse> createSubCodeMasters(List<SubCodeMasterRequest> subCodeMasterRequests, CodeMaster codeMaster) {
        List<SubCodeMasterResponse> subCodeMasterResponses = new ArrayList<>();

        // request -> entity
        List<SubCodeMaster> subCodeMasters = mapper.toEntities(subCodeMasterRequests, SubCodeMaster.class);

        // 한개씩 생성
        for (SubCodeMaster subCodeMaster : subCodeMasters) {
            subCodeMaster.setCodeMaster(codeMaster);
            SubCodeMaster saveSubCodeMaster = subCodeMasterRepository.save(subCodeMaster);
            subCodeMasterResponses.add(mapper.toResponse(saveSubCodeMaster, SubCodeMasterResponse.class));
        }
        return subCodeMasterResponses;
    }

    // 코드마스터 전체 조회 (검색조건 : 주코드, 코드명)
    public Page<CodeMasterResponse> getCodeMasters(String mainCode, String codeName, Pageable pageable) {
        Page<CodeMaster> codeMasters = codeMasterRepository.findByMainCodeAndCodeName(mainCode, codeName, pageable);
        return mapper.toPageResponses(codeMasters, CodeMasterResponse.class);
    }

    // 부코드 마스터 조회
    public List<SubCodeMasterResponse> getSubCodeMasters(Long id) {
        CodeMaster codeMaster = findByIdAndDeleteYnTrue(id);
        List<SubCodeMaster> subCodeMasters = subCodeMasterRepository.findSubCodeMasterByCodeMasterAndDeleteYnFalse(codeMaster);
        return mapper.toListResponses(subCodeMasters, SubCodeMasterResponse.class);
    }

    // 코드마스터 수정
    public CodeMasterResponse updateCodeMaster(Long id, CodeMasterUpdateRequest codeMasterUpdateRequest) {
        CodeMaster newCodeMaster = mapper.toEntity(codeMasterUpdateRequest, CodeMaster.class);
        CodeMaster findCodeMaster = findByIdAndDeleteYnTrue(id);
        // 수정매핑
        findCodeMaster.put(newCodeMaster);
        CodeMaster updateCodeMaster = codeMasterRepository.save(newCodeMaster);
        return mapper.toResponse(updateCodeMaster, CodeMasterResponse.class);
    }

    // 부코드마스터 수정
    public SubCodeMasterResponse updateSubCodeMaster(Long subCodeMasterId, SubCodeMasterRequest subCodeMasterRequest) {
        SubCodeMaster newSubCodeMaster = mapper.toEntity(subCodeMasterRequest, SubCodeMaster.class);
        SubCodeMaster findSubCodeMaster = subCodeMasterRepository.findByIdAndDeleteYnFalse(subCodeMasterId);
        // 수정매핑
        findSubCodeMaster.put(newSubCodeMaster);
        return mapper.toResponse(findSubCodeMaster, SubCodeMasterResponse.class);
    }

    // 부코트마스터 삭제
    public void deleteSubCodeMaster(Long id) {
        SubCodeMaster subCodeMaster = subCodeMasterRepository.findByIdAndDeleteYnFalse(id);
        subCodeMaster.setDeleteYn(true);
    }

    // 코드마스터 삭제
    public void deleteCodeMaster(Long id) {
        CodeMaster codeMaster = findByIdAndDeleteYnTrue(id);
        List<SubCodeMaster> subCodeMasterList = subCodeMasterRepository.findSubCodeMasterByCodeMasterAndDeleteYnFalse(codeMaster);
        codeMaster.setDeleteYn(true);
        // 주코드에 해당하는 부코드 삭제
        subCodeMasterList.forEach(subCodeMaster -> deleteSubCodeMaster(subCodeMaster.getId()));
        codeMasterRepository.save(codeMaster);
    }

    // 부코드 생성
    public SubCodeMasterResponse createSubCodeMaster(
            Long codeMasterId,
            SubCodeMasterRequest subCodeMasterRequest
    ) {
        CodeMaster codeMaster = findByIdAndDeleteYnTrue(codeMasterId);
        SubCodeMaster subCodeMaster = mapper.toEntity(subCodeMasterRequest, SubCodeMaster.class);
        subCodeMaster.setCodeMaster(codeMaster);
        return mapper.toResponse(subCodeMasterRepository.save(subCodeMaster), SubCodeMasterResponse.class);
    }

    // 코드마스터 단일 조회
    public CodeMasterResponse getCodeMaster(Long id) {
        CodeMasterResponse codeMasterResponse = mapper.toResponse(findByIdAndDeleteYnTrue(id), CodeMasterResponse.class);
        codeMasterResponse.setSubCodeMasterResponse(getSubCodeMasters(id));
        return codeMasterResponse;
    }
}
