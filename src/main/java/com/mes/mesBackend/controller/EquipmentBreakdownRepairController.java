package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EquipmentBreakdownRequest;
import com.mes.mesBackend.dto.request.RepairPartRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.EquipmentBreakdownService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

// 17-2. 설비 고장 수리내역 등록
@Tag(name = "equipment-breakdown-repair", description = "17-2. 설비 고장 수리내역 등록 API")
@RequestMapping("/equipment-breakdown-repairs")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class EquipmentBreakdownRepairController {
    private final EquipmentBreakdownService equipmentBreakdownService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(EquipmentBreakdownRepairController.class);
    private CustomLogger cLogger;

    // 설비고장 생성
    @Operation(summary = "설비고장 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentBreakdownResponse> createEquipmentBreakdown(
            @RequestBody @Valid EquipmentBreakdownRequest equipmentBreakdownRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentBreakdownResponse equipmentBreakdownResponse = equipmentBreakdownService.createEquipmentBreakdown(equipmentBreakdownRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + equipmentBreakdownResponse.getId() + " from createEquipmentBreakdown.");
        return new ResponseEntity<>(equipmentBreakdownResponse, OK);
    }

    // 설비고장 리스트 검색 조회, 검색조건: 작업장 id, 설비유형, 작업기간 fromDate~toDate
    @Operation(summary = "설비고장 리스트 조회", description = "검색조건: 작업장 id, 설비유형(작업라인 id), 작업기간 fromDate~toDate")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EquipmentBreakdownResponse>> getEquipmentBreakdowns(
            @RequestParam(required = false) @Parameter(description = "작업장 id") Long workCenterId,
            @RequestParam(required = false) @Parameter(description = "설비유형(작업라인 id)") Long workLineId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<EquipmentBreakdownResponse> equipmentBreakdownResponses = equipmentBreakdownService.getEquipmentBreakdowns(workCenterId, workLineId, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getEquipmentBreakdowns.");
        return new ResponseEntity<>(equipmentBreakdownResponses, OK);
    }

    // 설비고장 단일조회
    @Operation(summary = "설비고장 단일 조회")
    @GetMapping("/{equipment-breakdown-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<EquipmentBreakdownResponse> getEquipmentBreakdown(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentBreakdownResponse equipmentBreakdownResponse = equipmentBreakdownService.getEquipmentBreakdown(equipmentBreakdownId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + equipmentBreakdownResponse.getId() + " from getEquipmentBreakdown.");
        return new ResponseEntity<>(equipmentBreakdownResponse, OK);
    }

    // 설비고장 수정
    @Operation(summary = "설비고장 수정")
    @PatchMapping("/{equipment-breakdown-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentBreakdownResponse> updateEquipmentBreakdown(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestBody @Valid EquipmentBreakdownRequest equipmentBreakdownRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentBreakdownResponse equipmentBreakdownResponse = equipmentBreakdownService.updateEquipmentBreakdown(equipmentBreakdownId, equipmentBreakdownRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + equipmentBreakdownResponse.getId() + " from updateEquipmentBreakdown.");
        return new ResponseEntity<>(equipmentBreakdownResponse, OK);
    }

    // 설비고장 삭제
    @DeleteMapping("/{equipment-breakdown-id}")
    @Operation(summary = "설비고장 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteEquipmentBreakdown(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비 id") Long equipmentBreakdownId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentBreakdownService.deleteEquipmentBreakdown(equipmentBreakdownId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + equipmentBreakdownId + " from deleteEquipmentBreakdown.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 설비고장 파일 생성
    @PutMapping(value = "/{equipment-breakdown-id}/files", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "설비고장 파일 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentBreakdownResponse> createFilesToEquipmentBreakdown(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestParam @Parameter(description = "수리전 false, 수리후 true") boolean fileDivision,
            @RequestPart List<MultipartFile> files,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, IOException, BadRequestException {
        EquipmentBreakdownResponse equipmentBreakdownResponse = equipmentBreakdownService.createFilesToEquipmentBreakdown(equipmentBreakdownId, fileDivision, files);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + equipmentBreakdownResponse.getId() + " from createFilesToEquipmentBreakdown.");
        return new ResponseEntity<>(equipmentBreakdownResponse, OK);
    }

    // 설비고장 파일 삭제
    @DeleteMapping("/{equipment-breakdown-id}/files/{file-id}")
    @ResponseBody
    @Operation(summary = "설비고장 파일 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteFileToEquipmentBreakdown(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "file-id") @Parameter(description = "파일 id") Long fileId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentBreakdownService.deleteFileToEquipmentBreakdown(equipmentBreakdownId, fileId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + fileId + " from deleteFileToEquipmentBreakdown.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 설비 고장 파일 조회
    @GetMapping(value = "/{equipment-breakdown-id}/files")
    @ResponseBody
    @Operation(summary = "설비고장 파일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<EquipmentBreakdownFileResponse>> getFilesToEquipmentBreakdown(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestParam @Parameter(description = "수리전 false, 수리후 true") boolean fileDivision,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<EquipmentBreakdownFileResponse> fileResponse = equipmentBreakdownService.getFilesToEquipmentBreakdown(equipmentBreakdownId, fileDivision);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed from getFilesToEquipmentBreakdown.");
        return new ResponseEntity<>(fileResponse, OK);
    }

    // ============================================== 수리항목 ==============================================
    // 수리항목 생성
    @PostMapping("/{equipment-breakdown-id}/repair-items")
    @ResponseBody
    @Operation(summary = "수리항목 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairItemResponse> createRepairItem(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestParam @Parameter(description = "수리코드 id") Long repairCodeId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        RepairItemResponse repairItemResponse = equipmentBreakdownService.createRepairItem(equipmentBreakdownId, repairCodeId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + repairItemResponse.getId() + " from createRepairItem.");
        return new ResponseEntity<>(repairItemResponse, OK);
    }

    // 수리항목 전체 조회
    @GetMapping("/{equipment-breakdown-id}/repair-items")
    @ResponseBody
    @Operation(summary = "수리항목 전체 조회", description = "")
    public ResponseEntity<List<RepairItemResponse>> getRepairItems(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<RepairItemResponse> repairItemResponses = equipmentBreakdownService.getRepairItemResponses(equipmentBreakdownId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list from getRepairItems.");
        return new ResponseEntity<>(repairItemResponses, OK);
    }

    // 수리항목 단일 조회
    @GetMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}")
    @ResponseBody
    @Operation(summary = "수리항목 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<RepairItemResponse> getRepairItem(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairItemResponse repairItemResponse = equipmentBreakdownService.getRepairItemResponse(equipmentBreakdownId, repairItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + repairItemResponse.getId() + " from getRepairItem.");
        return new ResponseEntity<>(repairItemResponse, OK);
    }

    // 수리항목 수정
    @PatchMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}")
    @ResponseBody
    @Operation(summary = "수리항목 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairItemResponse> updateRepairItem(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @RequestParam @Parameter(description = "수리코드 id") Long repairCodeId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        RepairItemResponse repairItemResponse = equipmentBreakdownService.updateRepairItem(equipmentBreakdownId, repairItemId, repairCodeId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + repairItemResponse.getId() + " from updateRepairItem.");
        return new ResponseEntity<>(repairItemResponse, OK);
    }

    // 수리항목 삭제
    @DeleteMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}")
    @ResponseBody()
    @Operation(summary = "수리항목 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteRepairItem(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentBreakdownService.deleteRepairItem(equipmentBreakdownId, repairItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + repairItemId + " from deleteRepairItem.");
        return new ResponseEntity(NO_CONTENT);
    }

    // ============================================== 수리부품정보 ==============================================
    // 수리부품 생성
    @PostMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}/repair-parts")
    @ResponseBody
    @Operation(summary = "수리부품 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairPartResponse> createRepairPart(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @Valid @RequestBody RepairPartRequest repairPartRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairPartResponse repairPartResponse = equipmentBreakdownService.createRepairPart(equipmentBreakdownId, repairItemId, repairPartRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + repairPartResponse.getId() + " from createRepairPart.");
        return new ResponseEntity<>(repairPartResponse, OK);
    }

    // 수리부품 리스트 조회
    @GetMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}/repair-parts")
    @ResponseBody
    @Operation(summary = "수리부품 전체 조회", description = "검색조건: 수리부품그룹, 수리부품계정, 품번, 품명, 검색어")
    public ResponseEntity<List<RepairPartResponse>> getRepairParts(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<RepairPartResponse> repairPartResponses = equipmentBreakdownService.getRepairPartResponses(equipmentBreakdownId, repairItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list from getRepairParts.");
        return new ResponseEntity<>(repairPartResponses, OK);
    }

    // 수리부품 단일 조회
    @GetMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}/repair-parts/{repair-part-id}")
    @ResponseBody()
    @Operation(summary = "수리부품 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<RepairPartResponse> getRepairPart(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @PathVariable(value = "repair-part-id") @Parameter(description = "수리부품 id") Long repairPartId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairPartResponse repairPartResponse = equipmentBreakdownService.getRepairPartResponse(equipmentBreakdownId, repairItemId, repairPartId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + repairPartResponse.getId() + " from getRepairPart.");
        return new ResponseEntity<>(repairPartResponse, OK);
    }

    // 수리부품 수정
    @PatchMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}/repair-parts/{repair-part-id}")
    @ResponseBody()
    @Operation(summary = "수리부품 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairPartResponse> updateRepairPart(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @PathVariable(value = "repair-part-id") @Parameter(description = "수리부품 id") Long repairPartId,
            @RequestBody @Valid RepairPartRequest repairPartRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairPartResponse repairPartResponse = equipmentBreakdownService.updateRepairPart(equipmentBreakdownId, repairItemId, repairPartId, repairPartRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + repairPartResponse.getId() + " from updateRepairPart.");
        return new ResponseEntity<>(repairPartResponse, OK);
    }

    // 수리부품 삭제
    @DeleteMapping("/{equipment-breakdown-id}/repair-items/{repair-worker-id}/repair-parts/{repair-part-id}")
    @ResponseBody()
    @Operation(summary = "수리부품 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteRepairPart(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리항목 id") Long repairItemId,
            @PathVariable(value = "repair-part-id") @Parameter(description = "수리부품 id") Long repairPartId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentBreakdownService.deleteRepairPart(equipmentBreakdownId,repairItemId, repairPartId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + repairPartId + " from deleteRepairPart.");
        return new ResponseEntity(NO_CONTENT);
    }

    // ============================================== 수리작업자 정보 ==============================================
    // 수리작업자 생성
    @PostMapping("/{equipment-breakdown-id}/repair-workers")
    @ResponseBody
    @Operation(summary = "수리작업자 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairWorkerResponse> createRepairWorker(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestParam @Parameter(description = "작업자 id") Long userId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairWorkerResponse repairWorkerResponse = equipmentBreakdownService.createRepairWorker(equipmentBreakdownId, userId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + repairWorkerResponse.getId() + " from createRepairWorker.");
        return new ResponseEntity<>(repairWorkerResponse, OK);
    }

    // 수리작업자 전체 조회
    @GetMapping("/{equipment-breakdown-id}/repair-workers")
    @ResponseBody
    @Operation(summary = "수리작업자 전체 조회", description = "")
    public ResponseEntity<List<RepairWorkerResponse>> getRepairWorkers(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<RepairWorkerResponse> repairWorkerResponses = equipmentBreakdownService.getRepairWorkerResponses(equipmentBreakdownId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list from getRepairWorkers.");
        return new ResponseEntity<>(repairWorkerResponses, OK);
    }

    // 수리작업자 단일 조회
    @GetMapping("/{equipment-breakdown-id}/repair-workers/{repair-worker-id}")
    @ResponseBody
    @Operation(summary = "수리작업자 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<RepairWorkerResponse> getRepairWorker(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리작업자 id") Long repairWorkerId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairWorkerResponse repairWorkerResponse = equipmentBreakdownService.getRepairWorkerResponse(equipmentBreakdownId, repairWorkerId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + repairWorkerResponse.getId() + " from getRepairWorker.");
        return new ResponseEntity<>(repairWorkerResponse, OK);
    }

    // 수리작업자 수정
    @PatchMapping("/{equipment-breakdown-id}/repair-workers/{repair-worker-id}")
    @ResponseBody
    @Operation(summary = "수리작업자 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RepairWorkerResponse> updateRepairWorker(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리작업자 id") Long repairWorkerId,
            @RequestParam @Parameter(description = "작업자  id") Long userId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        RepairWorkerResponse repairWorkerResponse = equipmentBreakdownService.updateRepairWorker(equipmentBreakdownId, repairWorkerId, userId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + repairWorkerResponse.getId() + " from updateRepairWorker.");
        return new ResponseEntity<>(repairWorkerResponse, OK);
    }

    // 수리작업자 삭제
    @DeleteMapping("/{equipment-breakdown-id}/repair-workers/{repair-worker-id}")
    @ResponseBody()
    @Operation(summary = "수리작업자 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteRepairWorker(
            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
            @PathVariable(value = "repair-worker-id") @Parameter(description = "수리작업자 id") Long repairWorkerId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentBreakdownService.deleteRepairWorker(equipmentBreakdownId, repairWorkerId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + repairWorkerId + " from deleteRepairWorker.");
        return new ResponseEntity(NO_CONTENT);
    }

}
