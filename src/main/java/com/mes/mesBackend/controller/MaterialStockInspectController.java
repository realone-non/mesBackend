package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.MaterialStockInspectRequestRequest;
import com.mes.mesBackend.dto.request.RequestMaterialStockInspect;
import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;
import com.mes.mesBackend.dto.response.MaterialStockInspectResponse;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.repository.custom.MaterialStockInspectRequestRepositoryCustom;
import com.mes.mesBackend.service.MaterialWarehouseService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

//재고실사 등록
@Tag(name = "material-stockinspect")
@RequestMapping(value = "/material-stockinspect")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class MaterialStockInspectController {
    @Autowired
    MaterialWarehouseService materialWarehouseService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(MaterialStockInspectRequestRepositoryCustom.class);
    private CustomLogger cLogger;

    //DB재고실사 데이터 등록
    @PostMapping("/{request-id}/stock-inspect")
    @ResponseBody
    @Operation(summary = "DB재고실사 데이터 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "created"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<Void> createStockInspectData(
            @PathVariable(value = "request-id") @Parameter(description = "실사의뢰 ID") Long requestId,
            @RequestParam(required = false) @Parameter(description = "품목계정ID") Long itemAccountId,
            @RequestParam(required = true) @Parameter(description = "실사의뢰 타입") InspectionType type,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException{
        materialWarehouseService.createMaterialStockInspect(requestId, itemAccountId, type);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the stockInspect requestId:" + requestId + " from createStockInspectData.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    //재고실사 조회
    @GetMapping("/{request-id}")
    @ResponseBody
    @Operation(summary = "재고실사 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<MaterialStockInspectResponse>> getMaterialStockInspects(
            @PathVariable(value = "request-id") @Parameter(description = "실사의뢰 ID") Long requestId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "시작날짜") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "종료날짜") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "아이템어카운트명") String itemAccount,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException{
        List<MaterialStockInspectResponse> responseList = materialWarehouseService.getMaterialStockInspects(requestId, fromDate, toDate, itemAccount);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getMaterialStockInspects.");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    //재고실사 단일 조회
    @GetMapping("/{request-id}/{inspect-id}")
    @ResponseBody
    @Operation(summary = "재고실사 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MaterialStockInspectResponse> getMaterialStockInspect(
            @PathVariable(value = "request-id") @Parameter(description = "재고실사의뢰 ID") Long requestId,
            @PathVariable(value = "inspect-id") @Parameter(description = "재고실사 ID") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException{
        MaterialStockInspectResponse response = materialWarehouseService.getMaterialStockInspect(requestId, id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + id + " from getMaterialStockInspect.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //재고실사 수정
    @PatchMapping("/{request-id}")
    @ResponseBody()
    @Operation(summary = "재고실사 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<MaterialStockInspectResponse>> modifyMaterialStockInspect(
            @PathVariable(value = "request-id") @Parameter(description = "실사의뢰 id") Long requestId,
            @RequestBody @Valid List<RequestMaterialStockInspect> requestList,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        List<MaterialStockInspectResponse> responseList = materialWarehouseService.modifyMaterialStockInspect(requestId, requestList);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the stockRequestId:" + requestId + " from modifyMaterialStockInspect.");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    //재고실사 삭제
    @DeleteMapping("{id}")
    @ResponseBody()
    @Operation(summary = "재고실사 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteMaterialStockInspect(
            @PathVariable(value = "id") @Parameter(description = "재고실사 id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        materialWarehouseService.deleteMaterialStockInspect(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteMaterialStockInspect.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
