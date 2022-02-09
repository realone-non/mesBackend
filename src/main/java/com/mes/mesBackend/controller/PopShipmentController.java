package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.PopShipmentResponse;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PopShipmentService;
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
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/pop-shipments")
@Tag(name = "pop-shipment", description = "[pop] 출하 API")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class PopShipmentController {
    private final PopShipmentService popShipmentService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PopShipmentController.class);
    private CustomLogger cLogger;

    // 출하 정보 목록 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "[미구현] (pop) 출하정보 목록", description = "검색조건: 검색시작날짜(fromDate) ~ 검색종료날짜(toDate), 거래처 명")
    public ResponseEntity<List<PopShipmentResponse>> getPopShipments(
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하일자 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하일자 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "거래처 명") String clientName,
            @RequestParam(required = false) @Parameter(description = "완료여부(빈값: 전부, true: COMPLETION 만 조회, false: SCHEDULE 만 조회)") Boolean completionYn,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<PopShipmentResponse> responses = popShipmentService.getPopShipments(fromDate, toDate, clientName, completionYn);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopShipments.");
        return new ResponseEntity<>(responses, OK);
    }

    // 출하상태 COMPLETION 으로 변경
    // request: shipmentId
    @PutMapping("/{shipment-id}")
    @ResponseBody
    @Operation(summary = "[미구현] (pop) 출하상태 완료 변경", description = "")
    public ResponseEntity updateShipmentStateCompletion(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        popShipmentService.updateShipmentStateCompletion(shipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from updateShipmentStateCompletion.");
        return new ResponseEntity<>(NO_CONTENT);
    }
}
