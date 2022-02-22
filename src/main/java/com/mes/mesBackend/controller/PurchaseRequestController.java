package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.PurchaseRequestRequest;
import com.mes.mesBackend.dto.response.ProduceRequestBomDetail;
import com.mes.mesBackend.dto.response.PurchaseRequestResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PurchaseRequestService;
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

// 9-1. 구매요청 등록
@RequestMapping(value = "/purchase-requests")
@Tag(name = "purchase-request", description = "구매요청등록 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class PurchaseRequestController {
    private final PurchaseRequestService purchaseRequestService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PurchaseRequestController.class);
    private CustomLogger cLogger;

    // 구매요청 생성
    @Operation(summary = "구매요청 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseRequestResponse> createPurchaseRequest(
            @RequestBody @Valid PurchaseRequestRequest purchaseRequestRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws BadRequestException, NotFoundException {
        PurchaseRequestResponse purchaseRequest = purchaseRequestService.createPurchaseRequest(purchaseRequestRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + purchaseRequest.getId() + " from createPurchaseRequest.");
        return new ResponseEntity<>(purchaseRequest, OK);
    }

    // 구매요청 단일조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "구매요청 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<PurchaseRequestResponse> getPurchaseRequest(
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PurchaseRequestResponse purchaseRequest = purchaseRequestService.getPurchaseRequestResponseOrThrow(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + purchaseRequest.getId() + " from getPurchaseRequest.");
        return new ResponseEntity<>(purchaseRequest, OK);
    }

    // 구매요청 리스트 조회, 검색조건: 요청기간, 제조오더번호, 품목그룹, 품번|품명, 제조사 품번, 완료포함(check)
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "구매요청 전체 조회",
            description = "검색조건: 요청기간, 제조오더번호, 품목그룹, 품번|품명, 제조사 품번, 완료포함(check)"
    )
    public ResponseEntity<List<PurchaseRequestResponse>> getPurchaseRequests(
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "요청기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "요청기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "제조오더번호") String produceOrderNo,
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoAndName,
            @RequestParam(required = false) @Parameter(description = "제조사 품번") String manufacturerPartNo,
            @RequestParam(required = false) @Parameter(description = "완료포함") Boolean orderCompletion,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<PurchaseRequestResponse> purchaseRequests = purchaseRequestService.getPurchaseRequests(fromDate, toDate, produceOrderNo, itemGroupId, itemNoAndName, manufacturerPartNo, orderCompletion);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPurchaseRequests.");
        return new ResponseEntity<>(purchaseRequests, OK);
    }

    // 구매요청 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "구매요청 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PurchaseRequestResponse> updatePurchaseRequest(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid PurchaseRequestRequest purchaseRequestRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        PurchaseRequestResponse purchaseRequest = purchaseRequestService.updatePurchaseRequest(id, purchaseRequestRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + purchaseRequest.getId() + " from updatePurchaseRequest.");
        return new ResponseEntity<>(purchaseRequest, OK);
    }

    // 구매요청 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "구매요청 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deletePurchaseRequest(
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        purchaseRequestService.deletePurchaseRequest(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deletePurchaseRequest.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 수주품목에 해당하는 원부자재
    @GetMapping("/bom-details")
    @ResponseBody
    @Operation(summary = "수주품목에 해당하는 원부자재")
    public ResponseEntity<List<ProduceRequestBomDetail>> getProduceOrderBomDetails(
            @RequestParam @Parameter(description = "제조 오더 id") Long produceOrderId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ProduceRequestBomDetail> produceRequestBomDetails = purchaseRequestService.getProduceOrderBomDetails(produceOrderId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProduceOrderBomDetails.");
        return new ResponseEntity<>(produceRequestBomDetails, OK);
    }
}
