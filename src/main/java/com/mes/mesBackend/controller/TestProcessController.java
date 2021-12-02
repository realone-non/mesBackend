package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.TestProcessRequest;
import com.mes.mesBackend.dto.response.TestProcessResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.TestProcessService;
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

// 검사방법
@Tag(name = "test-process", description = "검사방법 API")
@RequestMapping(value = "/test-processes")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class TestProcessController {
    @Autowired
    TestProcessService testProcessService;

    // 검사방법 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "검사방법 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<TestProcessResponse> createTestProcess(
            @RequestBody @Valid TestProcessRequest testProcessRequest
    ) {
        return new ResponseEntity<>(testProcessService.createTestProcess(testProcessRequest), HttpStatus.OK);
    }

    // 검사방법 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "검사방법 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<TestProcessResponse> getTestProcess(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(testProcessService.getTestProcess(id), HttpStatus.OK);
    }

    // 검사방법 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "검사방법 리스트 조회")
    public ResponseEntity<List<TestProcessResponse>> getTestProcesses() {
        return new ResponseEntity<>(testProcessService.getTestProcesses(), HttpStatus.OK);
    }

    // 검사방법 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "검사방법 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<TestProcessResponse> updateTestProcess(
            @PathVariable Long id,
            @RequestBody @Valid TestProcessRequest testProcessRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(testProcessService.updateTestProcess(id, testProcessRequest), HttpStatus.OK);
    }

    // 검사방법 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "검사방법 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteTestProcess(
            @PathVariable Long id
    ) throws NotFoundException {
        testProcessService.deleteTestProcess(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
