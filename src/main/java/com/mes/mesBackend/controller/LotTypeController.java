package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.LotTypeRequest;
import com.mes.mesBackend.dto.response.LotTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.LotTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// Lot 유형
@Tag(name = "lot-type", description = "LOT 유형 API")
@RequestMapping(value = "/lot-types")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class LotTypeController {
    @Autowired
    LotTypeService lotTypeService;

    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(LotTypeController.class);
    private CustomLogger cLogger;


    // LOT 유형 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "LOT 유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<LotTypeResponse> createLotType(
            @RequestBody @Valid LotTypeRequest lotTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        LotTypeResponse lotType = lotTypeService.createLotType(lotTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + lotType.getId() + " from createLotType.");
        return new ResponseEntity<>(lotType, HttpStatus.OK);
    }

    // LOT 유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "LOT 유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<LotTypeResponse> getLotType(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        LotTypeResponse lotType = lotTypeService.getLotType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + lotType.getId() + " from getLotType.");
        return new ResponseEntity<>(lotType, HttpStatus.OK);
    }

    // LOT 유형 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "LOT 유형 리스트 조회")
    public ResponseEntity<List<LotTypeResponse>> getLotTypes(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<LotTypeResponse> lotTypes = lotTypeService.getLotTypes();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getLotTypes.");
        return new ResponseEntity<>(lotTypes, HttpStatus.OK);
    }

    // LOT 유형 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "LOT 유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<LotTypeResponse> updateLotType(
            @PathVariable Long id,
            @RequestBody @Valid LotTypeRequest lotTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        LotTypeResponse lotType = lotTypeService.updateLotType(id, lotTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + lotType.getId() + " from updateLotType.");
        return new ResponseEntity<>(lotType, HttpStatus.OK);
    }

    // LOT 유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "LOT 유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteLotType(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        lotTypeService.deleteLotType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteLotType.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
