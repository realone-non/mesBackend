package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.TestCriteriaRequest;
import com.mes.mesBackend.dto.response.TestCriteriaResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.TestCriteriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 검사기준
@RestController
@RequestMapping(value = "/test-criterias")
@Api(tags = "test-criteria")
@RequiredArgsConstructor
public class TestCriteriaController {
    @Autowired
    TestCriteriaService testCriteriaService;

    // 검사기준 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "검사기준 생성")
    public ResponseEntity<TestCriteriaResponse> createTestCriteria(
            @RequestBody TestCriteriaRequest testCriteriaRequest
    ) {
        return new ResponseEntity<>(testCriteriaService.createTestCriteria(testCriteriaRequest), HttpStatus.OK);
    }

    // 검사기준 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "검사기준 단일 조회")
    public ResponseEntity<TestCriteriaResponse> getTestCriteria(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(testCriteriaService.getTestCriteria(id), HttpStatus.OK);
    }

    // 검사기준 리스트 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "검사기준 리스트 조회")
    public ResponseEntity<List<TestCriteriaResponse>> getTestCriterias() {
        return new ResponseEntity<>(testCriteriaService.getTestCriterias(), HttpStatus.OK);
    }

    // 검사기준 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "검사기준 수정")
    public ResponseEntity<TestCriteriaResponse> updateTestCriteria(
            @PathVariable Long id,
            @RequestBody TestCriteriaRequest testCriteriaRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(testCriteriaService.updateTestCriteria(id, testCriteriaRequest), HttpStatus.OK);
    }

    // 검사기준 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "검사기준 삭제")
    public ResponseEntity deleteTestCriteria(
            @PathVariable Long id
    ) throws NotFoundException {
        testCriteriaService.deleteTestCriteria(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
