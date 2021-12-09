package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.GaugeTypeRequest;
import com.mes.mesBackend.dto.response.GaugeTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.GaugeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "gauge-type", description = "GAUGE 유형 API")
@RequestMapping("/gauge-types")
@RestController
@SecurityRequirement(name = "Authorization")
public class GaugeTypeController {
    @Autowired
    GaugeTypeService gaugeTypeService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(GaugeTypeController.class);
    private CustomLogger cLogger;

    // GAUGE 유형 생성
    @Operation(summary = "GAUGE 유형 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<GaugeTypeResponse> createGaugeType(
            @RequestBody @Valid GaugeTypeRequest gaugeTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        GaugeTypeResponse gaugeType = gaugeTypeService.createGaugeType(gaugeTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + gaugeType.getId() + " from createGaugeType.");
        return new ResponseEntity<>(gaugeType, HttpStatus.OK);
    }

    // GAUGE 유형 단일 조회
    @Operation(summary = "GAUGE 유형 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<GaugeTypeResponse> getGaugeType(
            @PathVariable @Parameter(description = "GAUGE 유형 id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        GaugeTypeResponse gaugeType = gaugeTypeService.getGaugeType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + gaugeType.getId() + " from getGaugeType.");
        return new ResponseEntity<>(gaugeType, HttpStatus.OK);
    }

    // GAUGE 유형 리스트 조회
    @Operation(summary = "GAUGE 유형 리스트 조회", description = "")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<GaugeTypeResponse>> getGaugeTypes(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<GaugeTypeResponse> gaugeTypes = gaugeTypeService.getGaugeTypes();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getGaugeTypes.");
        return new ResponseEntity<>(gaugeTypes, HttpStatus.OK);
    }

    // GAUGE 유형 수정
    @Operation(summary = "GAUGE 유형 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<GaugeTypeResponse> updateGaugeType(
            @PathVariable @Parameter(description = "GAUGE 유형 id") Long id,
            @RequestBody @Valid GaugeTypeRequest gaugeTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        GaugeTypeResponse gaugeType = gaugeTypeService.updateGaugeType(id, gaugeTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + gaugeType.getId() + " from updateGaugeType.");
        return new ResponseEntity<>(gaugeType, HttpStatus.OK);
    }

    // GAUGE 유형 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "GAUGE 유형 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteGaugeType(
            @PathVariable @Parameter(description = "GAUGE 유형 id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        gaugeTypeService.deleteGaugeType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteGaugeType.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
