package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.LotLogResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.LotLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

// 7-1. LOT 마스터 조회
@RequestMapping(value = "/lot-logs")
@Tag(name = "lot-master", description = "lot log 조회 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class LotLogController {
    private final LotLogService lotLogService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(LotLogController.class);
    private CustomLogger cLogger;

    // Lot log 조회
    // 검색조건: 작업공정 id, 작업지시 id, lotMaster id
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "Lot log 조회",
            description = "검색조건: 작업공정 id, 작업지시 id, lotMaster id"
    )
    public ResponseEntity<List<LotLogResponse>> getLotLogs(
            @RequestParam(required = false) @Parameter(description = "작업공정 id") Long workProcessId,
            @RequestParam(required = false) @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestParam(required = false) @Parameter(description = "lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<LotLogResponse> lotLogResponses =
                lotLogService.getLotLogs(workProcessId, workOrderId, lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getLotLogs.");
        return new ResponseEntity<>(lotLogResponses, OK);
    }
}
