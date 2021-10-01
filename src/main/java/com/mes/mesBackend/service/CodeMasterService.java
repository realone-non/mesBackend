package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CodeMasterRequest;
import com.mes.mesBackend.dto.response.CodeMasterResponse;

public interface CodeMasterService {
    CodeMasterResponse createCodeMaster(CodeMasterRequest codeMasterRequest);
}
