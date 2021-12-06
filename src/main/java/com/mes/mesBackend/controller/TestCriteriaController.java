package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.TestCriteriaRequest;
import com.mes.mesBackend.dto.response.TestCriteriaResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.TestCriteriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(TestCriteriaController.class);
    private CustomLogger cLogger;


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
            @RequestBody @Valid TestCriteriaRequest testCriteriaRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        TestCriteriaResponse testCriteria = testCriteriaService.createTestCriteria(testCriteriaRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + testCriteria.getId() + " from createTestCriteria.");
        return new ResponseEntity<>(testCriteria, HttpStatus.OK);
    }

    // 검사기준 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "검사기준 단일 조회")
    public ResponseEntity<TestCriteriaResponse> getTestCriteria(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        TestCriteriaResponse testCriteria = testCriteriaService.getTestCriteria(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + testCriteria.getId() + " from getTestCriteria.");
        return new ResponseEntity<>(testCriteria, HttpStatus.OK);
    }

    // 검사기준 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "검사기준 리스트 조회")
    public ResponseEntity<List<TestCriteriaResponse>> getTestCriterias(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<TestCriteriaResponse> testCriterias = testCriteriaService.getTestCriterias();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getTestCriterias.");
        return new ResponseEntity<>(testCriterias, HttpStatus.OK);
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
            @RequestBody @Valid TestCriteriaRequest testCriteriaRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        TestCriteriaResponse testCriteria = testCriteriaService.updateTestCriteria(id, testCriteriaRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + testCriteria.getId() + " from updateTestCriteria.");
        return new ResponseEntity<>(testCriteria, HttpStatus.OK);
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
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        testCriteriaService.deleteTestCriteria(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteTestCriteria.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
