package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.ProductionPlanResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ProductionPlanService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// 6-3. 생산계획 수립
@RequestMapping(value = "/production-plans")
@Tag(name = "production-plan", description = "생산게획 수립 API")
@RestController
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class ProductionPlanController {
    private final ProductionPlanService productionPlanService;
    private final LogService logService;
    private Logger logger = LoggerFactory.getLogger(ProductionPlanController.class);
    private CustomLogger cLogger;

    // 생산계획 수립 전체 조회, 검색조건: 작업라인, 작업예정일
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "생산계획 수립 정보 리스트 조회",
            description = "검색조건: 작업라인 id, 작업예정일 fromDate~toDate"
    )
    public ResponseEntity<List<ProductionPlanResponse>> getProductionPlans(
            @RequestParam(required = false) @Parameter(description = "작업라인 id") Long workLineId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "작업예정일 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "작업예정일 toDate") LocalDate toDate,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ProductionPlanResponse> productionPlans = productionPlanService.getProductionPlans(workLineId, fromDate, toDate);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProductionPlans.");
        return new ResponseEntity<>(productionPlans, HttpStatus.OK);
    }

    // 생산계획 수립 단일조회
    @GetMapping("/{work-order-id}")
    @ResponseBody
    @Operation(summary = "생산계획 수립 단일조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ProductionPlanResponse> getProductionPlan(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업 지시 id") Long workOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ProductionPlanResponse productionPlan = productionPlanService.getProductionPlan(workOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProductionPlan.");
        return new ResponseEntity<>(productionPlan, HttpStatus.OK);
    }

    // 생산계획 수립 등록(작업순번만)
    @PutMapping(value = "/{work-order-id}")
    @ResponseBody
    @Operation(summary = "생산계획 수립 등록", description = "작업순번만 등록 가능")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ProductionPlanResponse> createProductionPlanOrder(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업 지시 id") Long workOrderId,
            @RequestParam @Parameter(description = "작업순번") int orders,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ProductionPlanResponse productionPlan = productionPlanService.createProductionPlanOrder(workOrderId, orders);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + productionPlan.getId() + " from createProductionPlanOrder.");
        return new ResponseEntity<>(productionPlan, HttpStatus.OK);
    }
}
