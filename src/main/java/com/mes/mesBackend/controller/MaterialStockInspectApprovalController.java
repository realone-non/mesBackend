package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;
import com.mes.mesBackend.dto.response.MaterialStockInspectResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.repository.custom.MaterialStockInspectRequestRepositoryCustom;
import com.mes.mesBackend.service.MaterialWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

//재고실사 승인등록
@Tag(name = "material-stockinspect-approval", description = "재고실사승인 API")
@RequestMapping(value = "/material-stockinspect-approvals")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class MaterialStockInspectApprovalController {
    private final MaterialWarehouseService materialWarehouseService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(MaterialStockInspectRequestRepositoryCustom.class);
    private CustomLogger cLogger;

    //재고실사 승인 등록
    @PostMapping("/{request-id}/approvals/{user-id}")
    @ResponseBody
    @Operation(summary = "재고실사 승인 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "created"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<MaterialStockInspectResponse>> createStockInspectApproval(
            @PathVariable(value = "request-id") @Parameter(description = "실사의뢰 ID") Long requestId,
            @PathVariable(value = "user-id") @Parameter(description = "승인자 ID") Long userId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<MaterialStockInspectResponse> responseList = materialWarehouseService.createStockInspectApproval(requestId, userId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the stockInspect requestId:" + requestId + " from createStockInspectData.");
        return new ResponseEntity<>(responseList, OK);
    }

    //재고실사의뢰 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "재고실사의뢰 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<MaterialStockInspectRequestResponse>> getMaterialStockInspects(
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "시작날짜") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "종료날짜") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<MaterialStockInspectRequestResponse> responseList = materialWarehouseService.getMaterialStockInspectRequestList(fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getMaterialStockInspects.");
        return new ResponseEntity<>(responseList, OK);
    }

    //재고실사 조회
    @GetMapping("/{request-id}")
    @ResponseBody
    @Operation(summary = "재고실사 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<MaterialStockInspectResponse>> getMaterialStockInspects(
            @PathVariable(value = "request-id") @Parameter(description = "실사의뢰 ID") Long requestId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "시작날짜") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "종료날짜") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "아이템어카운트명") String itemAccount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<MaterialStockInspectResponse> responseList = materialWarehouseService.getMaterialStockInspects(requestId, fromDate, toDate, itemAccount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getMaterialStockInspects.");
        return new ResponseEntity<>(responseList, OK);
    }
}
