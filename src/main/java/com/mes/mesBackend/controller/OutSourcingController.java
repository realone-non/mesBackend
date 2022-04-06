package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.OutsourcingMaterialReleaseRequest;
import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
import com.mes.mesBackend.dto.response.OutsourcingMaterialReleaseResponse;
import com.mes.mesBackend.dto.response.OutsourcingProductionResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.OutsourcingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "outsourcing", description = "외주생산관리 API")
@RequestMapping(value = "/outsourcings")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class OutSourcingController {
    private final OutsourcingService outsourcingService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ItemFormController.class);
    private CustomLogger cLogger;

    // 외주생산의뢰 등록
    @PostMapping
    @ResponseBody
    @Operation(summary = "외주생산의뢰 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingProductionResponse> createOutsourcingProduction(
            @RequestBody @Valid OutsourcingProductionRequestRequest productionRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingProductionResponse outsourcingProductionResponse = outsourcingService.createOutsourcingProduction(productionRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + outsourcingProductionResponse.getId() + " from createoutsourcingProduction.");
        return new ResponseEntity<>(outsourcingProductionResponse, OK);
    }

    // 외주생산 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "외주생산 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<OutsourcingProductionResponse>> getOutsourcingProductions(
            @RequestParam(required = false) @Parameter(description = "외주사 ID") Long clientId,
            @RequestParam(required = false) @Parameter(description = "아이템 품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "아이템 품명") String itemName,
            @RequestParam(required = false) @Parameter(description = "시작날짜") LocalDate startDate,
            @RequestParam(required = false) @Parameter(description = "종료날짜") LocalDate endDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        List<OutsourcingProductionResponse> productions = outsourcingService.getOutsourcingProductions(clientId, itemNo, itemName, startDate, endDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        return new ResponseEntity<>(productions, OK);
    }

    // 외주생산의뢰 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "외주생산의뢰 조회")
    public ResponseEntity<OutsourcingProductionResponse> getOutsourcingProduction(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingProductionResponse production = outsourcingService.getOutsourcingProductionResponseOrThrow(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemForms.");
        return new ResponseEntity<>(production, OK);
    }

    // 외주생산의뢰 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "외주생산의뢰 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingProductionResponse> modifyOutsourcingProduction(
            @PathVariable Long id,
            @RequestBody @Valid OutsourcingProductionRequestRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingProductionResponse production = outsourcingService.modifyOutsourcingProduction(id, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + production.getId() + " from modifyOutsourcingProduction.");
        return new ResponseEntity<>(production, OK);
    }

    // 외주생산의뢰 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "외주생산의뢰 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteOutsourcingProduction(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        outsourcingService.deleteOutsourcingProduction(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteOutsourcingProduction.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 외주생산 원재료 출고 대상 등록
    @PostMapping("/{request-id}/material")
    @ResponseBody
    @Operation(summary = "외주생산 원재료 출고 대상 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingMaterialReleaseResponse> createOutsourcingMaterialRelease(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산 ID") Long id,
            @RequestBody @Valid OutsourcingMaterialReleaseRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingMaterialReleaseResponse response = outsourcingService.createOutsourcingMaterial(id,request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + response.getId() + " from createOutsourcingMaterialRelase.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주생산 원재료 출고 대상 리스트 조회
    @GetMapping("/{request-id}/material")
    @ResponseBody
    @Operation(summary = "외주생산 원재료 출고 대상 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<OutsourcingMaterialReleaseResponse>> getOutsourcingMaterialReleaseList(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산 ID") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<OutsourcingMaterialReleaseResponse> responses = outsourcingService.getOutsourcingMeterials(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getOutsourcingMaterialReleaseList.");
        return new ResponseEntity<>(responses, OK);
    }

    // 외주생산 원재료 출고 대상 단일 조회
    @GetMapping("/{request-id}/material/{id}")
    @ResponseBody
    @Operation(summary = "외주생산 원재료 출고 대상 단일 조회")
    public ResponseEntity<OutsourcingMaterialReleaseResponse> getOutsourcingMaterialRelease(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산의뢰 ID") Long requestId,
            @PathVariable(value = "id") @Parameter(description = "원재료 출고 대상 ID") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingMaterialReleaseResponse response = outsourcingService.getOutsourcingMaterialResponseOrThrow(requestId,id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemForms.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주생산 원재료 출고 대상 수정
    @PatchMapping("/{request-id}/material/{id}")
    @ResponseBody
    @Operation(summary = "외주생산원재료 출고 대상 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingMaterialReleaseResponse> modifyOutsourcingMaterialRelease (
            @PathVariable(value = "request-id") @Parameter(description = "외주 생산 의뢰 ID") long requestId,
            @PathVariable(value = "id") @Parameter(description = "원재료 출고 ID") long id,
            @RequestBody @Valid OutsourcingMaterialReleaseRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingMaterialReleaseResponse response = outsourcingService.modifyOutsourcingMaterial(requestId, id, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + id + " from modifyOutsourcingMaterialRelease.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주생산원재료 출고 대상 삭제
    @DeleteMapping("/{request-id}/material/{id}")
    @ResponseBody
    @Operation(summary = "외주생산원재료 출고 대상 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteOutsourcingMaterialRelease(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산 ID") Long requestId,
            @PathVariable(value = "id") @Parameter(description = "원재료 출고대상 ID") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        outsourcingService.deleteOutsourcingMaterial(requestId, id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteOutsourcingMaterialRelease.");
        return new ResponseEntity(NO_CONTENT);
    }
}
