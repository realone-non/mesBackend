package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;
import com.mes.mesBackend.dto.response.MaterialStockInspectResponse;
import com.mes.mesBackend.entity.enumeration.InspectionType;
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
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//재고실사 승인등록
@Tag(name = "material-stockinspect-approval")
@RequestMapping(value = "/material-stockinspect-approval")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class MaterialStockInspectApprovalController {
    @Autowired
    MaterialWarehouseService materialWarehouseService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(MaterialStockInspectRequestRepositoryCustom.class);
    private CustomLogger cLogger;

    //재고실사 승인 등록
    @PostMapping("/{request-id}/approval/{user-id}")
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
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<MaterialStockInspectResponse> responseList = materialWarehouseService.createStockInspectApproval(requestId, userId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the stockInspect requestId:" + requestId + " from createStockInspectData.");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "시작날짜") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "종료날짜") LocalDate toDate,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException{
        List<MaterialStockInspectRequestResponse> responseList = materialWarehouseService.getMaterialStockInspectRequestList(fromDate, toDate);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getMaterialStockInspects.");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "시작날짜") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "종료날짜") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "아이템어카운트명") String itemAccount,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException{
        List<MaterialStockInspectResponse> responseList = materialWarehouseService.getMaterialStockInspects(requestId, fromDate, toDate, itemAccount);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getMaterialStockInspects.");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
