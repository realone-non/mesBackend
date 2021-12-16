package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkOrderRequest;
import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.dto.response.WorkOrderResponse;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkOrderService;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

// 6-2. 작업지시 등록
@RequestMapping
@Tag(name = "work-order", description = "작업지시 등록 API")
@RestController
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class WorkOrderController {
    private final WorkOrderService workOrderService;
    private final LogService logService;

    private Logger logger = LoggerFactory.getLogger(WorkOrderController.class);
    private CustomLogger cLogger;

    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 착수예정일 fromDate~endDate, 지시상태
    @GetMapping("/work-order-produce-orders")
    @ResponseBody
    @Operation(
            summary = "제조오더 정보 리스트 조회",
            description = "검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 착수예정일 fromDate~endDate, 지시상태"
    )
    public ResponseEntity<List<WorkOrderProduceOrderResponse>> getProduceOrders(
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoAndName,
            @RequestParam(required = false) @Parameter(description = "수주번호") String contractNo,
            @RequestParam(required = false) @Parameter(description = "제조오더번호") String produceOrderNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "착수예정일 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "착수예정일 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "지시상태") InstructionStatus instructionStatus,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<WorkOrderProduceOrderResponse> produceOrders = workOrderService.getProduceOrders(itemGroupId, itemNoAndName, contractNo, produceOrderNo, fromDate, toDate, instructionStatus);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProduceOrders.");
        return new ResponseEntity<>(produceOrders, HttpStatus.OK);
    }

    // 작업지시 생성
    @Operation(summary = "작업 지시 생성", description = "")
    @PostMapping("/produce-orders/{produce-order-id}/work-orders")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조오더 id") Long produceOrderId,
            @RequestBody @Valid WorkOrderRequest workOrderRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkOrderResponse workOrder = workOrderService.createWorkOrder(produceOrderId, workOrderRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + workOrder.getId() + " from createWorkOrder.");
        return new ResponseEntity<>(workOrder, HttpStatus.OK);
    }

    // 작업지시 단일조회
    @GetMapping("/produce-orders/{produce-order-id}/work-orders/{work-order-id}")
    @ResponseBody()
    @Operation(summary = "작업지시 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<WorkOrderResponse> getWorkOrder(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조오더 id") Long produceOrderId,
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkOrderResponse workOrder = workOrderService.getWorkOrder(produceOrderId, workOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workOrder.getId() + " from getWorkOrder.");
        return new ResponseEntity<>(workOrder, HttpStatus.OK);
    }

    // 작업지시 리스트 조회
    @GetMapping("/produce-orders/{produce-order-id}/work-orders")
    @ResponseBody
    @Operation(summary = "작업지시 리스트 조회", description = "")
    public ResponseEntity<List<WorkOrderResponse>> getWorkOrders(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조오더 id") Long produceOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<WorkOrderResponse> workOrders = workOrderService.getWorkOrders(produceOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrders.");
        return new ResponseEntity<>(workOrders, HttpStatus.OK);
    }

    // 작업지시 수정
    @PatchMapping("/produce-orders/{produce-order-id}/work-orders/{work-order-id}")
    @ResponseBody()
    @Operation(summary = "작업지시 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkOrderResponse> updateWorkOrder(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조오더 id") Long produceOrderId,
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestBody @Valid WorkOrderRequest workOrderRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkOrderResponse workOrder = workOrderService.updateWorkOrder(produceOrderId, workOrderId, workOrderRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + workOrder.getId() + " from updateWorkOrder.");
        return new ResponseEntity<>(workOrder, HttpStatus.OK);
    }

    // 작업지시 삭제
    @DeleteMapping("/produce-orders/{produce-order-id}/work-orders/{work-order-id}")
    @ResponseBody()
    @Operation(summary = "작업지시 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteWorkOrder(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조오더 id") Long produceOrderId,
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        workOrderService.deleteWorkOrder(produceOrderId, workOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + workOrderId + " from deleteWorkOrder.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
