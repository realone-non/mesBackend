package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.InputTestRequestCreateRequest;
import com.mes.mesBackend.dto.request.InputTestRequestUpdateRequest;
import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.InputTestRequestService;
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

import static com.mes.mesBackend.entity.enumeration.InputTestDivision.PRODUCT;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 16-1. 검사의뢰 등록
@RequestMapping(value = "/product-input-test-requests")
@Tag(name = "product-input-test-request", description = "16-1. 검사의뢰등록 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@Slf4j
@RequiredArgsConstructor
public class ProductInputTestRequestController {
    private final InputTestRequestService inputTestRequestService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ProductInputTestRequestController.class);
    private CustomLogger cLogger;

    // 검사의뢰 생성
    @Operation(summary = "검사의뢰 생성", description = "* testType 추후 변경 예정, 완제품인 lot id 만 등록가능")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<InputTestRequestResponse> createProductInputTestRequest(
            @RequestBody @Valid InputTestRequestCreateRequest inputTestRequestRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        InputTestRequestResponse inputTestRequest = inputTestRequestService.createInputTestRequest(inputTestRequestRequest, PRODUCT);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + inputTestRequest.getId() + " from createProductInputTestRequest.");
        return new ResponseEntity<>(inputTestRequest, OK);
    }

    // 검사의뢰 리스트 검색 조회
    // 검색조건: 창고 id, LOT 유형 id, 품명|품목, 검사유형, 품목그룹, 요청유형, 의뢰기간
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "검사의뢰 리스트 조회",
            description = "검색조건: 창고 id, LOT 유형 id, 품명|품목, 검사방법, 품목그룹 id, 요청유형, 의뢰기간")
    public ResponseEntity<List<InputTestRequestResponse>> getProductInputTestRequests(
            @RequestParam(required = false) @Parameter(description = "창고 id") Long warehouseId,
            @RequestParam(required = false) @Parameter(description = "LOT 유형 id") Long lotTypeId,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoAndName,
            @RequestParam(required = false) @Parameter(description = "검사방법") InspectionType inspectionType,
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "요청유형") TestType testType,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "의뢰기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "의뢰기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<InputTestRequestResponse> inputTestRequests = inputTestRequestService.getInputTestRequests(
                warehouseId, lotTypeId, itemNoAndName, inspectionType, itemGroupId, testType, fromDate, toDate, PRODUCT);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProductInputTestRequests.");
        return new ResponseEntity<>(inputTestRequests, OK);
    }

    // 검사의뢰 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "검사의뢰 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<InputTestRequestResponse> getProductInputTestRequest(
            @PathVariable(value = "id") @Parameter(description = "검사의뢰 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        InputTestRequestResponse inputTestRequest = inputTestRequestService.getInputTestRequestResponse(id, PRODUCT);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + inputTestRequest.getId() + " from getProductItemInputTestRequest.");
        return new ResponseEntity<>(inputTestRequest, OK);
    }

    // 검사의뢰 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "검사의뢰 수정", description = "* testType 추후 변경 예정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<InputTestRequestResponse> updateProductInputTestRequest(
            @PathVariable(value = "id") @Parameter(description = "검사의뢰 id") Long id,
            @RequestBody @Valid InputTestRequestUpdateRequest inputTestRequestUpdateRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        InputTestRequestResponse inputTestRequest = inputTestRequestService.updateInputTestRequest(id, inputTestRequestUpdateRequest, PRODUCT);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + inputTestRequest.getId() + " from updateProductInputTestRequest.");
        return new ResponseEntity<>(inputTestRequest, OK);
    }

    // 검사의뢰 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "검사의뢰 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteProductInputTestRequest(
            @PathVariable(value = "id") @Parameter(description = "검사의뢰 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        inputTestRequestService.deleteInputTestRequest(id, PRODUCT);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteProductInputTestRequest.");
        return new ResponseEntity(NO_CONTENT);
    }
}
