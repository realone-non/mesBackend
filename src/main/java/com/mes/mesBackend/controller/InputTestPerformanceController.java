package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.InputTestPerformanceResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.InputTestPerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// 14-3. 검사실적 조회
@RequestMapping(value = "/input-test-performances")
@Tag(name = "input-test-performance", description = "검사실적조회 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class InputTestPerformanceController {
    private final InputTestPerformanceService inputTestPerformanceService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(InputTestPerformanceController.class);
    private CustomLogger cLogger;

    // 검사실적조회
    // 검색조건: 검사기간 fromDate~toDate, 품목 id, 거래처 id, 입고번호(구매입고 id)
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "검사실적조회",
            description = "검색조건: 검사기간 fromDate~toDate, 품명|품번, 거래처 id, 입고번호(구매입고 id)")
    public ResponseEntity<List<InputTestPerformanceResponse>> getInputTestPerformances(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "검사기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "검사기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoAndName,
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @Parameter(description = "입고번호 (purchaseInputId)") Long purchaseInputNo,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<InputTestPerformanceResponse> inputTestPerformanceResponses = inputTestPerformanceService.getInputTestPerformances(fromDate, toDate, itemNoAndName, clientId, purchaseInputNo);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getInputTestPerformances.");
        return new ResponseEntity<>(inputTestPerformanceResponses, HttpStatus.OK);
    }
}
