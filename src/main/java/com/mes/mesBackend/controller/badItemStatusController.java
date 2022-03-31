package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.WorkOrderBadItemStatusDetailResponse;
import com.mes.mesBackend.dto.response.WorkOrderBadItemStatusResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.BadItemEnrollmentService;
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

// 불량조회
@RequestMapping("/work-order-bad-items")
@Tag(name = "bad-item-status", description = "작업지시 불량현황")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class badItemStatusController {
    private final BadItemEnrollmentService badItemEnrollmentService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(BadItemEnrollmentController.class);
    private CustomLogger cLogger;

    // 작업지시조회
    @Operation(
            summary = "작업지시 불량률 정보 리스트 조회(지시상태 완료, 진행중만 조회)",
            description = "현재 완료, 진행중인 작업지시만 조회, 검색조건: 공정 id, 작업지시 번호, 품번|품명, 작업자 id, 작업기간 fromDate~toDate"
    )
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<WorkOrderBadItemStatusResponse>> getWorkOrderBadItems(
            @RequestParam(required = false) @Parameter(description = "작업공정 id") Long workProcessId,
            @RequestParam(required = false) @Parameter(description = "JOB NO(작업지시번호)") String workOrderNo,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
            @RequestParam(required = false) @Parameter(description = "작업자 id") Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간(작업시작) fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간(작업시작) toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<WorkOrderBadItemStatusResponse> responses = badItemEnrollmentService.getWorkOrderBadItems(workProcessId, workOrderNo, itemNoAndItemName, userId, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrderBadItems.");
        return new ResponseEntity<>(responses, OK);
    }

    // 작업지시 상세 불량률 조회
    @Operation(summary = "작업지시 상세 불량률 조회", description = "")
    @GetMapping("/{work-order-id}/detail-status")
    @ResponseBody
    public ResponseEntity<List<WorkOrderBadItemStatusDetailResponse>> getWorkOrderBadItemDetails(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<WorkOrderBadItemStatusDetailResponse> responses = badItemEnrollmentService.getWorkOrderBadItemDetails(workOrderId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrderBadItemDetails.");
        return new ResponseEntity<>(responses, OK);
    }
}
