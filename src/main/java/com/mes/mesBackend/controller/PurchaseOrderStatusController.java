package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.PurchaseOrderStatusResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PurchaseOrderService;
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

// 9-3. 발주현황 조회
@RequestMapping(value = "/purchase-order-statuses")
@Tag(name = "purchase-order-status", description = "발주현황 조회 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class PurchaseOrderStatusController {
    private final PurchaseOrderService purchaseOrderService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PurchaseOrderStatusController.class);
    private CustomLogger cLogger;

    // 발주현황 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간 fromDate~toDate
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "발주현황 리스트 조회",
            description = "검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간 fromDate~toDate"
    )
    public ResponseEntity<List<PurchaseOrderStatusResponse>> getPurchaseOrderStatuses(
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestParam(required = false) @Parameter(description = "담당자 id") Long userId,
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @Parameter(description = "입고창고 id") Long wareHouseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "발주기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "발주기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<PurchaseOrderStatusResponse> purchaseOrders = purchaseOrderService.getPurchaseOrderStatuses(currencyId, userId, clientId, wareHouseId, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseOrderStatuses.");
        return new ResponseEntity<>(purchaseOrders, OK);
    }
}
