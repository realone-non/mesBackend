package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeMasterRequest;
import com.mes.mesBackend.dto.request.CodeMasterUpdateRequest;
import com.mes.mesBackend.dto.request.SubCodeMasterRequest;
import com.mes.mesBackend.dto.response.CodeMasterResponse;
import com.mes.mesBackend.dto.response.SubCodeMasterResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.CodeMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/code-masters")
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

    // 코드마스터 페이징 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "코드마스터 페이징 조회", notes = "검색조건 : 주코드, 코드명")
    public ResponseEntity<Page<CodeMasterResponse>> getCodeMasters(
            @RequestParam(required = false) String mainCode,
            @RequestParam(required = false) String codeName,
            @PageableDefault Pageable pageable
    ) {
        return new ResponseEntity<>(codeMasterService.getCodeMasters(mainCode, codeName, pageable), HttpStatus.OK);
    }

    // 부코드 마스터 조회
    @GetMapping("/{id}/sub-code-masters")
    @ResponseBody
    @ApiOperation(value = "부코드마스터 조회",notes = "해당 주코드에 해당하는 부코드 전체 조회")
    public ResponseEntity<List<SubCodeMasterResponse>> getSubCodeMasters(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(codeMasterService.getSubCodeMasters(id), HttpStatus.OK);
    }

    // 코드마스터 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "코드마스터 수정")
    public ResponseEntity<CodeMasterResponse> updateCodeMaster(
            @PathVariable Long id,
            @RequestBody CodeMasterUpdateRequest codeMasterUpdateRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(codeMasterService.updateCodeMaster(id, codeMasterUpdateRequest), HttpStatus.OK);
    }

    // 부코드마스터 수정
    @PatchMapping("/sub-code-masters/{id}")
    @ResponseBody
    @ApiOperation(value = "부코드마스터 수정")
    public ResponseEntity<SubCodeMasterResponse> updateSubCodeMaster(
            @PathVariable Long id,
            @RequestBody SubCodeMasterRequest subCodeMasterRequest
    ) {
        return new ResponseEntity<>(codeMasterService.updateSubCodeMaster(id, subCodeMasterRequest), HttpStatus.OK);
    }

    // 부코드마스터 삭제
    @DeleteMapping("/sub-code-masters/{id}")
    @ResponseBody
    @ApiOperation(value = "부코드마스터 삭제")
    public ResponseEntity deleteSubCodeMaster(@PathVariable Long id) {
        codeMasterService.deleteSubCodeMaster(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 코드마스터 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "코드마스터 삭제")
    public ResponseEntity deleteCodeMaster(@PathVariable Long id) throws NotFoundException {
        codeMasterService.deleteCodeMaster(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 부코드 생성
    @PostMapping("/{id}/sub-code-masters")
    @ResponseBody
    @ApiOperation(value = "부코드마스터 생성")
    public ResponseEntity<SubCodeMasterResponse> createSubCodeMaster(
            @PathVariable(value = "id") Long codeMasterId,
            @RequestBody SubCodeMasterRequest subCodeMasterRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(codeMasterService.createSubCodeMaster(codeMasterId, subCodeMasterRequest), HttpStatus.OK);
    }

    // 부코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "코드마스터 단일 조회")
    public ResponseEntity<CodeMasterResponse> getCodeMaster(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(codeMasterService.getCodeMaster(id), HttpStatus.OK);
    }
}
