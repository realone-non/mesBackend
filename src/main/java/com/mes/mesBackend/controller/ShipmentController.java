package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ShipmentItemRequest;
import com.mes.mesBackend.dto.request.ShipmentRequest;
import com.mes.mesBackend.dto.response.ShipmentItemResponse;
import com.mes.mesBackend.dto.response.ShipmentResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ShipmentService;
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

// 4-5. 출하 등록
//@RequestMapping(value = "/shipments")
//@Tag(name = "shipment", description = "출하등록 API")
//@RestController
//@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class ShipmentController {
    private final ShipmentService shipmentService;
    private final LogService logService;

    private Logger logger = LoggerFactory.getLogger(ShipmentController.class);
    private CustomLogger cLogger;

    // ====================================================== 출하 ======================================================
    // 출하 생성
    @Operation(summary = "출하 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ShipmentResponse> createShipment(
            @RequestBody @Valid ShipmentRequest shipmentRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentResponse shipment = shipmentService.createShipment(shipmentRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + shipment.getId() + " from createShipment.");
        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }
    // 출하 단일 조회
    @GetMapping("/{shipment-id}")
    @ResponseBody()
    @Operation(summary = "출하 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ShipmentResponse> getShipment(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentResponse shipment = shipmentService.getShipment(shipmentId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + shipment.getId() + " from getShipment.");
        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }
    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    @GetMapping
    @ResponseBody
    @Operation(summary = "출하 리스트 조회", description = "검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명")
    public ResponseEntity<List<ShipmentResponse>> getShipments(
            @RequestParam(required = false) @Parameter(description = "거래처 명") String clientName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "출하기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "출하기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestParam(required = false) @Parameter(description = "담당자 명") String userName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ShipmentResponse> shipments = shipmentService.getShipments(clientName, fromDate, toDate, currencyId, userName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getShipments.");
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }
    // 출하 수정
    @PatchMapping("/{shipment-id}")
    @ResponseBody()
    @Operation(summary = "출하 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ShipmentResponse> updateShipment(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestBody @Valid ShipmentRequest shipmentRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentResponse shipment = shipmentService.updateShipment(shipmentId, shipmentRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + shipment.getId() + " from updateShipment.");
        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }
    // 출하 삭제
    @DeleteMapping("/{shipment-id}")
    @ResponseBody()
    @Operation(summary = "출하 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteShipment(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        shipmentService.deleteShipment(shipmentId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + shipmentId + " from deleteShipment.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // =================================================== 출하 품목 ====================================================
    // 출하 품목정보 생성
    @Operation(summary = "출하 품목 생성", description = "")
    @PostMapping("/{shipment-id}/shipment-items")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ShipmentItemResponse> createShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestBody @Valid ShipmentItemRequest shipmentItemRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentItemResponse shipmentItem = shipmentService.createShipmentItem(shipmentId, shipmentItemRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + shipmentItem + " from createShipmentItem.");
        return new ResponseEntity<>(shipmentItem, HttpStatus.OK);
    }
    // 출하 품목정보 단일 조회
    @GetMapping("/{shipment-id}/shipment-items/{shipment-item-id}")
    @ResponseBody
    @Operation(summary = "출하 품목 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ShipmentItemResponse> getShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목 id") Long shipmentItemId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentItemResponse shipmentItem = shipmentService.getShipmentItem(shipmentId, shipmentItemId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + shipmentItem + " from getShipmentItem.");
        return new ResponseEntity<>(shipmentItem, HttpStatus.OK);
    }

    // 출하 품목 정보 전체조회
    @GetMapping("/{shipment-id}/shipment-items")
    @ResponseBody
    @Operation(summary = "출하 품목 전체조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<ShipmentItemResponse>> getShipmentItems(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ShipmentItemResponse> shipmentItems = shipmentService.getShipmentItems(shipmentId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of shipmentId: " + shipmentId + " from getShipmentItems.");
        return new ResponseEntity<>(shipmentItems, HttpStatus.OK);
    }

    // 출하 품목정보 수정
    @PatchMapping("/{shipment-id}/shipment-items/{shipment-item-id}")
    @ResponseBody()
    @Operation(summary = "출하 품목 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ShipmentItemResponse> updateShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목 id") Long shipmentItemId,
            @RequestBody @Valid ShipmentItemRequest shipmentItemRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentItemResponse shipmentItem = shipmentService.updateShipmentItem(shipmentId, shipmentItemId, shipmentItemRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + shipmentItem + " from updateShipmentItem.");
        return new ResponseEntity<>(shipmentItem, HttpStatus.OK);
    }

    // 출하 품목정보 삭제
    @DeleteMapping("/{shipment-id}/shipment-items/{shipment-item-id}")
    @ResponseBody()
    @Operation(summary = "출하 품목 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목 id") Long shipmentItemId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        shipmentService.deleteShipmentItem(shipmentId, shipmentItemId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + shipmentId + " from deleteShipmentItem.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // ==================================================== Invoice ====================================================
    // Invoice 생성
    // Invoice 조회
    // Invoice 수정
    // Invoice 삭제

    // ==================================================== LOT 정보 ====================================================
    // Lot 정보
}
