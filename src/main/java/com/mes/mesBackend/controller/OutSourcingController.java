package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemFormRequest;
import com.mes.mesBackend.dto.request.OutsourcingMaterialReleaseRequest;
import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
import com.mes.mesBackend.dto.response.ItemFormResponse;
import com.mes.mesBackend.dto.response.OutsourcingMaterialReleaseResponse;
import com.mes.mesBackend.dto.response.OutsourcingProductionResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ItemFormService;
import com.mes.mesBackend.service.OutsourcingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 품목형태
@Tag(name = "outsourcing", description = "외주관리 API")
@RequestMapping(value = "/outsourcing")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class OutSourcingController {

    @Autowired
    OutsourcingService outsourcingService;

    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(ItemFormController.class);
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
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        OutsourcingProductionResponse outsourcingProductionResponse = outsourcingService.createOutsourcingProduction(productionRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + outsourcingProductionResponse.getId() + " from createoutsourcingProduction.");
        return new ResponseEntity<>(outsourcingProductionResponse, HttpStatus.OK);
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
            @RequestParam(required = false) @Parameter(description = "아이템 ID") Long itemId,
            @RequestParam(required = false) @Parameter(description = "시작날짜") LocalDate startDate,
            @RequestParam(required = false) @Parameter(description = "종료날짜") LocalDate endDate,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<OutsourcingProductionResponse> productions = outsourcingService.getOutsourcingProductions(clientId, itemId, startDate, endDate);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        return new ResponseEntity<>(productions, HttpStatus.OK);
    }

    // 외주생산의뢰 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "외주생산의뢰 조회")
    public ResponseEntity<Optional<OutsourcingProductionResponse>> getOutsourcingProduction(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        Optional<OutsourcingProductionResponse> production = outsourcingService.getOutsourcingProduction(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemForms.");
        return new ResponseEntity<>(production, HttpStatus.OK);
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
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingProductionResponse production = outsourcingService.modifyOutsourcingProduction(id, request);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + production.getId() + " from modifyOutsourcingProduction.");
        return new ResponseEntity<>(production, HttpStatus.OK);
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
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        outsourcingService.deleteOutsourcingProduction(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteOutsourcingProduction.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 외주생산 원재료 출고 대상 등록
    @PostMapping("/material")
    @ResponseBody
    @Operation(summary = "외주생산 원재료 출고 대상 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<Optional<OutsourcingMaterialReleaseResponse>> createOutsourcingMaterialRelease(
            @RequestBody @Valid OutsourcingMaterialReleaseRequest request,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        Optional<OutsourcingMaterialReleaseResponse> response = outsourcingService.createOutsourcingMaterial(request);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + response.get().getId() + " from createOutsourcingMaterialRelase.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 외주생산 원재료 출고 대상 리스트 조회
    @GetMapping("/{productionId}/material")
    @ResponseBody
    @Operation(summary = "외주생산 원재료 출고 대상 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<OutsourcingMaterialReleaseResponse>> getOutsourcingMaterialReleaseList(
            @PathVariable Long productionId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<OutsourcingMaterialReleaseResponse> responses = outsourcingService.getOutsourcingMeterials(productionId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getOutsourcingMaterialReleaseList.");
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 외주생산 원재료 출고 대상 단일 조회
    @GetMapping("/material/{id}")
    @ResponseBody
    @Operation(summary = "외주생산 원재료 출고 대상 단일 조회")
    public ResponseEntity<Optional<OutsourcingMaterialReleaseResponse>> getOutsourcingMaterialRelease(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        Optional<OutsourcingMaterialReleaseResponse> response = outsourcingService.getOutsourcingMaterial(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemForms.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 외주생산 원재료 출고 대상 수정
    @PatchMapping("/material/{id}")
    @ResponseBody
    @Operation(summary = "외주생산원재료 출고 대상 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<Optional<OutsourcingMaterialReleaseResponse>> modifyOutsourcingMaterialRelease (
            @PathVariable Long id,
            @RequestBody @Valid OutsourcingMaterialReleaseRequest request,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        Optional<OutsourcingMaterialReleaseResponse> response = outsourcingService.modifyOutsourcingMaterial(id, request);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + id + " from modifyOutsourcingMaterialRelease.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 외주생산원재료 출고 대상 삭제
    @DeleteMapping("/material/{id}")
    @ResponseBody
    @Operation(summary = "외주생산원재료 출고 대상 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteOutsourcingMaterialRelease(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        outsourcingService.deleteOutsourcingMaterial(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteOutsourcingMaterialRelease.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
