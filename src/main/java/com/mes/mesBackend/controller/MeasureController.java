package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.MeasureRequest;
import com.mes.mesBackend.dto.response.MeasureResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.MeasureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 3-5-3. 계측기 등록
@Tag(name = "measure", description = "계측기 API")
@RequestMapping("/measures")
@RestController
@SecurityRequirement(name = "Authorization")
public class MeasureController {
    @Autowired
    MeasureService measureService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(MeasureController.class);
    private CustomLogger cLogger;

    // 계측기 생성
    @Operation(summary = "계측기 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MeasureResponse> createMeasure(
            @RequestBody @Valid MeasureRequest measureRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        MeasureResponse measure = measureService.createMeasure(measureRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + measure.getId() + " from createMeasure.");
        return new ResponseEntity<>(measure, HttpStatus.OK);
    }

    // 계측기 단일 조회
    @Operation(summary = "계측기 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<MeasureResponse> getMeasure(
            @PathVariable @Parameter(description = "계측기 id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        MeasureResponse measure = measureService.getMeasure(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + measure.getId() + " from getMeasure.");
        return new ResponseEntity<>(measure, HttpStatus.OK);
    }

    // 계측기 전체 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
    @Operation(summary = "계측기 전체 조회", description = "검색조건: GAUGE유형, 검교정대상(월)")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<MeasureResponse>> getMeasures(
            @RequestParam(required = false) @Parameter(description = "GAUGE유형 id") Long gaugeId,
            @RequestParam(required = false) @Parameter(description = "월") Long month,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<MeasureResponse> measures = measureService.getMeasures(gaugeId, month);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of gaugeId: " + gaugeId + " from getMeasures.");
        return new ResponseEntity<>(measures, HttpStatus.OK);
    }

    // 계측기 수정
    @Operation(summary = "계측기 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MeasureResponse> updateMeasure(
            @PathVariable @Parameter(description = "계측기 id") Long id,
            @RequestBody @Valid MeasureRequest measureRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        MeasureResponse measure = measureService.updateMeasure(id, measureRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + measure.getId() + " from updateMeasure.");
        return new ResponseEntity<>(measure, HttpStatus.OK);
    }

    // 계측기 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "계측기 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteMeasure(
            @PathVariable @Parameter(description = "계측기 id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        measureService.deleteMeasure(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteMeasure.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 계측기 페이징 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
//    @Operation(summary = "계측기 페이징 조회", description = "검색조건: GAUGE유형, 검교정대상(월)")
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
//    public ResponseEntity<Page<MeasureResponse>> getMeasures(
//            @RequestParam(required = false) @Parameter(description = "GAUGE유형 id") Long gaugeId,
//            @RequestParam(required = false) @Parameter(description = "월") Long month,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(measureService.getMeasures(gaugeId, month, pageable), HttpStatus.OK);
//    }
}
