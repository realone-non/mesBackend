package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.MeasureCalibrationRequest;
import com.mes.mesBackend.dto.response.MeasureCalibrationResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.MeasureCalibrationService;
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

// 17-5. 계측기 검교정 실적 등록
@Tag(name = "measure-calibration", description = "17-5. 계측기 검교정 실적 등록 API")
@RequestMapping("/measure-calibrations")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class MeasureCalibrationController {
    private final MeasureCalibrationService measureCalibrationService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(MeasureCalibrationController.class);
    private CustomLogger cLogger;

    // 계측기 검교정 실적 생성
    @Operation(summary = "계측기 검교정 실적 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MeasureCalibrationResponse> createMeasureCalibration(
            @RequestBody @Valid MeasureCalibrationRequest measureCalibrationRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        MeasureCalibrationResponse measureCalibration = measureCalibrationService.createMeasureCalibration(measureCalibrationRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + measureCalibration.getId() + " from createMeasureCalibration.");
        return new ResponseEntity<>(measureCalibration, OK);
    }

    // 계측기 검교정 실적 단일 조회
    @Operation(summary = "계측기 검교정 실적 단일 조회", description = "")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<MeasureCalibrationResponse> getMeasureCalibration(
            @PathVariable @Parameter(description = "계측기 검교정 실적 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        MeasureCalibrationResponse measureCalibration = measureCalibrationService.getMeasureCalibrationResponse(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + measureCalibration.getId() + " from getMeasureCalibration.");
        return new ResponseEntity<>(measureCalibration, OK);
    }

    // 계측기 검교정 실적 리스트 검색 조회, 검색조건: 검정처(부서 id), 측정기유형(계측기유형), 검정기간(검교정일자) fromDate~toDate
    @Operation(summary = "계측기 검교정 실적 전체 조회", description = "검색조건: 검정처(부서 id), 측정기유형(계측기유형 id), 검정기간(검교정일자) fromDate~toDate")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<MeasureCalibrationResponse>> getMeasureCalibrations(
            @RequestParam(required = false) @Parameter(description = "검정처(부서 id)") Long departmentId,
            @RequestParam(required = false) @Parameter(description = "측정기유형(계측기유형)") Long gaugeTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "검정기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "검정기간 toDate") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<MeasureCalibrationResponse> measureCalibrations = measureCalibrationService.getMeasureCalibrations(departmentId, gaugeTypeId, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getMeasureCalibrations.");
        return new ResponseEntity<>(measureCalibrations, OK);
    }

    // 계측기 검교정 실적 수정
    @Operation(summary = "계측기 검교정 실적 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MeasureCalibrationResponse> updateMeasureCalibration(
            @PathVariable @Parameter(description = "계측기 검교정 실적 id") Long id,
            @RequestBody @Valid MeasureCalibrationRequest measureCalibrationRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        MeasureCalibrationResponse measureCalibration = measureCalibrationService.updateMeasureCalibration(id, measureCalibrationRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + measureCalibration.getId() + " from updateMeasureCalibration.");
        return new ResponseEntity<>(measureCalibration, OK);
    }

    // 계측기 검교정 실적 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "계측기 검교정 실적 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteMeasureCalibration(
            @PathVariable @Parameter(description = "계측기 검교정 실적 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        measureCalibrationService.deleteMeasureCalibration(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteMeasureCalibration.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 계측기 검교정 성적서 파일 생성
    @PutMapping(value = "/{id}/report-files", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "계측기 검교정 성적서 파일 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MeasureCalibrationResponse> createFilesToMeasureCalibration(
            @PathVariable(value = "id") @Parameter(description = "계측기 검교정 실적 id") Long id,
            @RequestPart MultipartFile file,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, IOException, BadRequestException {
        MeasureCalibrationResponse measureCalibration = measureCalibrationService.createFilesToMeasureCalibration(id, file);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + measureCalibration.getId() + " from createFilesToMeasureCalibration.");
        return new ResponseEntity<>(measureCalibration, OK);
    }

    // 계측기 검교정 성적서 파일 삭제
    @DeleteMapping("/{id}/report-files")
    @ResponseBody
    @Operation(summary = "계측기 검교정 성적서 파일 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteFileToMeasureCalibration(
            @PathVariable(value = "id") @Parameter(description = "계측기 검교정 실적 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        measureCalibrationService.deleteFileToMeasureCalibration(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteFileToMeasureCalibration.");
        return new ResponseEntity(NO_CONTENT);
    }
}
