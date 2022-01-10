//package com.mes.mesBackend.controller;
//
//import com.mes.mesBackend.dto.request.EquipmentRequest;
//import com.mes.mesBackend.dto.request.ItemRequest;
//import com.mes.mesBackend.dto.response.EquipmentResponse;
//import com.mes.mesBackend.dto.response.ItemFileResponse;
//import com.mes.mesBackend.dto.response.ItemResponse;
//import com.mes.mesBackend.exception.BadRequestException;
//import com.mes.mesBackend.exception.NotFoundException;
//import com.mes.mesBackend.logger.CustomLogger;
//import com.mes.mesBackend.logger.LogService;
//import com.mes.mesBackend.logger.MongoLogger;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.validation.Valid;
//import java.io.IOException;
//import java.util.List;
//
//import static org.springframework.http.HttpStatus.NO_CONTENT;
//import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
//
//// 17-2. 설비 고장 수리내역 등록
//@Tag(name = "equipment-breakdown-repair", description = "17-2. 설비 고장 수리내역 등록 API")
//@RequestMapping("/equipment-breakdown-repairs")
//@RestController
//@SecurityRequirement(name = "Authorization")
//@RequiredArgsConstructor
//public class EquipmentBreakdownRepairController {
//    private final EquipmentBreakdownService equipmentBreakdownService;
//    private final LogService logService;
//    private Logger logger = LoggerFactory.getLogger(EquipmentBreakdownRepairController.class);
//    private CustomLogger cLogger;
//
//    // 설비고장 생성
//    @Operation(summary = "설비고장 생성")
//    @PostMapping
//    @ResponseBody
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "success"),
//                    @ApiResponse(responseCode = "404", description = "not found resource"),
//                    @ApiResponse(responseCode = "400", description = "bad request")
//            }
//    )
//    public ResponseEntity<EquipmentBreakdownResponse> createEquipmentBreakdown(
//            @RequestBody @Valid EquipmentBreakdownRequest equipmentBreakdownRequest,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException {
//        EquipmentBreakdownResponse equipmentBreakdown = equipmentBreakdownService.createEquipmentBreakdown(equipmentBreakdownRequest);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + equipmentBreakdown.getId() + " from createEquipmentBreakdown.");
//        return new ResponseEntity<>(equipmentBreakdown, HttpStatus.OK);
//    }
//
//    // 설비고장 리스트 검색 조회, 검색조건: 작업장 id, 설비유형, 작업기간 fromDate~toDate
//    @Operation(summary = "설비고장 리스트 조회", description = "")
//    @GetMapping
//    @ResponseBody
//    public ResponseEntity<List<EquipmentBreakdownResponse>> getEquipmentBreakdowns(
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) {
//        List<EquipmentBreakdownResponse> equipmentBreakdowns = equipmentBreakdownService.getEquipmentBreakdowns();
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getEquipmentBreakdowns.");
//        return new ResponseEntity<>(equipmentBreakdowns, HttpStatus.OK);
//    }
//
//    // 설비고장 단일조회
//    @Operation(summary = "설비고장 단일 조회")
//    @GetMapping("/{equipment-breakdown-id}")
//    @ResponseBody
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "success"),
//                    @ApiResponse(responseCode = "404", description = "not found resource"),
//            }
//    )
//    public ResponseEntity<EquipmentBreakdownResponse> getEquipmentBreakdown(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException {
//        EquipmentBreakdownResponse equipmentBreakdown = equipmentBreakdownService.getEquipmentBreakdown(equipmentBreakdownId);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + equipmentBreakdown.getId() + " from getEquipmentBreakdown.");
//        return new ResponseEntity<>(equipmentBreakdown, HttpStatus.OK);
//    }
//
//    // 설비고장 수정
//    @Operation(summary = "설비고장 수정")
//    @PatchMapping("/{equipment-breakdown-id}")
//    @ResponseBody
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "success"),
//                    @ApiResponse(responseCode = "404", description = "not found resource"),
//                    @ApiResponse(responseCode = "400", description = "bad request")
//            }
//    )
//    public ResponseEntity<EquipmentBreakdownResponse> updateEquipmentBreakdown(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @RequestBody @Valid EquipmentBreakdownRequest equipmentBreakdownRequest,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException {
//        EquipmentBreakdownResponse equipmentBreakdown = equipmentBreakdownService.updateEquipmentBreakdown(equipmentBreakdownId, equipmentBreakdownRequest);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + equipmentBreakdown.getId() + " from updateEquipmentBreakdown.");
//        return new ResponseEntity<>(equipmentBreakdown, HttpStatus.OK);
//    }
//
//    // 설비고장 삭제
//    @DeleteMapping("/{equipment-breakdown-id}")
//    @Operation(summary = "설비고장 삭제")
//    @ResponseBody
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "204", description = "no content"),
//                    @ApiResponse(responseCode = "404", description = "not found resource")
//            }
//    )
//    public ResponseEntity deleteEquipmentBreakdown(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비 id") Long equipmentBreakdownId,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException {
//        equipmentBreakdownService.deleteEquipmentBreakdown(equipmentBreakdownId);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + equipmentBreakdownId + " from deleteEquipmentBreakdown.");
//        return new ResponseEntity(NO_CONTENT);
//    }
//
//    // 설비고장 수리전 파일 생성
//    @PutMapping(value = "/{equipment-breakdown-id}/files", consumes = MULTIPART_FORM_DATA_VALUE)
//    @ResponseBody
//    @Operation(summary = "설비고장 파일 생성")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "success"),
//                    @ApiResponse(responseCode = "404", description = "not found resource"),
//                    @ApiResponse(responseCode = "400", description = "bad request")
//            }
//    )
//    public ResponseEntity<EquipmentBreakdownResponse> createFilesToEquipmentBreakdown(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @RequestParam @Parameter(description = "수리전 false, 수리후 true") boolean fileDivision,
//            @RequestPart List<MultipartFile> files,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException, IOException, BadRequestException {
//        EquipmentBreakdownResponse equipmentBreakdown = equipmentBreakdownService.createFilesToEquipmentBreakdown(equipmentBreakdownId, files);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        // TODO(파일 아이디 가져오기)
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + equipmentBreakdown.getId() + " from createFilesToEquipmentBreakdown.");
//        return new ResponseEntity<>(equipmentBreakdown, HttpStatus.OK);
//    }
//
//    // 설비고장 파일 삭제
//    @DeleteMapping("/{equipment-breakdown-id}/files/{file-id}")
//    @ResponseBody
//    @Operation(summary = "품목 파일 삭제")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "204", description = "no content"),
//                    @ApiResponse(responseCode = "404", description = "not found resource")
//            }
//    )
//    public ResponseEntity deleteFileToEquipmentBreakdown(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @PathVariable(value = "file-id") @Parameter(description = "파일 id") Long fileId,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException {
//        equipmentBreakdownService.deleteFileToEquipmentBreakdown(equipmentBreakdownId, fileId);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + fileId + " from deleteFileToEquipmentBreakdown.");
//        return new ResponseEntity(NO_CONTENT);
//    }
//
//    // ============================================== 수리항목 ==============================================
//    // 수리항목 생성
//    @PostMapping("/{equipment-breakdown-id}/repair-items")
//    @ResponseBody
//    @Operation(summary = "수리항목 생성")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "success"),
//                    @ApiResponse(responseCode = "404", description = "not found resource"),
//                    @ApiResponse(responseCode = "400", description = "bad request")
//            }
//    )
//    public ResponseEntity<RepairItemResponse> createRepairItem(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @Valid @RequestBody RepairItemRequest repairItemRequest,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException, BadRequestException {
//        RepairItemResponse repairItem = equipmentBreakdownService.createRepairItem(equipmentBreakdownId, repairItemRequest);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + repairItem.getId() + " from createRepairItem.");
//        return new ResponseEntity<>(repairItem, HttpStatus.OK);
//    }
//
//    // 수리항목 전체 조회
//    @GetMapping("/{equipment-breakdown-id}/repair-items")
//    @ResponseBody
//    @Operation(summary = "수리항목 전체 조회", description = "")
//    public ResponseEntity<List<RepairItemResponse>> getRepairItems(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) {
//        List<RepairItemResponse> repairItems = equipmentBreakdownService.getRepairItems(equipmentBreakdownId);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list from getRepairItems.");
//        return new ResponseEntity<>(repairItems, HttpStatus.OK);
//    }
//
//    // 수리항목 단일 조회
//    @GetMapping("/{equipment-breakdown-id}/repair-items/{repair-item-id}")
//    @ResponseBody
//    @Operation(summary = "수리항목 단일 조회")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "success"),
//                    @ApiResponse(responseCode = "404", description = "not found resource"),
//            }
//    )
//    public ResponseEntity<RepairItemResponse> getRepairItem(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @PathVariable(value = "repair-item-id") @Parameter(description = "수리항목 id") Long repairItemId,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException {
//        RepairItemResponse repairItem = equipmentBreakdownService.getRepairItem(equipmentBreakdownId, repairItemId);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + repairItem.getId() + " from getRepairItem.");
//        return new ResponseEntity<>(repairItem, HttpStatus.OK);
//    }
//
//    // 수리항목 수정
//    @PatchMapping("/{equipment-breakdown-id}/repair-items/{repair-item-id}")
//    @ResponseBody
//    @Operation(summary = "수리항목 수정")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "success"),
//                    @ApiResponse(responseCode = "404", description = "not found resource"),
//                    @ApiResponse(responseCode = "400", description = "bad request")
//            }
//    )
//    public ResponseEntity<RepairItemResponse> updateRepairItem(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @PathVariable(value = "repair-item-id") @Parameter(description = "수리항목 id") Long repairItemId,
//            @RequestBody @Valid RepairItemRequest repairItemRequest,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException, BadRequestException {
//        RepairItemResponse repairItemResponse = equipmentBreakdownService.updateRepairItem(equipmentBreakdownId, repairItemId, repairItemRequest);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + repairItemResponse.getId() + " from updateRepairItem.");
//        return new ResponseEntity<>(repairItemResponse, HttpStatus.OK);
//    }
//
//    // 수리항목 삭제
//    @DeleteMapping("/{equipment-breakdown-id}/repair-items/{repair-item-id}")
//    @ResponseBody()
//    @Operation(summary = "수리항목 삭제")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "204", description = "no content"),
//                    @ApiResponse(responseCode = "404", description = "not found resource")
//            }
//    )
//    public ResponseEntity<Void> deleteRepairItem(
//            @PathVariable(value = "equipment-breakdown-id") @Parameter(description = "설비고장 id") Long equipmentBreakdownId,
//            @PathVariable(value = "repair-item-id") @Parameter(description = "수리항목 id") Long repairItemId,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) throws NotFoundException {
//        equipmentBreakdownService.deleteRepairItem(equipmentBreakdownId, repairItemId);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + repairItemId + " from deleteRepairItem.");
//        return new ResponseEntity(NO_CONTENT);
//    }
//
//    // ============================================== 수리부품정보 ==============================================
//    // 수리부품 생성
//
//    // 수리부품 리스트 조회
//    // 수리부품 단일 조회
//    // 수리부품 수정
//    // 수리부품 삭제
//
//    // ============================================== 수리작업자 정보 ==============================================
//    // TODO(협의 후 진행 작업보류)
//}
