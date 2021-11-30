package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EquipmentMaintenanceRequest;
import com.mes.mesBackend.dto.response.EquipmentMaintenanceResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.EquipmentMaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 3-5-2. 설비 보전항목 등록
@Tag(name = "equipment-maintenance", description = "설비 보전항목 API")
@RequestMapping("/equipment-maintenances")
@RestController
public class EquipmentMaintenanceController {
    @Autowired
    EquipmentMaintenanceService equipmentMaintenanceService;

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
            @RequestBody @Valid EquipmentMaintenanceRequest equipmentMaintenanceRequest
    ) {
        return new ResponseEntity<>(equipmentMaintenanceService.createEquipmentMaintenance(equipmentMaintenanceRequest), HttpStatus.OK);
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
            @PathVariable @Parameter(description = "설비 보전항목 id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(equipmentMaintenanceService.getEquipmentMaintenance(id), HttpStatus.OK);
    }

    // 설비 보전항목 전체 조회
    @Operation(summary = "설비 보전항목 전체 조회", description = "")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EquipmentMaintenanceResponse>> getEquipmentMaintenances() {
        return new ResponseEntity<>(equipmentMaintenanceService.getEquipmentMaintenances(), HttpStatus.OK);
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
            @RequestBody @Valid EquipmentMaintenanceRequest equipmentMaintenanceRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(equipmentMaintenanceService.updateEquipmentMaintenance(id, equipmentMaintenanceRequest), HttpStatus.OK);
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
    public ResponseEntity deleteEquipmentMaintenance(@PathVariable @Parameter(description = "설비 보전항목 id") Long id) throws NotFoundException {
        equipmentMaintenanceService.deleteEquipmentMaintenance(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
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
//        return new ResponseEntity<>(equipmentMaintenanceService.getEquipmentMaintenances(pageable), HttpStatus.OK);
//    }
}
