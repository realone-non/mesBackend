package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.TestProcessRequest;
import com.mes.mesBackend.dto.response.TestProcessResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.TestProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;


// 검사방법
@Tag(name = "test-process", description = "검사방법 API")
@RequestMapping(value = "/test-processes")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class TestProcessController {
    private final TestProcessService testProcessService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(TestProcessController.class);
    private CustomLogger cLogger;
    
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
            @RequestBody @Valid TestProcessRequest testProcessRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        TestProcessResponse testProcess = testProcessService.createTestProcess(testProcessRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + testProcess.getId() + " from createTestProcess.");
        return new ResponseEntity<>(testProcess, OK);
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
    public ResponseEntity<TestProcessResponse> getTestProcess(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        TestProcessResponse testProcess = testProcessService.getTestProcess(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + testProcess.getId() + " from getTestProcess.");
        return new ResponseEntity<>(testProcess, OK);
    }

    // 검사방법 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "검사방법 리스트 조회")
    public ResponseEntity<List<TestProcessResponse>> getTestProcesses(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<TestProcessResponse> testProcesses = testProcessService.getTestProcesses();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getTestProcesses.");
        return new ResponseEntity<>(testProcesses, OK);
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
            @RequestBody @Valid TestProcessRequest testProcessRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        TestProcessResponse testProcess = testProcessService.updateTestProcess(id, testProcessRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + testProcess.getId() + " from updateTestProcess.");
        return new ResponseEntity<>(testProcess, OK);
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
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        testProcessService.deleteTestProcess(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteTestProcess.");
        return new ResponseEntity(NO_CONTENT);
    }
}
