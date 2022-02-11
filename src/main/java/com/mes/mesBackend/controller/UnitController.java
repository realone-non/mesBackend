package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UnitRequest;
import com.mes.mesBackend.dto.response.UnitResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.UnitService;
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

@Tag(name = "unit", description = "단위 API")
@RequestMapping(value = "/units")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class UnitController {
    private final UnitService unitService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(UnitController.class);
    private CustomLogger cLogger;

    // 단위 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "단위 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UnitResponse> createUnit(
            @RequestBody @Valid UnitRequest unitRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        UnitResponse unit = unitService.createUnit(unitRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + unit.getId() + " from createUnit.");
        return new ResponseEntity<>(unit, OK);
    }

    // 단위 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "단위 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<UnitResponse> getUnit(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        UnitResponse unit = unitService.getUnit(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + unit.getId() + " from getUnit.");
        return new ResponseEntity<>(unit, OK);
    }

    // 단위 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "단위 전체 조회")
    public ResponseEntity<List<UnitResponse>> getUnits(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<UnitResponse> units = unitService.getUnits();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getUnits.");
        return new ResponseEntity<>(units, OK);
    }

    // 단위 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "단위 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UnitResponse> updateUnit(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid UnitRequest unitRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        UnitResponse unit = unitService.updateUnit(id, unitRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + unit.getId() + " from updateUnit.");
        return new ResponseEntity<>(unit, OK);
    }

    // 단위 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "단위 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteUnit(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        unitService.deleteUnit(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteUnit.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 단위 페이징 조회
//    @GetMapping
//    @ResponseBody
//    @Operation(summary = "단위 페이징 조회")
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
//    public ResponseEntity<Page<UnitResponse>> getUnits(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(unitService.getUnits(pageable), OK);
//    }
}
