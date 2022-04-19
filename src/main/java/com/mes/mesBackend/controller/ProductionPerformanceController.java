package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ProductionPerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

// 8-6. 생산실적 관리
@Tag(name = "production-performance", description = "8-6. 생산실적 관리 API")
@RequestMapping("/production-performances")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class ProductionPerformanceController {
    private final ProductionPerformanceService productionPerformanceService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ProductionPerformanceController.class);
    private CustomLogger cLogger;

    // 생산실적 리스트 조회, 검색조건: 조회기간 fromDate~toDate, 작업공정 id
    @Operation(summary = "생산실적관리 리스트 조회", description = "조회기간 fromDate~toDate, 작업공정 id")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductionPerformanceResponse>> getProductionPerformances(
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업시작일 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업시작일 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "작업공정 id") Long workProcessId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ProductionPerformanceResponse> productionPerformanceResponses = productionPerformanceService.getProductionPerformances(fromDate, toDate, workProcessId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProductionPerformances.");
        return new ResponseEntity<>(productionPerformanceResponses, OK);
    }
}
