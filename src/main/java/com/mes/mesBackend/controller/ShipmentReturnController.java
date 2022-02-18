package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ShipmentReturnRequest;
import com.mes.mesBackend.dto.response.ShipmentReturnLotResponse;
import com.mes.mesBackend.dto.response.ShipmentReturnResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ShipmentReturnService;
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

// 4-6. 출하반품 등록
@RequestMapping(value = "/shipment-returns")
@Tag(name = "shipment-return", description = "4-6. 출하반품등록 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class ShipmentReturnController {
    private final ShipmentReturnService shipmentReturnService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ShipmentReturnController.class);
    private CustomLogger cLogger;


    // 출하반품 생성
    @Operation(summary = "출하반품 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ShipmentReturnResponse> createShipmentReturn(
            @RequestBody @Valid ShipmentReturnRequest shipmentReturnRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ShipmentReturnResponse shipmentReturn = shipmentReturnService.createShipmentReturn(shipmentReturnRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + shipmentReturn.getId() + " from createShipmentReturn.");
        return new ResponseEntity<>(shipmentReturn, OK);
    }

    // 출하반품 리스트 검색 조회, 검색조건: 거래처 id, 품번|품명, 반품기간 fromDate~toDate
    @GetMapping
    @ResponseBody
    @Operation(summary = "출하반품 리스트 검색 조회", description = "검색조건: 거래처 id, 품번|품명, 반품기간 fromDate~toDate")
    public ResponseEntity<List<ShipmentReturnResponse>> getShipmentReturns(
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "반품기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "반품기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ShipmentReturnResponse> shipmentReturns = shipmentReturnService.getShipmentReturns(clientId, itemNoAndItemName, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getShipmentReturns.");
        return new ResponseEntity<>(shipmentReturns, OK);
    }

    // 출하반품 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "출하반품 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ShipmentReturnResponse> updateShipmentReturn(
            @PathVariable @Parameter(description = "출하 반품 id") Long id,
            @RequestBody @Valid ShipmentReturnRequest shipmentReturnRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ShipmentReturnResponse shipmentReturn = shipmentReturnService.updateShipmentReturn(id, shipmentReturnRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + shipmentReturn.getId() + " from updateShipmentReturn.");
        return new ResponseEntity<>(shipmentReturn, OK);
    }

    // 출하반품 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "출하반품 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteShipmentReturn(
            @PathVariable @Parameter(description = "출하 반품 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        shipmentReturnService.deleteShipmentReturn(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteShipmentReturn.");
        return new ResponseEntity(NO_CONTENT);
    }

    // clientId 로 shipmentIds 조회
    @GetMapping("/shipment-lots")
    @ResponseBody
    @Operation(summary = "거래처로 shipmentLotId 조회", description = "검색조건: 거래처 id")
    public ResponseEntity<List<ShipmentReturnLotResponse>> getShipmentLots(
            @RequestParam @Parameter(description = "거래처 id") Long clientId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ShipmentReturnLotResponse> shipmentReturnLotResponses = shipmentReturnService.getShipmentLots(clientId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getShipmentLots.");
        return new ResponseEntity<>(shipmentReturnLotResponses, OK);
    }

    // 조건: lotNo
    // 단건조회
}
