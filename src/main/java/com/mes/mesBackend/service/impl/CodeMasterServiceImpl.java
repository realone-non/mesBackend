package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.CodeMasterRequest;
import com.mes.mesBackend.dto.request.CodeMasterUpdateRequest;
import com.mes.mesBackend.dto.request.SubCodeMasterRequest;
import com.mes.mesBackend.dto.response.CodeMasterResponse;
import com.mes.mesBackend.dto.response.SubCodeMasterResponse;
import com.mes.mesBackend.entity.CodeMaster;
import com.mes.mesBackend.entity.SubCodeMaster;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.CodeMasterRepository;
import com.mes.mesBackend.repository.SubCodeMasterRepository;
import com.mes.mesBackend.service.CodeMasterService;
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
    @Autowired
    ModelMapper modelMapper;

    // 코드마스터 아이디로 조회
    private CodeMaster findByIdAndDeleteYnTrue(Long id) throws NotFoundException {
        CodeMaster findCodeMaster = codeMasterRepository.findByIdAndDeleteYnFalse(id);
        if (findCodeMaster == null) {
            throw new NotFoundException("codeMaster does not exists. input codeMasterId: " + id);
        }
        return findCodeMaster;
    }

    // 코드마스터 생성
    public CodeMasterResponse createCodeMaster(CodeMasterRequest codeMasterRequest) {
        List<SubCodeMasterRequest> subCodeMasterRequests = codeMasterRequest.getSubCodeMasterRequest();
        CodeMaster codeMaster = modelMapper.toEntity(codeMasterRequest, CodeMaster.class);
        CodeMaster saveCodeMaster = codeMasterRepository.save(codeMaster);
        CodeMasterResponse saveCodeMasterResponse = modelMapper.toResponse(saveCodeMaster, CodeMasterResponse.class);

        // 부코드 생성
        List<SubCodeMasterResponse> subCodeMasterResponses =  createSubCodeMasters(subCodeMasterRequests, saveCodeMaster);
        saveCodeMasterResponse.setSubCodeMasterResponse(subCodeMasterResponses);
        return saveCodeMasterResponse;
    }

    // 부코드 생성
    private List<SubCodeMasterResponse> createSubCodeMasters(List<SubCodeMasterRequest> subCodeMasterRequests, CodeMaster codeMaster) {
        List<SubCodeMasterResponse> subCodeMasterResponses = new ArrayList<>();

        // request -> entity
        List<SubCodeMaster> subCodeMasters = modelMapper.toEntities(subCodeMasterRequests, SubCodeMaster.class);

        // 한개씩 생성
        for (SubCodeMaster subCodeMaster : subCodeMasters) {
            subCodeMaster.setCodeMaster(codeMaster);
            SubCodeMaster saveSubCodeMaster = subCodeMasterRepository.save(subCodeMaster);
            subCodeMasterResponses.add(modelMapper.toResponse(saveSubCodeMaster, SubCodeMasterResponse.class));
        }
        return subCodeMasterResponses;
    }

    // 코드마스터 전체 조회 (검색조건 : 주코드, 코드명)
    public Page<CodeMasterResponse> getCodeMasters(String mainCode, String codeName, Pageable pageable) {
        Page<CodeMaster> codeMasters = codeMasterRepository.findByMainCodeAndCodeName(mainCode, codeName, pageable);
        return modelMapper.toPageResponses(codeMasters, CodeMasterResponse.class);
    }

    // 부코드 마스터 조회
    public List<SubCodeMasterResponse> getSubCodeMasters(Long id) throws NotFoundException {
        CodeMaster codeMaster = findByIdAndDeleteYnTrue(id);
        List<SubCodeMaster> subCodeMasters = subCodeMasterRepository.findSubCodeMasterByCodeMasterAndDeleteYnFalse(codeMaster);
        return modelMapper.toListResponses(subCodeMasters, SubCodeMasterResponse.class);
    }

    // 코드마스터 수정
    public CodeMasterResponse updateCodeMaster(Long id, CodeMasterUpdateRequest codeMasterUpdateRequest) throws NotFoundException {
        CodeMaster newCodeMaster = modelMapper.toEntity(codeMasterUpdateRequest, CodeMaster.class);
        CodeMaster findCodeMaster = findByIdAndDeleteYnTrue(id);
        // 수정매핑
        findCodeMaster.put(newCodeMaster);
        CodeMaster updateCodeMaster = codeMasterRepository.save(newCodeMaster);
        return modelMapper.toResponse(updateCodeMaster, CodeMasterResponse.class);
    }

    // 부코드마스터 수정
    public SubCodeMasterResponse updateSubCodeMaster(Long subCodeMasterId, SubCodeMasterRequest subCodeMasterRequest) {
        SubCodeMaster newSubCodeMaster = modelMapper.toEntity(subCodeMasterRequest, SubCodeMaster.class);
        SubCodeMaster findSubCodeMaster = subCodeMasterRepository.findByIdAndDeleteYnFalse(subCodeMasterId);
        // 수정매핑
        findSubCodeMaster.put(newSubCodeMaster);
        return modelMapper.toResponse(findSubCodeMaster, SubCodeMasterResponse.class);
    }

    // 부코트마스터 삭제
    public void deleteSubCodeMaster(Long id) {
        SubCodeMaster subCodeMaster = subCodeMasterRepository.findByIdAndDeleteYnFalse(id);
        subCodeMaster.setDeleteYn(true);
    }

    // 코드마스터 삭제
    public void deleteCodeMaster(Long id) throws NotFoundException {
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
    ) throws NotFoundException {
        CodeMaster codeMaster = findByIdAndDeleteYnTrue(codeMasterId);
        SubCodeMaster subCodeMaster = modelMapper.toEntity(subCodeMasterRequest, SubCodeMaster.class);
        subCodeMaster.setCodeMaster(codeMaster);
        return modelMapper.toResponse(subCodeMasterRepository.save(subCodeMaster), SubCodeMasterResponse.class);
    }

    // 코드마스터 단일 조회
    public CodeMasterResponse getCodeMaster(Long id) throws NotFoundException {
        CodeMasterResponse codeMasterResponse = modelMapper.toResponse(findByIdAndDeleteYnTrue(id), CodeMasterResponse.class);
        codeMasterResponse.setSubCodeMasterResponse(getSubCodeMasters(id));
        return codeMasterResponse;
    }
}
