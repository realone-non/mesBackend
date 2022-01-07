package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.WorkOrderStateDetailResponse;
import com.mes.mesBackend.dto.response.WorkOrderStateResponse;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkOrderStateService;
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

// 8-1. 작지상태 확인
@RequestMapping("/work-order-states")
@Tag(name = "work-order-state", description = "작지상태 확인 API")
@RestController
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class WorkOrderStateController {
    private final WorkOrderStateService workOrderStateService;
    private final LogService logService;
    private Logger logger = LoggerFactory.getLogger(WorkOrderStateController.class);
    private CustomLogger cLogger;

    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "작업지시 리스트 조회",
            description = "검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호"
    )
    public ResponseEntity<List<WorkOrderStateResponse>> getWorkOrderStates(
            @RequestParam(required = false) @Parameter(description = "작업장 id") Long workProcessId,
            @RequestParam(required = false) @Parameter(description = "작업라인 id") Long workLineId,
            @RequestParam(required = false) @Parameter(description = "제조오더번호") String produceOrderNo,
            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "지시상태") OrderState orderState,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "작업기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "작업기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "수주번호") String contractNo,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<WorkOrderStateResponse> workOrderStates = workOrderStateService.getWorkOrderStates(workProcessId, workLineId, produceOrderNo, itemAccountId, orderState, fromDate, toDate, contractNo);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrderStates.");
        return new ResponseEntity<>(workOrderStates, HttpStatus.OK);
    }

    // 작업지시 단일 조회
    @GetMapping("/{work-order-id}")
    @ResponseBody()
    @Operation(summary = "작업지시 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<WorkOrderStateResponse> getWorkOrderState(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkOrderStateResponse workOrderState = workOrderStateService.getWorkOrderState(workOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workOrderState.getId() + " from getWorkOrderState.");
        return new ResponseEntity<>(workOrderState, HttpStatus.OK);
    }

    // 작업지시 상태 이력 정보 리스트 조회
    @GetMapping("/{work-order-id}/work-order-state-details")
    @ResponseBody
    @Operation(summary = "작업지시 상태 이력 정보 조회")
    public ResponseEntity<WorkOrderStateDetailResponse> getWorkOrderStateDetails(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        WorkOrderStateDetailResponse workOrderStateDetail = workOrderStateService.getWorkOrderStateDetail(workOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrderStateDetails.");
        return new ResponseEntity<>(workOrderStateDetail, HttpStatus.OK);
    }
}
