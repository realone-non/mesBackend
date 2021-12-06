package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ItemGroupService;
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

// 3-2-2. 그룹코드 등록
@Tag(name = "item-group-code", description = "그룹코드 API")
@RequestMapping("/item-group-codes")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class ItemGroupCodeController {
    @Autowired
    ItemGroupService itemGroupCodeService;

    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(ItemGroupCodeController.class);
    private CustomLogger cLogger;


    // 그룹코드 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "그룹코드 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CodeResponse> createItemGroupCode(
            @RequestBody @Valid CodeRequest codeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        CodeResponse itemGroupCode = itemGroupCodeService.createItemGroupCode(codeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + itemGroupCode.getId() + " from createItemGroupCode.");
        return new ResponseEntity<>(itemGroupCode, HttpStatus.OK);
    }

    // 그룹코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "그룹코드 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CodeResponse> getItemGroupCode(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        CodeResponse itemGroupCode = itemGroupCodeService.getItemGroupCode(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + itemGroupCode.getId() + " from getItemGroupCode.");
        return new ResponseEntity<>(itemGroupCode, HttpStatus.OK);
    }

    // 그룹코드 리스트 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "그룹코드 리스트 조회")
    public ResponseEntity<List<CodeResponse>> getItemGroupCodes(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<CodeResponse> itemGroupCodes = itemGroupCodeService.getItemGroupCodes();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemGroupCodes.");
        return new ResponseEntity<>(itemGroupCodes, HttpStatus.OK);
    }

    // 그룹코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "그룹코드 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteItemGroupCode(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        itemGroupCodeService.deleteItemGroupCode(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteItemGroupCode.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
