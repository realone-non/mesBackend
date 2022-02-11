package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ShipmentCreateRequest;
import com.mes.mesBackend.dto.request.ShipmentUpdateRequest;
import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.dto.response.ShipmentItemResponse;
import com.mes.mesBackend.dto.response.ShipmentLotInfoResponse;
import com.mes.mesBackend.dto.response.ShipmentResponse;
import com.mes.mesBackend.exception.BadRequestException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

// 4-5. 출하 등록
@RequestMapping(value = "/shipments")
@Tag(name = "shipment", description = "4-5. 출하등록 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class ShipmentController {
    private final ShipmentService shipmentService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ShipmentController.class);
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
            @RequestBody @Valid ShipmentCreateRequest shipmentCreateRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentResponse shipment = shipmentService.createShipment(shipmentCreateRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + shipment.getId() + " from createShipment.");
        return new ResponseEntity<>(shipment, OK);
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
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentResponse shipment = shipmentService.getShipmentResponse(shipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + shipment.getId() + " from getShipment.");
        return new ResponseEntity<>(shipment, OK);
    }

    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    @GetMapping
    @ResponseBody
    @Operation(summary = "출하 리스트 조회", description = "검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명")
    public ResponseEntity<List<ShipmentResponse>> getShipments(
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestParam(required = false) @Parameter(description = "담당자 id") Long userId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ShipmentResponse> shipments = shipmentService.getShipments(clientId, fromDate, toDate, currencyId, userId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getShipments.");
        return new ResponseEntity<>(shipments, OK);
    }
    // 출하 수정
    @PatchMapping("/{shipment-id}")
    @ResponseBody
    @Operation(
            summary = "출하 수정",
            description = "수정 안되는 이유: <br/>" +
                    "출하에 해당되는 출하 품목이 존재할 경우. <br/> " +
                    "출하가 완료될 경우 수정."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ShipmentResponse> updateShipment(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestBody @Valid ShipmentUpdateRequest shipmentUpdateRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ShipmentResponse shipment = shipmentService.updateShipment(shipmentId, shipmentUpdateRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + shipment.getId() + " from updateShipment.");
        return new ResponseEntity<>(shipment, OK);
    }
    // 출하 삭제
    @DeleteMapping("/{shipment-id}")
    @ResponseBody()
    @Operation(
            summary = "출하 삭제",
            description = "삭제 안되는 이유: " +
                    "출하에 해당되는 출하 품목이 존재할 경우. <br/>" +
                    "출하가 완료될 경우."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteShipment(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        shipmentService.deleteShipment(shipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + shipmentId + " from deleteShipment.");
        return new ResponseEntity(NO_CONTENT);
    }

    // =================================================== 출하 품목 ====================================================

    // 출하 가능 품복 정보 조회
    @GetMapping("/{shipment-id}/possible-shipment-items")
    @ResponseBody
    @Operation(summary = "출하 가능 품목 전체조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<ShipmentItemResponse>> getPossibleShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ShipmentItemResponse> shipmentItem = shipmentService.getPossibleShipmentItem(shipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of shipmentId: " + shipmentId + " from getShipmentItem.");
        return new ResponseEntity<>(shipmentItem, OK);
    }

    // 출하 품목정보 생성
    @Operation(
            summary = "출하 품목 생성",
            description = "생성 안되는 경우: " +
                    "수주품목은 해당 출하에 중복등록 불가능. <br/>" +
                    "출하에 등록 된 거래처와 입력할 수주품목에 해당하는 수주의 고객사가 다르면 등록 불가능. <br/>" +
                    "출하가 완료될 경우 생성 불가능."
    )
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
            @RequestParam @Valid @Parameter(description = "수주품목 id") Long contractItemId,
            @RequestParam(required = false) @Parameter(description = "비고") String note,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ShipmentItemResponse shipmentItem = shipmentService.createShipmentItem(shipmentId, contractItemId, note);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + shipmentItem + " from createShipmentItem.");
        return new ResponseEntity<>(shipmentItem, OK);
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
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ShipmentItemResponse shipmentItem = shipmentService.getShipmentItemResponse(shipmentId, shipmentItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + shipmentItem + " from getShipmentItem.");
        return new ResponseEntity<>(shipmentItem, OK);
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
    public ResponseEntity<List<ShipmentItemResponse>> getShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ShipmentItemResponse> shipmentItem = shipmentService.getShipmentItem(shipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of shipmentId: " + shipmentId + " from getShipmentItem.");
        return new ResponseEntity<>(shipmentItem, OK);
    }

    // 출하 품목정보 수정
    @PatchMapping("/{shipment-id}/shipment-items/{shipment-item-id}")
    @ResponseBody
    @Operation(
            summary = "출하 품목 수정",
            description = "수정 안되는 이유: <br/> " +
                    "해당하는 출하 품목정보에 대한 LOT 정보가 존재하면 수정 불가능(LOT 정보 삭제 후 다시 수정). <br/> " +
                    "출하가 완료되었으면 수정 불가능. <br/> " +
                    "수주품목은 해당 출하에 중복등록 불가능."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ShipmentItemResponse> updateShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목정보 id") Long shipmentItemId,
            @RequestParam @Valid @Parameter(description = "수주품목 id") Long contractItemId,
            @RequestParam @Parameter(description = "비고") String note,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ShipmentItemResponse shipmentItem = shipmentService.updateShipmentItem(shipmentId, shipmentItemId, contractItemId, note);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + shipmentItem + " from updateShipmentItem.");
        return new ResponseEntity<>(shipmentItem, OK);
    }

    // 출하 품목정보 삭제
    @DeleteMapping("/{shipment-id}/shipment-items/{shipment-item-id}")
    @ResponseBody
    @Operation(
            summary = "출하 품목 삭제",
            description = "삭제 안되는 이유: <br/>" +
                    "해당하는 출하 품목정보에 대한 LOT 정보가 존재하면 수정 불가능(LOT 정보 삭제 후 다시 수정). <br/>" +
                    "출하가 완료되었으면 수정 불가능."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteShipmentItem(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목 id") Long shipmentItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        shipmentService.deleteShipmentItem(shipmentId, shipmentItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + shipmentId + " from deleteShipmentItem.");
        return new ResponseEntity(NO_CONTENT);
    }


    // ==================================================== LOT 정보 ====================================================
    // LOT 정보 생성
    @Operation(
            summary = "LOT 정보 생성",
            description = "출하가 완료되었으면 생성 불가능. <br/> " +
                    "입력한 lotMasterId 가 출하품목의 품목과 lotMaster 의 품목과 같으며 포장공정이 완료 되고 현재 재고가 0 이상인 lotMaster 가 아닐 경우. <br/>"
    )
    @PostMapping("/{shipment-id}/shipment-items/{shipment-item-id}/shipment-lots")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ShipmentLotInfoResponse> createShipmentLot(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목 id") Long shipmentItemId,
            @RequestParam @Parameter(description = "출하 품목정보의 품목과 lotMaster 의 품목이랑 같으며 포장공정 끝나고 현재 재고가 0 이 아닌 lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ShipmentLotInfoResponse shipmentLot = shipmentService.createShipmentLot(shipmentId, shipmentItemId, lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + shipmentLot + " from createShipmentLot.");
        return new ResponseEntity<>(shipmentLot, OK);
    }

    // LOT 정보 전체 조회
    @GetMapping("/{shipment-id}/shipment-items/{shipment-item-id}/shipment-lots")
    @ResponseBody
    @Operation(summary = "LOT 정보 전체 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<ShipmentLotInfoResponse>> getShipmentLots(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목 id") Long shipmentItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ShipmentLotInfoResponse> shipmentLots = shipmentService.getShipmentLots(shipmentId, shipmentItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of shipmentId: " + shipmentItemId + " from getShipmentLots.");
        return new ResponseEntity<>(shipmentLots, OK);
    }

    // 출하 LOT 정보 삭제
    @DeleteMapping("/{shipment-id}/shipment-items/{shipment-item-id}/shipment-lots/{shipment-lot-id}")
    @ResponseBody
    @Operation(
            summary = "출하 LOT 정보 삭제",
            description = "출하가 완료되었으면 삭제 불가능."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteShipmentLot(
            @PathVariable(value = "shipment-id") @Parameter(description = "출하 id") Long shipmentId,
            @PathVariable(value = "shipment-item-id") @Parameter(description = "출하 품목 id") Long shipmentItemId,
            @PathVariable(value = "shipment-lot-id") @Parameter(description = "출하 lot 정보 id") Long shipmentLotId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        shipmentService.deleteShipmentLot(shipmentId, shipmentItemId, shipmentLotId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + shipmentLotId + " from deleteShipmentLot.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 출하 LOT 정보 생성 시 LOT 정보 조회 API
    // 출하 품목정보의 품목과 lotMaster 의 품목이랑 같으며 포장공정 끝나고 현재 재고가 0 이 아닌 lotMaster id
    @GetMapping("/lot-masters")
    @ResponseBody
    @Operation(
            summary = "출하 LOT 정보 생성 시 조건에 맞는 LOT 조회",
            description = "출하 품목정보의 품목과 lotMaster 의 품목이랑 같으며 포장공정 끝나고 현재 재고가 0 이 아니면서, 재고수량이 수주미출하수량 보다 같거나 적은 lotMaster id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<LotMasterResponse.idAndLotNo>> getShipmentLotMasters(
            @RequestParam @Parameter(description = "수주품목 id") Long contractItemId,
            @RequestParam @Parameter(description = "수주미출하수량") int notShippedAmount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<LotMasterResponse.idAndLotNo> lotMasters = shipmentService.getShipmentLotMasters(contractItemId, notShippedAmount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getShipmentLotMasters.");
        return new ResponseEntity<>(lotMasters, OK);
    }

    // ==================================================== Invoice ====================================================
    // Invoice 생성
    // Invoice 조회
    // Invoice 수정
    // Invoice 삭제

}
