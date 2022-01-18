package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.InputTestScheduleResponse;
import com.mes.mesBackend.entity.enumeration.TestType;
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

// 15-4. 검사대기 현황
@RequestMapping(value = "/outsourcing-input-test-schedules")
@Tag(name = "outsourcing-input-test-schedule", description = "15-4. 검사대기 현황 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class OutsourcingInputTestScheduleController {
    private final InputTestPerformanceService inputTestPerformanceService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(OutsourcingInputTestScheduleController.class);
    private CustomLogger cLogger;

    // 검사대기 현황 조회
    // 검색조건: 검사창고 id, 검사유형, 품명|품번, 거래처, 검사기간 fromDate~toDate
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "검사대기 현황 조회",
            description = "검색조건: 검사창고 id, 검사유형, 품명|품번, 거래처, 검사요청기간 fromDate~toDate")
    public ResponseEntity<List<InputTestScheduleResponse>> getOutsourcingInputTestSchedules(
            @RequestParam(required = false) @Parameter(description = "검사창고 id") Long wareHouseId,
            @RequestParam(required = false) @Parameter(description = "검사유형") TestType testType,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoAndName,
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "검사요청기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "검사요청기간 toDate") LocalDate toDate,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<InputTestScheduleResponse> inputTestScheduleResponses = inputTestPerformanceService.getInputTestSchedules(wareHouseId, testType, itemNoAndName, clientId, fromDate, toDate, false);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getOutsourcingInputTestSchedules.");
        return new ResponseEntity<>(inputTestScheduleResponses, HttpStatus.OK);
    }
}
