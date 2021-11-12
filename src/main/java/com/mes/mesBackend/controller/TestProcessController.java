package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.TestProcessRequest;
import com.mes.mesBackend.dto.response.TestProcessResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.TestProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 검사방법
@RestController
@RequestMapping(value = "/test-processes")
@Api(tags = "test-process")
@RequiredArgsConstructor
public class TestProcessController {
    @Autowired
    TestProcessService testProcessService;

    // 검사방법 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "검사방법 생성")
    public ResponseEntity<TestProcessResponse> createTestProcess(
            @RequestBody @Valid TestProcessRequest testProcessRequest
    ) {
        return new ResponseEntity<>(testProcessService.createTestProcess(testProcessRequest), HttpStatus.OK);
    }

    // 검사방법 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "검사방법 단일 조회")
    public ResponseEntity<TestProcessResponse> getTestProcess(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(testProcessService.getTestProcess(id), HttpStatus.OK);
    }

    // 검사방법 리스트 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "검사방법 리스트 조회")
    public ResponseEntity<List<TestProcessResponse>> getTestProcesses() {
        return new ResponseEntity<>(testProcessService.getTestProcesses(), HttpStatus.OK);
    }

    // 검사방법 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "검사방법 수정")
    public ResponseEntity<TestProcessResponse> updateTestProcess(
            @PathVariable Long id,
            @RequestBody @Valid TestProcessRequest testProcessRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(testProcessService.updateTestProcess(id, testProcessRequest), HttpStatus.OK);
    }

    // 검사방법 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "검사방법 삭제")
    public ResponseEntity deleteTestProcess(
            @PathVariable Long id
    ) throws NotFoundException {
        testProcessService.deleteTestProcess(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
