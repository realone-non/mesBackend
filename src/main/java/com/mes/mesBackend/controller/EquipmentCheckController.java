package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EquipmentCheckDetailRequest;
import com.mes.mesBackend.dto.response.EquipmentCheckDetailResponse;
import com.mes.mesBackend.dto.response.EquipmentCheckResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.EquipmentCheckService;
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

// 17-1. 설비점검 실적 등록
@Tag(name = "equipment-check", description = "17-1. 설비점검 실적 등록 API")
@RequestMapping("/equipment-checks")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class EquipmentCheckController {
    private final EquipmentCheckService equipmentCheckService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(EquipmentCheckController.class);
    private CustomLogger cLogger;

    // 설비 리스트 조회
    // 검색조건: 설비유형, 작업기간(디테일 정보 생성날짜 기준) fromDate~toDate
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "설비정보 전체 조회",
            description = "검색조건: 설비유형,, 작업기간 fromDate~toDate"
    )
    public ResponseEntity<List<EquipmentCheckResponse>> getEquipmentChecks(
            @RequestParam(required = false) @Parameter(description = "설비유형(작업라인 id)") Long workLineId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<EquipmentCheckResponse> equipmentChecks = equipmentCheckService.getEquipmentChecks(workLineId, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getEquipmentChecks.");
        return new ResponseEntity<>(equipmentChecks, OK);
    }
    
    // 설비 단일 조회
    @GetMapping("/{equipment-id}")
    @ResponseBody
    @Operation(summary = "설비정보 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<EquipmentCheckResponse> getEquipmentCheck(
            @PathVariable(value = "equipment-id") @Parameter(description = "설비 id") Long equipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentCheckResponse equipmentCheck = equipmentCheckService.getEquipmentCheckResponse(equipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + equipmentCheck.getId() + " from getEquipmentCheck.");
        return new ResponseEntity<>(equipmentCheck, OK);
    }

    // ================================================ 설비점검 실적 상세 정보 ================================================
    // 상세정보 생성
    @Operation(summary = "상세정보 생성", description = "")
    @PostMapping("/{equipment-id}/equipment-check-details")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentCheckDetailResponse> createEquipmentCheckDetail(
            @PathVariable(value = "equipment-id") @Parameter(description = "설비 id") Long equipmentId,
            @RequestBody @Valid EquipmentCheckDetailRequest equipmentCheckDetailRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentCheckDetailResponse equipmentCheckDetail = equipmentCheckService.createEquipmentCheckDetail(equipmentId, equipmentCheckDetailRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + equipmentCheckDetail.getId() + " from createEquipmentCheckDetail.");
        return new ResponseEntity<>(equipmentCheckDetail, OK);
    }
    
    // 상세정보 전체 조회
    @GetMapping("/{equipment-id}/equipment-check-details")
    @ResponseBody
    @Operation(summary = "상세정보 전체 조회", description = "")
    public ResponseEntity<List<EquipmentCheckDetailResponse>> getEquipmentCheckDetails(
            @PathVariable(value = "equipment-id") @Parameter(description = "설비 id") Long equipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<EquipmentCheckDetailResponse> equipmentCheckDetails = equipmentCheckService.getEquipmentCheckDetails(equipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getEquipmentCheckDetails.");
        return new ResponseEntity<>(equipmentCheckDetails, OK);
    }
    
    // 상세정보 단일 조회
    @GetMapping("/{equipment-id}/equipment-check-details/{equipment-check-detail-id}")
    @ResponseBody()
    @Operation(summary = "상세정보 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<EquipmentCheckDetailResponse> getEquipmentCheckDetail(
            @PathVariable(value = "equipment-id") @Parameter(description = "설비 id") Long equipmentId,
            @PathVariable(value = "equipment-check-detail-id") @Parameter(description = "설비상세 id") Long equipmentCheckDetailId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentCheckDetailResponse equipmentCheckDetail = equipmentCheckService.getEquipmentCheckDetailResponseOrThrow(equipmentId, equipmentCheckDetailId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + equipmentCheckDetail.getId() + " from getEquipmentCheckDetail.");
        return new ResponseEntity<>(equipmentCheckDetail, OK);
    }
    
    // 상세정보 수정
    @PatchMapping("/{equipment-id}/equipment-check-details/{equipment-check-detail-id}")
    @ResponseBody()
    @Operation(summary = "상세정보 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentCheckDetailResponse> updateEquipmentCheckDetail(
            @PathVariable(value = "equipment-id") @Parameter(description = "설비 id") Long equipmentId,
            @PathVariable(value = "equipment-check-detail-id") @Parameter(description = "설비상세 id") Long equipmentCheckDetailId,
            @RequestBody @Valid EquipmentCheckDetailRequest equipmentCheckDetailRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentCheckDetailResponse equipmentCheckDetail = equipmentCheckService.updateEquipmentCheckDetail(equipmentId, equipmentCheckDetailId, equipmentCheckDetailRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + equipmentCheckDetail.getId() + " from updateEquipmentCheckDetail.");
        return new ResponseEntity<>(equipmentCheckDetail, OK);
    }
    
    // 상제정보 삭제
    @DeleteMapping("/{equipment-id}/equipment-check-details/{equipment-check-detail-id}")
    @ResponseBody()
    @Operation(summary = "상세정보 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteEquipmentCheckDetail(
            @PathVariable(value = "equipment-id") @Parameter(description = "설비 id") Long equipmentId,
            @PathVariable(value = "equipment-check-detail-id") @Parameter(description = "설비상세 id") Long equipmentCheckDetailId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentCheckService.deleteEquipmentCheckDetail(equipmentId, equipmentCheckDetailId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + equipmentCheckDetailId + " from deleteEquipmentCheckDetail.");
        return new ResponseEntity(NO_CONTENT);
    }
}
