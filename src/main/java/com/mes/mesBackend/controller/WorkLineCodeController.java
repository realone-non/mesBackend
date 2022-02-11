package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkLineService;
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

// 3-3-3. 작업라인 코드 등록
@Tag(name = "work-line-code", description = "작업라인 코드 API")
@RequestMapping("/work-line-codes")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class WorkLineCodeController {
    private final WorkLineService workLineCodeService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(WorkLineCodeController.class);
    private CustomLogger cLogger;

    // 라인코드 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "라인코드 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CodeResponse> createWorkLineCode(
            @RequestBody @Valid CodeRequest codeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        CodeResponse workLineCode = workLineCodeService.createWorkLineCode(codeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + workLineCode.getId() + " from createWorkLineCode.");
        return new ResponseEntity<>(workLineCode, OK);
    }

    // 라인코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "라인코드 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CodeResponse> getWorkLineCode(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        CodeResponse workLineCode = workLineCodeService.getWorkLineCode(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workLineCode.getId() + " from getWorkLineCode.");
        return new ResponseEntity<>(workLineCode, OK);
    }

    // 라인코드 리스트 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "라인코드 리스트 조회")
    public ResponseEntity<List<CodeResponse>> getWorkLineCodes(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<CodeResponse> workLineCodes = workLineCodeService.getWorkLineCodes();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkLineCodes.");
        return new ResponseEntity<>(workLineCodes, OK);
    }

    // 라인코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "라인코드 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity deleteWorkLineCode(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        workLineCodeService.deleteWorkLineCode(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteWorkLineCode.");
        return new ResponseEntity(NO_CONTENT);
    }
    
}
