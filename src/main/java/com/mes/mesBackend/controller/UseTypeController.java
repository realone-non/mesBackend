package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UseTypeRequest;
import com.mes.mesBackend.dto.response.UseTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.UseTypeService;
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

// 용도유형
@Tag(name = "use-type", description = "용도유형 API")
@RequestMapping(value = "/use-types")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class UseTypeController {
    @Autowired
    UseTypeService useTypeService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(UseTypeController.class);
    private CustomLogger cLogger;


    // 용도유형 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "용도유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UseTypeResponse> createUseType(
            @RequestBody @Valid UseTypeRequest useTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        UseTypeResponse useType = useTypeService.createUseType(useTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + useType.getId() + " from createUseType.");
        return new ResponseEntity<>(useType, HttpStatus.OK);
    }

    // 용도유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "용도유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<UseTypeResponse> getUseType(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        UseTypeResponse useType = useTypeService.getUseType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + useType.getId() + " from getUseType.");
        return new ResponseEntity<>(useType, HttpStatus.OK);
    }

    // 용도유형 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "용도유형 리스트 조회")
    public ResponseEntity<List<UseTypeResponse>> getUseTypes(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<UseTypeResponse> useTypes = useTypeService.getUseTypes();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getUseTypes.");
        return new ResponseEntity<>(useTypes, HttpStatus.OK);
    }

    // 용도유형 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "용도유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UseTypeResponse> updateUseType(
            @PathVariable Long id,
            @RequestBody @Valid UseTypeRequest useTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        UseTypeResponse useType = useTypeService.updateUseType(id, useTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + useType.getId() + " from updateUseType.");
        return new ResponseEntity<>(useType, HttpStatus.OK);
    }

    // 용도유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "용도유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteUseType(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        useTypeService.deleteUseType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteUseType.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
