package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WareHouseTypeRequest;
import com.mes.mesBackend.dto.response.WareHouseTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WareHouseTypeService;
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

@Tag(name = "ware-house-type", description = "창고유형 API")
@RequestMapping(value = "/ware-house-types")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class WareHouseTypeController {
    @Autowired
    WareHouseTypeService wareHouseTypeService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(WareHouseTypeController.class);
    private CustomLogger cLogger;

    // 창고유형 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "창고유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WareHouseTypeResponse> createWareHouseType(
            @RequestBody @Valid WareHouseTypeRequest wareHouseTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        WareHouseTypeResponse wareHouseType = wareHouseTypeService.createWareHouseType(wareHouseTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + wareHouseType.getId() + " from createWareHouseType.");
        return new ResponseEntity<>(wareHouseType, HttpStatus.OK);
    }

    // 창고유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "창고유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<WareHouseTypeResponse> getWareHouseType(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WareHouseTypeResponse wareHouseType = wareHouseTypeService.getWareHouseType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + wareHouseType.getId() + " from getWareHouseType.");
        return new ResponseEntity<>(wareHouseType, HttpStatus.OK);
    }

    // 창고유형 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "창고유형 전체 조회")
    public ResponseEntity<List<WareHouseTypeResponse>> getWareHouseTypes(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<WareHouseTypeResponse> wareHouseTypes = wareHouseTypeService.getWareHouseTypes();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWareHouseTypes.");
        return new ResponseEntity<>(wareHouseTypes, HttpStatus.OK);
    }

    // 창고유형 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "창고유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WareHouseTypeResponse> updateWareHouseType(
            @PathVariable Long id,
            @RequestBody @Valid WareHouseTypeRequest wareHouseTypeRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WareHouseTypeResponse houseType = wareHouseTypeService.updateWareHouseType(id, wareHouseTypeRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + houseType.getId() + " from updateWareHouseType.");
        return new ResponseEntity<>(houseType, HttpStatus.OK);
    }

    // 창고유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "창고유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteWareHouseType(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        wareHouseTypeService.deleteWareHouseType(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteWareHouseType.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 창고유형 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "창고유형 페이징 조회")
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
//    public ResponseEntity<Page<WareHouseTypeResponse>> getWareHouseTypes(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(wareHouseTypeService.getWareHouseTypes(pageable), HttpStatus.OK);
//    }
}
