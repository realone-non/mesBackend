package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.PurchaseStatusCheckResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PurchaseInputService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

// 9-4. 구매현황 조회
@RequestMapping(value = "/purchase-status-checks")
@Tag(name = "purchase-status-check", description = "구매현황 조회 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class PurchaseStatusCheckController {
    private final PurchaseInputService purchaseInputService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PurchaseStatusCheckController.class);
    private CustomLogger cLogger;

    // 구매현황 리스트 조회
    // 검색조건: 거래처 id, 품명|품목, 입고기간 fromDate~toDate
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "구매현황 리스트 조회",
            description = "검색조건: 거래처 id, 품명|품목, 입고기간 fromDate~toDate"
    )
    public ResponseEntity<List<PurchaseStatusCheckResponse>> getPurchaseStatusChecks(
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @Parameter(description = "품명|품목") String itemNoAndItemName,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "입고기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "입고기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PurchaseStatusCheckResponse> purchaseStatusChecks = purchaseInputService.getPurchaseStatusChecks(clientId, itemNoAndItemName, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseStatusChecks.");
        return new ResponseEntity<>(purchaseStatusChecks, OK);
    }
}
