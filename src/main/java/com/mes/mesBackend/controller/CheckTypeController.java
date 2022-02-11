package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CheckTypeRequest;
import com.mes.mesBackend.dto.response.CheckTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.CheckTypeService;
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

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.OK;

// 점검유형 (일, 월, 분기 등등)
@Tag(name = "check-type", description = "점검유형 API")
@RequestMapping("/check-types")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class CheckTypeController {
    private final CheckTypeService checkTypeService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(CheckTypeController.class);
    private CustomLogger cLogger;

    // 점검유형 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "점검유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CheckTypeResponse> createCheckType(
            @RequestBody @Valid CheckTypeRequest checkTypeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        CheckTypeResponse checkType = checkTypeService.createCheckType(checkTypeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + checkType.getId() + " from createCheckType.");
        return new ResponseEntity<>(checkType, OK);
    }

    // 점검유형 단일조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "점검유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CheckTypeResponse> getCheckType(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        CheckTypeResponse checkType = checkTypeService.getCheckType(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + checkType.getId() + " from getCheckType.");
        return new ResponseEntity<>(checkType, OK);
    }

    // 점검유형 전체 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "점검유형 전체 조회")
    public ResponseEntity<List<CheckTypeResponse>> getCheckTypes(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<CheckTypeResponse> checkTypes = checkTypeService.getCheckTypes();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getCheckTypes.");
        return new ResponseEntity<>(checkTypes, OK);
    }

    // 점검유형 수정 api
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "점검유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CheckTypeResponse> updateCheckType(
            @PathVariable Long id,
            @RequestBody @Valid CheckTypeRequest checkTypeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        CheckTypeResponse checkType = checkTypeService.updateCheckType(id, checkTypeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + checkType.getId() + " from updateCheckType.");
        return new ResponseEntity<>(checkType, OK);
    }

    // 점검유형 삭제 api
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "점검유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteCheckType(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        checkTypeService.deleteCheckType(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteCheckType.");
        return new ResponseEntity(NO_CONTENT);
    }
}
