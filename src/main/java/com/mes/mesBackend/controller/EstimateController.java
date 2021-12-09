package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EstimateItemRequest;
import com.mes.mesBackend.dto.request.EstimatePiRequest;
import com.mes.mesBackend.dto.request.EstimateRequest;
import com.mes.mesBackend.dto.response.EstimateItemResponse;
import com.mes.mesBackend.dto.response.EstimatePiResponse;
import com.mes.mesBackend.dto.response.EstimateResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.EstimateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "estimate", description = "견적 API")
@RequestMapping("/estimates")
@RestController
@SecurityRequirement(name = "Authorization")
public class EstimateController {
    @Autowired
    EstimateService estimateService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(EstimateController.class);
    private CustomLogger cLogger;

    // 견적 생성
    @Operation(summary = "견적 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateResponse> createEstimate(
            @RequestBody @Valid EstimateRequest estimateRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EstimateResponse estimate = estimateService.createEstimate(estimateRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + estimate.getId() + " from createEstimate.");
        return new ResponseEntity<>(estimate, HttpStatus.OK);
    }

    // 견적 단일 조회
    @Operation(summary = "견적 단일 조회")
    @GetMapping("/{estimate-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<EstimateResponse> getEstimate(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EstimateResponse estimate = estimateService.getEstimate(estimateId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + estimate.getId() + " from getEstimate.");
        return new ResponseEntity<>(estimate, HttpStatus.OK);
    }

