package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.BomItemRequest;
import com.mes.mesBackend.dto.request.BomMasterRequest;
import com.mes.mesBackend.dto.response.BomItemDetailResponse;
import com.mes.mesBackend.dto.response.BomItemResponse;
import com.mes.mesBackend.dto.response.BomMasterResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.BomMasterService;
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

//5-2. 신제품 BOM 등록
@Tag(name = "new-product-bom-master", description = "신제품 BOM 등록 API")
@RequestMapping(value = "/new-product-bom-master")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class NewProductBomController {
    @Autowired
    BomMasterService bomMasterService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(BomMasterController.class);
    private CustomLogger cLogger;

    @Operation(summary = "BOM 마스터 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BomMasterResponse> createBomMaster(
            @RequestBody @Valid BomMasterRequest bomMasterRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BomMasterResponse bomMaster = bomMasterService.createBomMaster(bomMasterRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + bomMaster.getId() + " from createBomMaster.");
        return new ResponseEntity<>(bomMaster, HttpStatus.OK);
    }

    @Operation(summary = "BOM 마스터 단일 조회")
    @GetMapping("/{bom-master-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<BomMasterResponse> getBomMaster(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BomMasterResponse bomMaster = bomMasterService.getBomMaster(bomMasterId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + bomMaster.getId() + " from getBomMaster.");
        return new ResponseEntity<>(bomMaster, HttpStatus.OK);
    }

    @Operation(summary = "BOM 마스터 전체 조회", description = "검색조건: 품목계정, 품목그룹, 품번|품명")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<BomMasterResponse>> getBomMasters(
            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<BomMasterResponse> bomMasters = bomMasterService.getBomMasters(itemAccountId, itemGroupId, itemNoAndItemName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of itemAccountId: " +itemAccountId + ", itemGroupId : " + itemGroupId + " from getBomMasters.");
        return new ResponseEntity<>(bomMasters, HttpStatus.OK);
    }

    @Operation(summary = "BOM 마스터 수정")
    @PatchMapping("/{bom-master-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BomMasterResponse> updateBomMaster(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @RequestBody @Valid BomMasterRequest bomMasterRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BomMasterResponse bomMaster = bomMasterService.updateBomMaster(bomMasterId, bomMasterRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + bomMaster.getId() + " from updateBomMaster.");
        return new ResponseEntity<>(bomMaster, HttpStatus.OK);
    }

    @Operation(summary = "BOM 마스터 삭제")
    @DeleteMapping("/{bom-master-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteBomMaster(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        bomMasterService.deleteBomMaster(bomMasterId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + bomMasterId + " from deleteBomMaster.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "BOM 품목 생성")
    @PostMapping("/{bom-master-id}/bom-items")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BomItemResponse> createBomItem(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @RequestBody @Valid BomItemRequest bomMasterDetailRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BomItemResponse bomItem = bomMasterService.createBomItem(bomMasterId, bomMasterDetailRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + bomItem.getId() + " from createBomItem.");
        return new ResponseEntity<>(bomItem, HttpStatus.OK);
    }

    @Operation(summary = "BOM 품목 리스트 조회")
    @GetMapping("/{bom-master-id}/bom-items")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<List<BomItemDetailResponse>> getBomItems(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoOrItemName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<BomItemDetailResponse> bomItems = bomMasterService.getBomItems(bomMasterId, itemNoOrItemName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getBomItems.");
        return new ResponseEntity<>(bomItems, HttpStatus.OK);
    }

    @Operation(summary = "BOM 품목 수정")
    @PatchMapping("/{bom-master-id}/bom-items/{bom-item-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BomItemResponse> updateBomItem(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @PathVariable(value = "bom-item-id") @Parameter(description = "BOM 품목 id") Long bomItemId,
            @RequestBody @Valid BomItemRequest bomMasterDetailRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BomItemResponse bomItem = bomMasterService.updateBomItem(bomMasterId, bomItemId, bomMasterDetailRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + bomItem.getId() + " from updateBomItem.");
        return new ResponseEntity<>(bomItem, HttpStatus.OK);
    }

    @DeleteMapping("/{bom-master-id}/bom-items/{bom-item-id}")
    @Operation(summary = "BOM 품목 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteBomItem(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @PathVariable(value = "bom-item-id") @Parameter(description = "BOM 품목 id") Long bomItemId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String header
    ) throws NotFoundException {
        bomMasterService.deleteBomItem(bomMasterId, bomItemId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(header) + " is deleted the " + bomItemId + " from deleteBomItem.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{bom-master-id}/bom-items/{bom-item-id}")
    @Operation(summary = "BOM 품목 단일 조회")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<BomItemResponse> getBomItem(
            @PathVariable(value = "bom-master-id") @Parameter(description = "BOM 마스터 id") Long bomMasterId,
            @PathVariable(value = "bom-item-id") @Parameter(description = "BOM 품목 id") Long bomItemId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BomItemResponse bomItem = bomMasterService.getBomItem(bomMasterId, bomItemId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + bomItem.getId() + " from getBomItem.");
        return new ResponseEntity<>(bomItem, HttpStatus.OK);
    }

//    @Operation(summary = "BOM 마스터 페이징 조회", description = "검색조건: 품목계정, 품목그룹, 품번|품명")
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
//    public ResponseEntity<Page<BomMasterResponse>> getBomMasterPages(
//            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
//            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
//            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(bomMasterService.getBomMasters(itemAccountId, itemGroupId, itemNoAndItemName, pageable), HttpStatus.OK);
//    }
}
