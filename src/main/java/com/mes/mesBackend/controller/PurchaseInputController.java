package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.PurchaseInputRequest;
import com.mes.mesBackend.dto.response.PurchaseInputDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseInputResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PurchaseInputService;
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

// 9-5. 구매입고 등록
@RequestMapping(value = "/purchase-inputs")
@Tag(name = "purchase-input", description = "구매입고 등록 API")
@RestController
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class PurchaseInputController {
    private final PurchaseInputService purchaseInputService;
    private final LogService logService;
    private Logger logger = LoggerFactory.getLogger(PurchaseInputController.class);
    private CustomLogger cLogger;

    // 구매입고 리스트 조회, 검색조건: 입고기간 fromDate~toDate, 입고창고, 거래처, 품명|품번
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "구매입고 전체 조회",
            description = "검색조건: 검색조건: 입고기간 fromDate~toDate, 입고창고, 거래처, 품명|품번"
    )
    public ResponseEntity<List<PurchaseInputResponse>> getPurchaseInputs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "입고기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "입고기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "입고창고 id") Long wareHouseId,
            @RequestParam(required = false) @Parameter(description = "거래처 id") Long clientId,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoOrItemName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<PurchaseInputResponse> purchaseInputs = purchaseInputService.getPurchaseInputs(fromDate, toDate, wareHouseId, clientId, itemNoOrItemName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseInputs.");
        return new ResponseEntity<>(purchaseInputs, HttpStatus.OK);
    }


    // ================================ 구매입고 LOT 정보 ================================
    // 구매입고 LOT 생성
    @Operation(summary = "구매입고상세 생성", description = "LOT 자동생성")
    @PostMapping("/{purchase-request-id}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseInputDetailResponse> createPurchaseInputDetail(
            @PathVariable(value = "purchase-request-id") @Parameter(description = "구매입고(구매요청) id") Long purchaseRequestId,
            @RequestBody @Valid PurchaseInputRequest purchaseInputRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        PurchaseInputDetailResponse purchaseInputDetail = purchaseInputService.createPurchaseInputDetail(purchaseRequestId, purchaseInputRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + purchaseInputDetail.getId() + " from createPurchaseInputDetail.");
        return new ResponseEntity<>(purchaseInputDetail, HttpStatus.OK);
    }

    // 구매입고 LOT 전체 조회
    @GetMapping("/{purchase-request-id}/purchase-input-details")
    @ResponseBody
    @Operation(summary = "구매입고상세 전체 조회", description = "구매입고에 해당하는 전체 구매입고 LOT 정보 조회")
    public ResponseEntity<List<PurchaseInputDetailResponse>> getPurchaseInputDetails(
            @PathVariable(value = "purchase-request-id") @Parameter(description = "구매입고(구매요청) id") Long purchaseRequestId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PurchaseInputDetailResponse> purchaseInputDetails = purchaseInputService.getPurchaseInputDetails(purchaseRequestId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseInputDetails.");
        return new ResponseEntity<>(purchaseInputDetails, HttpStatus.OK);
    }

    // 구메입고 LOT 단일 조회
    @GetMapping("/{purchase-request-id}/purchase-input-details/{purchase-input-id}")
    @ResponseBody()
    @Operation(summary = "구매입고상세 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<PurchaseInputDetailResponse> getPurchaseInputDetail(
            @PathVariable(value = "purchase-request-id") @Parameter(description = "구매입고(구매요청) id") Long purchaseRequestId,
            @PathVariable(value = "purchase-input-id") @Parameter(description = "구매입고 상세 id") Long purchaseInputId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PurchaseInputDetailResponse purchaseInputDetail = purchaseInputService.getPurchaseInputDetailResponse(purchaseRequestId, purchaseInputId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + purchaseInputDetail.getId() + " from getPurchaseInputDetail.");
        return new ResponseEntity<>(purchaseInputDetail, HttpStatus.OK);
    }

    // 구매입고 LOT 수정
    @PatchMapping("/{purchase-request-id}/purchase-input-details/{purchase-input-id}")
    @ResponseBody()
    @Operation(summary = "구매입고상세 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseInputDetailResponse> updatePurchaseInputDetail(
            @PathVariable(value = "purchase-request-id") @Parameter(description = "구매입고(구매요청) id") Long purchaseRequestId,
            @PathVariable(value = "purchase-input-id") @Parameter(description = "구매입고 상세 id") Long purchaseInputId,
            @RequestBody @Valid PurchaseInputRequest.updateRequest purchaseInputUpdateRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PurchaseInputDetailResponse purchaseInputDetail = purchaseInputService.updatePurchaseInputDetail(purchaseRequestId, purchaseInputId, purchaseInputUpdateRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + purchaseInputDetail.getId() + " from updatePurchaseInputDetail.");
        return new ResponseEntity<>(purchaseInputDetail, HttpStatus.OK);
    }

    // 구매입고 LOT 삭제
    @DeleteMapping("/{purchase-request-id}/purchase-input-details/{purchase-input-id}")
    @ResponseBody()
    @Operation(summary = "구매입고상세 삭제", description = "해당 구매입고에서 제거")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deletePurchaseInputDetail(
            @PathVariable(value = "purchase-request-id") @Parameter(description = "구매입고(구매요청) id") Long purchaseRequestId,
            @PathVariable(value = "purchase-input-id") @Parameter(description = "구매입고 상세 id") Long purchaseInputId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        purchaseInputService.deletePurchaseInputDetail(purchaseRequestId, purchaseInputId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + purchaseInputId + " from deletePurchaseInputDetail.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