    // 견적 전체 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 거래처 담당자
    @Operation(summary = "견적 전체 조회", description = "검색조건: 거래처, 견적기간(fromDate~toDate), 화폐, 거래처 담당자")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EstimateResponse>> getEstimates(
            @RequestParam(required = false) @Parameter(description = "거래처") String clientName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestParam(required = false) @Parameter(description = "거래처 담당자 명") String chargeName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<EstimateResponse> estimates = estimateService.getEstimates(clientName, fromDate, toDate, currencyId, chargeName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of currencyId: " + currencyId + " from getEstimates.");
        return new ResponseEntity<>(estimates, HttpStatus.OK);
    }

    // 견적 수정
    @Operation(summary = "견적 수정")
    @PatchMapping("/{estimate-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateResponse> updateEstimate(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestBody @Valid EstimateRequest estimateRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EstimateResponse estimate = estimateService.updateEstimate(estimateId, estimateRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + estimate.getId() + " from updateEstimate.");
        return new ResponseEntity<>(estimate, HttpStatus.OK);
    }

    // 견적 삭제
    @DeleteMapping("/{estimate-id}")
    @Operation(summary = "견적 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteEstimate(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        estimateService.deleteEstimate(estimateId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + estimateId + " from deleteEstimate.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자
//    @Operation(summary = "견적 페이징 조회", description = "검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자")
//    @GetMapping
//    @ResponseBody
//    @Parameters(
//            value = {
//                    @Parameter(
//                            name = "page", description = "0 부터 시작되는 페이지 (0..N)",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "0")
//                    ),
//                    @Parameter(
//                            name = "size", description = "페이지의 사이즈",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "20")
//                    ),
//                    @Parameter(
//                            name = "sort", in = ParameterIn.QUERY,
//                            description = "정렬할 대상과 정렬 방식, 데이터 형식: property(,asc|desc). + 디폴트 정렬순서는 오름차순, 다중정렬 가능",
//                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,desc"))
//                    )
//            }
//    )
//    public ResponseEntity<Page<EstimateResponse>> getEstimates(
//            @RequestParam(required = false) @Parameter(description = "거래처") String clientName,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 fromDate") LocalDate fromDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 toDate") LocalDate toDate,
//            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
//            @RequestParam(required = false) @Parameter(description = "담당자 명") String chargeName,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(estimateService.getEstimates(clientName, fromDate, toDate, currencyId, chargeName, pageable), HttpStatus.OK);
//    }

    // ===================================== 견적 품목 정보 ======================================

    @Operation(summary = "견적 품목정보 생성")
    @PostMapping("/{estimate-id}/estimate-items")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateItemResponse> createEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestBody @Valid EstimateItemRequest estimateItemRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EstimateItemResponse estimateItem = estimateService.createEstimateItem(estimateId, estimateItemRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + estimateItem.getId() + " from createEstimateItem.");
        return new ResponseEntity<>(estimateItem, HttpStatus.OK);
    }

    @Operation(summary = "견적 품목정보 단일 조회")
    @GetMapping("/{estimate-id}/estimate-items/{estimate-item-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateItemResponse> getEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "estimate-item-id") @Parameter(description = "견적 품목 id") Long estimateItemId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EstimateItemResponse estimateItem = estimateService.getEstimateItem(estimateId, estimateItemId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + estimateItem.getId() + " from getEstimateItem.");
        return new ResponseEntity<>(estimateItem, HttpStatus.OK);
    }


    @Operation(summary = "견적 품목정보 리스트 조회",description = "해당하는 견적에 대한 모든 품목 조회")
    @GetMapping("/{estimate-id}/estimate-items")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<EstimateItemResponse>> getEstimateItems(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<EstimateItemResponse> estimateItems = estimateService.getEstimateItems(estimateId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getEstimateItems.");
        return new ResponseEntity<>(estimateItems, HttpStatus.OK);
    }

    @Operation(summary = "견적 품목정보 수정")
    @PatchMapping("/{estimate-id}/estimate-items/{estimate-item-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateItemResponse> updateEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "estimate-item-id") @Parameter(description = "견적 품목 id") Long estimateItemId,
            @RequestBody @Valid EstimateItemRequest estimateItemRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EstimateItemResponse estimateItem = estimateService.updateEstimateItem(estimateId, estimateItemId, estimateItemRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + estimateItem.getId() + " from updateEstimateItem.");
        return new ResponseEntity<>(estimateItem, HttpStatus.OK);
    }

    @Operation(summary = "견적 품목정보 삭제")
    @DeleteMapping("/{estimate-id}/estimate-items/{estimate-item-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity deleteEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "estimate-item-id") @Parameter(description = "견적 품목 id") Long estimateItemId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        estimateService.deleteEstimateItem(estimateId, estimateItemId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + estimateItemId + " from deleteEstimateItem.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

// ===================================== 견적 P/I ======================================
    // p/i
    /*
    * - invoice No 자동생성인지?
    *  널 허용 여부랑
    * 견적이랑 1:1인지
    * */
    @Operation(summary = "견적 P/I 생성")
    @PostMapping("/{estimate-id}/pis")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> createEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestBody @Valid EstimatePiRequest estimatePiRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        EstimatePiResponse estimatePi = estimateService.createEstimatePi(estimateId, estimatePiRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + estimatePi.getId() + " from createEstimatePi.");
        return new ResponseEntity<>(estimatePi, HttpStatus.OK);
    }

    @Operation(summary = "견적 P/I 조회")
    @GetMapping("/{estimate-id}/pis")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> getEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EstimatePiResponse estimatePi = estimateService.getEstimatePi(estimateId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + estimatePi.getId() + " from getEstimatePi.");
        return new ResponseEntity<>(estimatePi, HttpStatus.OK);
    }

    @Operation(summary = "견적 P/I 수정")
    @PatchMapping("/{estimate-id}/pis/{pi-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> updateEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "pi-id") @Parameter(description = "견적 P/I id") Long estimatePiId,
            @RequestBody @Valid EstimatePiRequest estimatePiRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        EstimatePiResponse estimatePi = estimateService.updateEstimatePi(estimateId, estimatePiId, estimatePiRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + estimatePi.getId() + " from updateEstimatePi.");
        return new ResponseEntity<>(estimatePi, HttpStatus.OK);
    }

    @Operation(summary = "견적 P/I 삭제")
    @DeleteMapping("/{estimate-id}/pis/{pi-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> deleteEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "pi-id") @Parameter(description = "견적 P/I id") Long estimatePiId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        estimateService.deleteEstimatePi(estimateId, estimatePiId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + estimatePiId + " from deleteEstimatePi.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
