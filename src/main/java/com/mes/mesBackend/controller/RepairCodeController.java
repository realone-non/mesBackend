package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.RepairCodeRequest;
import com.mes.mesBackend.dto.response.RepairCodeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.RepairCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 수리코드
@Tag(name = "repair-code", description = "수리코드 API")
@RequestMapping("/repair-codes")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class RepairCodeController {

    private final RepairCodeService repairCodeService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(RepairCodeController.class);
    private CustomLogger cLogger;

    // 수리코드 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "수리코드 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairCodeResponse> createRepairCode(
            @RequestBody @Valid RepairCodeRequest repairCodeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        RepairCodeResponse repairCode = repairCodeService.createRepairCode(repairCodeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + repairCode.getId() + " from createRepairCode.");
        return new ResponseEntity<>(repairCode, OK);
    }

    // 수리코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "수리코드 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<RepairCodeResponse> getRepairCode(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairCodeResponse repairCode = repairCodeService.getRepairCode(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + repairCode.getId() + " from getRepairCode.");
        return new ResponseEntity<>(repairCode, OK);
    }

    // 수리코드 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "수리코드 전체 조회")
    public ResponseEntity<List<RepairCodeResponse>> getRepairCodes(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<RepairCodeResponse> repairCodes = repairCodeService.getRepairCodes();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getRepairCodes.");
        return new ResponseEntity<>(repairCodes, OK);
    }

    // 수리코드 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "수리코드 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairCodeResponse> updateRepairCode(
            @PathVariable Long id,
            @RequestBody @Valid RepairCodeRequest repairCodeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairCodeResponse repairCode = repairCodeService.updateRepairCode(id, repairCodeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + repairCode.getId() + " from updateRepairCode.");
        return new ResponseEntity<>(repairCode, OK);
    }

    // 수리코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "수리코드 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteRepairCode(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        repairCodeService.deleteRepairCode(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteRepairCode.");
        return new ResponseEntity(NO_CONTENT);
    }
}
