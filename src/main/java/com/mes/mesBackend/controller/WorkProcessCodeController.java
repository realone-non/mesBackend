package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 3-3-2. 작업 공정 코드 등록
@RestController
@RequestMapping("/work-process-codes")
@Api(tags = "work-process-code")
@RequiredArgsConstructor
public class WorkProcessCodeController {

    @Autowired
    WorkProcessService workProcessCodeService;

    //  코드 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "작업공정 코드 생성")
    public ResponseEntity<CodeResponse> createWorkProcessCode(
            @RequestBody CodeRequest codeRequest
    ) {
        return new ResponseEntity<>(workProcessCodeService.createWorkProcessCode(codeRequest), HttpStatus.OK);
    }

    // 코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업공정 코드 조회")
    public ResponseEntity<CodeResponse> getWorkProcessCode(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workProcessCodeService.getWorkProcessCode(id), HttpStatus.OK);
    }

    // 코드 리스트 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "작업공정 코드 리스트 조회")
    public ResponseEntity<List<CodeResponse>> getWorkProcessCodes() {
        return new ResponseEntity<>(workProcessCodeService.getWorkProcessCodes(), HttpStatus.OK);
    }

    // 코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업공정 코드 삭제")
    public ResponseEntity deleteWorkProcessCode(
            @PathVariable Long id
    ) throws NotFoundException, BadRequestException {
        workProcessCodeService.deleteWorkProcessCode(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
