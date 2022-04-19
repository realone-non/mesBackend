package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EquipmentMaintenanceRequest;
import com.mes.mesBackend.dto.response.EquipmentMaintenanceResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.EquipmentMaintenanceService;
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
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 3-5-2. 설비 보전항목 등록
@Tag(name = "equipment-maintenance", description = "설비 보전항목 API")
@RequestMapping("/equipment-maintenances")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class EquipmentMaintenanceController {
    private final EquipmentMaintenanceService equipmentMaintenanceService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(EquipmentMaintenanceController.class);
    private CustomLogger cLogger;

    // 설비 보전항목 생성
    @Operation(summary = "설비 보전항목 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentMaintenanceResponse> createEquipmentMaintenance(
            @RequestBody @Valid EquipmentMaintenanceRequest equipmentMaintenanceRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        EquipmentMaintenanceResponse equipmentMaintenance = equipmentMaintenanceService.createEquipmentMaintenance(equipmentMaintenanceRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + equipmentMaintenance.getId() + " from createEquipmentMaintenance.");
        return new ResponseEntity<>(equipmentMaintenance, OK);
    }

    // 설비 보전항목 단일 조회
    @Operation(summary = "설비 보전항목 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<EquipmentMaintenanceResponse> getEquipmentMaintenance(
            @PathVariable @Parameter(description = "설비 보전항목 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentMaintenanceResponse equipmentMaintenance = equipmentMaintenanceService.getEquipmentMaintenance(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + equipmentMaintenance.getId() + " from getEquipmentMaintenance.");
        return new ResponseEntity<>(equipmentMaintenance, OK);
    }

    // 설비 보전항목 전체 조회
    @Operation(summary = "설비 보전항목 전체 조회", description = "")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EquipmentMaintenanceResponse>> getEquipmentMaintenances(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<EquipmentMaintenanceResponse> equipmentMaintenances = equipmentMaintenanceService.getEquipmentMaintenances();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getEquipmentMaintenances.");
        return new ResponseEntity<>(equipmentMaintenances, OK);
    }

    // 설비 보전항목 수정
    @Operation(summary = "설비 보전항목 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentMaintenanceResponse> updateEquipmentMaintenance(
            @PathVariable @Parameter(description = "설비 보전항목 id") Long id,
            @RequestBody @Valid EquipmentMaintenanceRequest equipmentMaintenanceRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        EquipmentMaintenanceResponse equipmentMaintenance = equipmentMaintenanceService.updateEquipmentMaintenance(id, equipmentMaintenanceRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is modified the " + equipmentMaintenance.getId() + " from updateEquipmentMaintenance.");
        return new ResponseEntity<>(equipmentMaintenance, OK);
    }

    // 설비 보전항목 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "설비 보전항목 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteEquipmentMaintenance(
            @PathVariable @Parameter(description = "설비 보전항목 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentMaintenanceService.deleteEquipmentMaintenance(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteEquipmentMaintenance.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 설비 보전항목 페이징 조회
//    @Operation(summary = "설비 보전항목 페이징 조회", description = "")
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
//    public ResponseEntity<Page<EquipmentMaintenanceResponse>> getEquipmentMaintenances(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(equipmentMaintenanceService.getEquipmentMaintenances(pageable), OK);
//    }
}
