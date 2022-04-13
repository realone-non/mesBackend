package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.PurchaseInputReturnCreateRequest;
import com.mes.mesBackend.dto.request.PurchaseInputReturnUpdateRequest;
import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.dto.response.PurchaseInputReturnResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PurchaseInputReturnService;
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

// 9-6. 구매입고 반품 등록
@Tag(name = "purchase-input-return", description = "9-6. 구매입고 반품 등록 API")
@RequestMapping("/purchase-input-returns")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class PurchaseInputReturnController {
    private final PurchaseInputReturnService purchaseInputReturnService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PurchaseInputReturnController.class);
    private CustomLogger cLogger;

    // 구매입고반품 생성
    @Operation(summary = "구매입고반품 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseInputReturnResponse> createPurchaseInputReturn(
            @RequestBody @Valid PurchaseInputReturnCreateRequest purchaseInputReturnRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        PurchaseInputReturnResponse purchaseInputReturnResponse = purchaseInputReturnService.createPurchaseInputReturn(purchaseInputReturnRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + purchaseInputReturnResponse.getId() + " from createPurchaseInputReturn.");
        return new ResponseEntity<>(purchaseInputReturnResponse, OK);
    }

    // 구매입고반품 리스트 검색 조회, 검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate
    @Operation(summary = "구매입고반품 리스트 조회", description = "검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<PurchaseInputReturnResponse>> getPurchaseInputReturns(
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoOrItemName,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "반품기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "반품기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<PurchaseInputReturnResponse> purchaseInputReturnResponses = purchaseInputReturnService.getPurchaseInputReturns(clientId, itemNoOrItemName, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseInputReturns.");
        return new ResponseEntity<>(purchaseInputReturnResponses, OK);
    }

    // 구매입고반품 단일조회
    @Operation(summary = "구매입고반품 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<PurchaseInputReturnResponse> getPurchaseInputReturn(
            @PathVariable(value = "id") @Parameter(description = "구매입고반품 id") Long purchaseInputReturnId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PurchaseInputReturnResponse purchaseInputReturnResponse = purchaseInputReturnService.getPurchaseInputReturnResponse(purchaseInputReturnId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + purchaseInputReturnResponse.getId() + " from getPurchaseInputReturn.");
        return new ResponseEntity<>(purchaseInputReturnResponse, OK);
    }

    // 구매입고반품 수정
    @Operation(summary = "구매입고반품 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseInputReturnResponse> updatePurchaseInputReturn(
            @PathVariable(value = "id") @Parameter(description = "구매입고반품 id") Long purchaseInputReturnId,
            @RequestBody @Valid PurchaseInputReturnUpdateRequest purchaseInputReturnUpdateRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        PurchaseInputReturnResponse purchaseInputReturnResponse = purchaseInputReturnService.updatePurchaseInputReturn(purchaseInputReturnId, purchaseInputReturnUpdateRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + purchaseInputReturnResponse.getId() + " from updatePurchaseInputReturn.");
        return new ResponseEntity<>(purchaseInputReturnResponse, OK);
    }

    // 구매입고반품 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "구매입고반품 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deletePurchaseInputReturn(
            @PathVariable(value = "id") @Parameter(description = "구매입고반품 id") Long purchaseInputReturnId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        purchaseInputReturnService.deletePurchaseInputReturn(purchaseInputReturnId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + purchaseInputReturnId + " from deletePurchaseInputReturn.");
        return new ResponseEntity(NO_CONTENT);
    }

    @Operation(summary = "구매입고반품 가능한 lotMaster 리스트 조회", description = "")
    @GetMapping("/lot-masters")
    @ResponseBody
    public ResponseEntity<List<LotMasterResponse.stockAmountAndBadItemAmount>> getPurchaseInputReturnPossibleLotMasters(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<LotMasterResponse.stockAmountAndBadItemAmount> responses = purchaseInputReturnService.getPurchaseInputReturnPossibleLotMasters();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseInputReturnPossibleLotMasters.");
        return new ResponseEntity<>(responses, OK);
    }
}
