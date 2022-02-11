package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.ShipmentStatusResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ShipmentService;
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

// 4-7. 출하 현황
@RequestMapping(value = "/shipment-statuses")
@Tag(name = "shipment-statuses", description = "4-7. 출하 현황 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class ShipmentStatusController {
    private final ShipmentService shipmentService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ShipmentStatusController.class);
    private CustomLogger cLogger;

    // 출하현황 검색 리스트 조회, 검색조건: 거래처 id, 출하기간 fromDate~toDate, 화폐 id, 담당자 id, 품번|품명
    @GetMapping
    @ResponseBody
    @Operation(summary = "출하현황 검색 리스트 조회", description = "검색조건: 거래처 id, 출하기간 fromDate~toDate, 화폐 id, 담당자 id, 품번|품명")
    public ResponseEntity<List<ShipmentStatusResponse>> getShipmentStatuses(
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestParam(required = false) @Parameter(description = "담당자 id") Long userId,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ShipmentStatusResponse> shipmentStatuses = shipmentService.getShipmentStatuses(clientId, fromDate, toDate, currencyId, userId, itemNoAndItemName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getShipmentStatuses.");
        return new ResponseEntity<>(shipmentStatuses, OK);
    }
}
