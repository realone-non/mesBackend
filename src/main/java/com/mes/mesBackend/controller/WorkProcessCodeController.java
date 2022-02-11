package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkProcessService;
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

// 3-3-2. 작업 공정 코드 등록
@Tag(name = "work-process-code", description = "작업 공정 코드 API")
@RequestMapping("/work-process-codes")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class WorkProcessCodeController {
    private final WorkProcessService workProcessCodeService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(WorkProcessCodeController.class);
    private CustomLogger cLogger;

    //  코드 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "작업공정 코드 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CodeResponse> createWorkProcessCode(
            @RequestBody @Valid CodeRequest codeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        CodeResponse workProcessCode = workProcessCodeService.createWorkProcessCode(codeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + workProcessCode.getId() + " from createWorkProcessCode.");
        return new ResponseEntity<>(workProcessCode, OK);
    }

    // 코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업공정 코드 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CodeResponse> getWorkProcessCode(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        CodeResponse workProcessCode = workProcessCodeService.getWorkProcessCode(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workProcessCode.getId() + " from getWorkProcessCode.");
        return new ResponseEntity<>(workProcessCode, OK);
    }

    // 코드 리스트 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "작업공정 코드 리스트 조회")
    public ResponseEntity<List<CodeResponse>> getWorkProcessCodes(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<CodeResponse> workProcessCodes = workProcessCodeService.getWorkProcessCodes();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkProcessCodes.");
        return new ResponseEntity<>(workProcessCodes, OK);
    }

    // 코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업공정 코드 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteWorkProcessCode(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        workProcessCodeService.deleteWorkProcessCode(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteWorkProcessCode.");
        return new ResponseEntity(NO_CONTENT);
    }
}
