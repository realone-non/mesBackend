package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ProduceOrderRequest;
import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.dto.response.ProduceOrderResponse;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ProduceOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

// 6-1. 제조오더 등록
@RequestMapping(value = "/produce-orders")
@Tag(name = "produce-order", description = "제조오더 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class ProduceOrderController {

    private final ProduceOrderService produceOrderService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ProduceOrderController.class);
    private CustomLogger cLogger;

    // 제조 오더 생성
    @Operation(summary = "제조 오더 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ProduceOrderResponse> createProduceOrder(
            @RequestBody @Valid ProduceOrderRequest produceOrderRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ProduceOrderResponse produceOrder = produceOrderService.createProduceOrder(produceOrderRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + produceOrder.getId() + " from createProduceOrder.");
        return new ResponseEntity<>(produceOrder, HttpStatus.OK);
    }

    // 제조 오더 단일 조회
    @GetMapping("/{produce-order-id}")
    @ResponseBody()
    @Operation(summary = "제조 오더 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ProduceOrderResponse> getProduceOrder(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조 오더 id") Long produceOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ProduceOrderResponse produceOrder = produceOrderService.getProduceOrder(produceOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + produceOrder.getId() + " from getProduceOrder.");
        return new ResponseEntity<>(produceOrder, HttpStatus.OK);
    }

    // 제조 오더 리스트 조회, 검색조건 : 품목그룹 id, 품명|품번, 지시상태, 제조오더번호, 수주번호, 착수예정일 fromDate~toDate, 자재납기일자(보류)
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "제조 오더 리스트 조회",
            description = "검색조건: 품목그룹 id, 품명|품번, 지시상태, 제조오더번호, 수주번호, 착수예정일 fromDate~toDate, 자재납기일자(보류)"
    )
    public ResponseEntity<List<ProduceOrderResponse>> getProduceOrders(
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoAndName,
            @RequestParam(required = false) @Parameter(description = "지시상태") InstructionStatus instructionStatus,
            @RequestParam(required = false) @Parameter(description = "제조오더번호") String produceOrderNo,
            @RequestParam(required = false) @Parameter(description = "수주번호") String contractNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "착수예정일 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "착수예정일 toDate") LocalDate toDate,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ProduceOrderResponse> produceOrders = produceOrderService.getProduceOrders(itemGroupId, itemNoAndName, instructionStatus, produceOrderNo, contractNo, fromDate, toDate);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProduceOrders.");
        return new ResponseEntity<>(produceOrders, HttpStatus.OK);
    }

    // 제조 오더 수정
    @PatchMapping("/{produce-order-id}")
    @ResponseBody()
    @Operation(summary = "제조 오더 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ProduceOrderResponse> updateProduceOrder(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조 오더 id") Long produceOrderId,
            @RequestBody @Valid ProduceOrderRequest produceOrderRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ProduceOrderResponse produceOrder = produceOrderService.updateProduceOrder(produceOrderId, produceOrderRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + produceOrder.getId() + " from updateProduceOrder.");
        return new ResponseEntity<>(produceOrder, HttpStatus.OK);
    }

    // 제조 오더 삭제
    @DeleteMapping("/{produce-order-id}")
    @ResponseBody()
    @Operation(summary = "제조 오더 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteProduceOrder(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조 오더 id") Long produceOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        produceOrderService.deleteProduceOrder(produceOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + produceOrderId + " from deleteProduceOrder.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 제조 오더 디테일 리스트 조회
    @GetMapping("/{produce-order-id}/produce-order-details")
    @ResponseBody
    @Operation(summary = "제조 오더 디테일 리스트 조회")
    public ResponseEntity<List<ProduceOrderDetailResponse>> getProduceOrderDetails(
            @PathVariable(value = "produce-order-id") @Parameter(description = "제조 오더 id") Long produceOrderId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ProduceOrderDetailResponse> produceOrderDetails = produceOrderService.getProduceOrderDetails(produceOrderId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProduceOrderDetails.");
        return new ResponseEntity<>(produceOrderDetails, HttpStatus.OK);
    }
}