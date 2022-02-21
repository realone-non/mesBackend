package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EquipmentRequest;
import com.mes.mesBackend.dto.response.EquipmentResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.EquipmentService;
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

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 3-5-1. 설비등록
@Tag(name = "equipment", description = "설비 API")
@RequestMapping("/equipments")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final LogService logService;

    private Logger logger = LoggerFactory.getLogger(EquipmentController.class);
    private CustomLogger cLogger;

    // 설비 생성
    @Operation(summary = "설비 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentResponse> createEquipment(
            @RequestBody @Valid EquipmentRequest equipmentRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentResponse equipment = equipmentService.createEquipment(equipmentRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + equipment.getId() + " from createEquipment.");
        return new ResponseEntity<>(equipment, OK);
    }

    // 설비 단일 조회
    @Operation(summary = "설비 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<EquipmentResponse> getEquipment(
            @PathVariable @Parameter(description = "설비 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentResponse equipment = equipmentService.getEquipment(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + equipment.getId() + " from getEquipment.");
        return new ResponseEntity<>(equipment, OK);
    }

    // 설비 전체 조회 검색조건: 설비명
    @Operation(summary = "설비 전체 조회", description = "")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EquipmentResponse>> getEquipments(
            @RequestParam(required = false) @Parameter(description = "설비명") String equipmentName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<EquipmentResponse> equipments = equipmentService.getEquipments(equipmentName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getEquipments.");
        return new ResponseEntity<>(equipments, OK);
    }

    // 설비 수정
    @Operation(summary = "설비 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable @Parameter(description = "설비 id") Long id,
            @RequestBody @Valid EquipmentRequest equipmentRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        EquipmentResponse equipment = equipmentService.updateEquipment(id, equipmentRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + equipment.getId() + " from updateEquipment.");
        return new ResponseEntity<>(equipment, OK);
    }

    // 설비 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "설비 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteEquipment(
            @PathVariable @Parameter(description = "설비 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        equipmentService.deleteEquipment(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteEquipment.");
        return new ResponseEntity(NO_CONTENT);
    }

//    // 설비 페이징 조회 검색조건: 설비명
//    @Operation(summary = "설비 페이징 조회", description = "")
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
//    public ResponseEntity<Page<EquipmentResponse>> getEquipments(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(equipmentService.getEquipments(pageable), OK);
//    }
}
