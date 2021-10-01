package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CodeMasterRequest;
import com.mes.mesBackend.dto.request.SubCodeMasterRequest;
import com.mes.mesBackend.dto.response.CodeMasterResponse;
import com.mes.mesBackend.entity.CodeMaster;
import com.mes.mesBackend.entity.SubCodeMaster;
import com.mes.mesBackend.repository.CodeMasterRepository;
import com.mes.mesBackend.repository.SubCodeMasterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeMasterServiceImpl implements CodeMasterService {

    @Autowired
    CodeMasterRepository codeMasterRepository;

    @Autowired
    SubCodeMasterRepository subCodeMasterRepository;

    @Autowired
    ModelMapper modelMapper;


    public CodeMasterResponse createCodeMaster(CodeMasterRequest codeMasterRequest) {
        List<SubCodeMasterRequest> subCodeMasterRequests = codeMasterRequest.getSubCodeMasterRequest();

//        List<SubCodeMaster> subCodeMasters = subCodeMasterRequests
//                .stream()
//                .map(subCodeMasterRequest ->
//                        modelMapper.map(subCodeMasterRequest, SubCodeMaster.class)).collect(Collectors.toList());
//
//        subCodeMasterRepository.saveAll(subCodeMasters);
//
        CodeMaster codeMaster = modelMapper.map(codeMasterRequest, CodeMaster.class);

        CodeMaster saveCodeMaster = codeMasterRepository.save(codeMaster);

        return modelMapper.map(saveCodeMaster, CodeMasterResponse.class);
    }

}
