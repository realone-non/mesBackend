package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeMasterRequest;
import com.mes.mesBackend.dto.response.CodeMasterResponse;
import com.mes.mesBackend.service.CodeMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/code-master")
@Api(tags = "code-master")
@RequiredArgsConstructor
public class CodeMasterController {

    @Autowired
    CodeMasterService codeMasterService;

    // 코드마스터 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "코드마스터 생성")
    public ResponseEntity<CodeMasterResponse> createCodeMaster(@RequestBody CodeMasterRequest codeMasterRequest){
        return new ResponseEntity<>(codeMasterService.createCodeMaster(codeMasterRequest), HttpStatus.OK);
    }

}
