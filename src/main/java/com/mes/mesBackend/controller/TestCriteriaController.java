package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.TestCriteriaRequest;
import com.mes.mesBackend.dto.response.TestCriteriaResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.TestCriteriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 검사기준
@Tag(name = "test-criteria", description = "검사기준 API")
@RequestMapping(value = "/test-criterias")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class TestCriteriaController {
    @Autowired
    TestCriteriaService testCriteriaService;

    // 검사기준 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "검사기준 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<TestCriteriaResponse> createTestCriteria(
            @RequestBody @Valid TestCriteriaRequest testCriteriaRequest
    ) {
        return new ResponseEntity<>(testCriteriaService.createTestCriteria(testCriteriaRequest), HttpStatus.OK);
    }

    // 검사기준 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "검사기준 단일 조회")
    public ResponseEntity<TestCriteriaResponse> getTestCriteria(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(testCriteriaService.getTestCriteria(id), HttpStatus.OK);
    }

    // 검사기준 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "검사기준 리스트 조회")
    public ResponseEntity<List<TestCriteriaResponse>> getTestCriterias() {
        return new ResponseEntity<>(testCriteriaService.getTestCriterias(), HttpStatus.OK);
    }

    // 검사기준 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "검사기준 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<TestCriteriaResponse> updateTestCriteria(
            @PathVariable Long id,
            @RequestBody @Valid TestCriteriaRequest testCriteriaRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(testCriteriaService.updateTestCriteria(id, testCriteriaRequest), HttpStatus.OK);
    }

    // 검사기준 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "검사기준 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteTestCriteria(
            @PathVariable Long id
    ) throws NotFoundException {
        testCriteriaService.deleteTestCriteria(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
