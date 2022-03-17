package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.PurchaseOrderDetailRequest;
import com.mes.mesBackend.dto.request.PurchaseOrderRequest;
import com.mes.mesBackend.dto.response.PurchaseOrderDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseOrderResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PurchaseOrderService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 9-2. 구매발주 등록
@RequestMapping(value = "/purchase-orders")
@Tag(name = "purchase-order", description = "9-2. 구매발주 등록 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);
    private CustomLogger cLogger;

    // 구매발주 생성
    @Operation(summary = "구매발주 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(
            @RequestBody @Valid PurchaseOrderRequest purchaseOrderRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + purchaseOrder.getId() + " from createPurchaseOrder.");
        return new ResponseEntity<>(purchaseOrder, OK);
    }
    // 구매발주 단일 조회
    @GetMapping("/{purchase-order-id}")
    @ResponseBody()
    @Operation(summary = "구매발주 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<PurchaseOrderResponse> getPurchaseOrder(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.getPurchaseOrderResponseOrThrow(purchaseOrderId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + purchaseOrder.getId() + " from getPurchaseOrder.");
        return new ResponseEntity<>(purchaseOrder, OK);
    }
    // 구매발주 리스트 검색 조회, 검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간, 완료포함(check)
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "구매발주 전체 조회",
            description = "검색조건: 화폐 id, 담당자 id, 거래처 id, 입고창고 id, 발주기간, 완료포함(check)"
    )
    public ResponseEntity<List<PurchaseOrderResponse>> getPurchaseOrders(
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestParam(required = false) @Parameter(description = "담당자 id") Long userId,
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @Parameter(description = "입고창고 id") Long wareHouseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "발주기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "발주기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "완료포함 여부") boolean orderCompletion,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<PurchaseOrderResponse> purchaseOrders = purchaseOrderService.getPurchaseOrders(currencyId, userId, clientId, wareHouseId, fromDate, toDate, orderCompletion);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseOrders.");
        return new ResponseEntity<>(purchaseOrders, OK);
    }
    // 구매발주 수정
    @PatchMapping("/{purchase-order-id}")
    @ResponseBody()
    @Operation(summary = "구매발주 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseOrderResponse> updatePurchaseOrder(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @RequestBody @Valid PurchaseOrderRequest purchaseOrderRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.updatePurchaseOrder(purchaseOrderId, purchaseOrderRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + purchaseOrder.getId() + " from updatePurchaseOrder.");
        return new ResponseEntity<>(purchaseOrder, OK);
    }

    // 구매발주 삭제
    @DeleteMapping("/{purchase-order-id}")
    @ResponseBody()
    @Operation(summary = "구매발주 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deletePurchaseOrder(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        purchaseOrderService.deletePurchaseOrder(purchaseOrderId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + purchaseOrderId + " from deletePurchaseOrder.");
        return new ResponseEntity(NO_CONTENT);
    }
    
    // ================================================================ 구매발주상세 ================================================================
    // 구매발주상세 생성
    @Operation(
            summary = "구매발주상세 생성",
            description = "구매발주에 해당하는 상세 내역이 존재할 땐 기존 상세내역의 거래처 기준으로 같은 구매요청만 추가 할 수 있다."
    )
    @PostMapping("/{purchase-order-id}/purchase-order-details")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseOrderDetailResponse> createPurchaseOrderDetail(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @RequestBody @Valid PurchaseOrderDetailRequest purchaseOrderDetailRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws BadRequestException, NotFoundException {
        PurchaseOrderDetailResponse purchaseOrderDetail = purchaseOrderService.createPurchaseOrderDetail(purchaseOrderId, purchaseOrderDetailRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + purchaseOrderDetail.getId() + " from createPurchaseOrderDetail.");
        return new ResponseEntity<>(purchaseOrderDetail, OK);
    }

    // 구매발주상세 단일 조회
    @GetMapping("/{purchase-order-id}/purchase-order-details/{purchase-order-detail-id}")
    @ResponseBody()
    @Operation(summary = "구매발주상세 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<PurchaseOrderDetailResponse> getPurchaseOrderDetail(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @PathVariable(value = "purchase-order-detail-id") @Parameter(description = "구매발주상세(구매요청) id") Long purchaseOrderDetailId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        PurchaseOrderDetailResponse purchaseOrderDetail = purchaseOrderService.getPurchaseOrderDetailResponse(purchaseOrderId, purchaseOrderDetailId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + purchaseOrderDetail.getId() + " from getPurchaseOrderDetail.");
        return new ResponseEntity<>(purchaseOrderDetail, OK);
    }

    // 구매발주상세 전체 조회
    @GetMapping("/{purchase-order-id}/purchase-order-details")
    @ResponseBody
    @Operation(summary = "구매발주상세 전체 조회", description = "")
    public ResponseEntity<List<PurchaseOrderDetailResponse>> getPurchaseOrderDetails(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PurchaseOrderDetailResponse> purchaseOrderDetails = purchaseOrderService.getPurchaseOrderDetails(purchaseOrderId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseOrderDetails.");
        return new ResponseEntity<>(purchaseOrderDetails, OK);
    }

    // 구매발주상세 수정
    @PatchMapping("/{purchase-order-id}/purchase-order-details/{purchase-order-detail-id}")
    @ResponseBody()
    @Operation(summary = "구매발주상세 수정", description = "비고 필드만 수정 할 수 있음.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseOrderDetailResponse> updatePurchaseOrderDetail(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @PathVariable(value = "purchase-order-detail-id") @Parameter(description = "구매발주상세(구매요청) id") Long purchaseOrderDetailId,
            @RequestParam(required = false) @Parameter(description = "비고") String note,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        PurchaseOrderDetailResponse purchaseOrderDetail = purchaseOrderService.updatePurchaseOrderDetail(purchaseOrderId, purchaseOrderDetailId, note);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + purchaseOrderDetail.getId() + " from updatePurchaseOrderDetail.");
        return new ResponseEntity<>(purchaseOrderDetail, OK);
    }

    // 구매발주상세 삭제
    @DeleteMapping("/{purchase-order-id}/purchase-order-details/{purchase-order-detail-id}")
    @ResponseBody()
    @Operation(summary = "구매발주상세 삭제", description = "해당 구매발주에서 제거")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deletePurchaseOrderDetail(
            @PathVariable(value = "purchase-order-id") @Parameter(description = "구매발주 id") Long purchaseOrderId,
            @PathVariable(value = "purchase-order-detail-id") @Parameter(description = "구매발주상세(구매요청) id") Long purchaseOrderDetailId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        purchaseOrderService.deletePurchaseOrderDetail(purchaseOrderId, purchaseOrderDetailId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + purchaseOrderDetailId + " from deletePurchaseOrderDetail.");
        return new ResponseEntity(NO_CONTENT);
    }
}
