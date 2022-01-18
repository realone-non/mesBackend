package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.InputTestDetailRequest;
import com.mes.mesBackend.dto.response.InputTestDetailResponse;
import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.InputTestDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.InputTestDivision.OUT_SOURCING;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

// 14-2. 검사 등록
@RequestMapping(value = "/outsourcing-input-test-enrollments")
@Tag(name = "outsourcing-input-test-enrollment", description = "15-2. 검사등록 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class OutsourcingInputTestEnrollmentController {
    private final InputTestDetailService inputTestDetailService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(OutsourcingInputTestEnrollmentController.class);
    private CustomLogger cLogger;

    // 검사요청정보 리스트 조회
    // 검색조건: 창고 id, 품명|품목, 완료여부, 입고번호, 품목그룹 id, LOT 유형 id, 요청기간 from~toDate, 제조사 id
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "검사요청정보 리스트 조회",
            description = "검색조건: 창고 id, 품명|품목, 완료여부, 입고번호, 품목그룹 id, LOT 유형 id, 요청기간 from~toDate, 제조사 id")
    public ResponseEntity<List<InputTestRequestInfoResponse>> getOutsourcingInputTestRequestInfo(
            @RequestParam(required = false) @Parameter(description = "창고 id") Long warehouseId,
            @RequestParam(required = false) @Parameter(description = "품명|품번") String itemNoAndName,
            @RequestParam(required = false) @Parameter(description = "완료여부") Boolean completionYn,
            @RequestParam(required = false) @Parameter(description = "입고번호 (purchaseInputId)") Long purchaseInputNo,
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "LOT 유형 id") Long lotTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "요청기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "요청기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "제조사 id(clientId)") Long manufactureId,
            @RequestParam(required = false) @Parameter(description = "검사유형(삭제)", hidden = true) TestType testType,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<InputTestRequestInfoResponse> inputTestInfos = inputTestDetailService.getInputTestRequestInfo(
                warehouseId, itemNoAndName, completionYn, purchaseInputNo, itemGroupId, lotTypeId, fromDate, toDate, manufactureId, OUT_SOURCING, testType);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getOutsourcingInputTestRequestInfo.");
        return new ResponseEntity<>(inputTestInfos, HttpStatus.OK);
    }

    // 검사정보 생성
    @Operation(summary = "검사정보 생성", description = "")
    @PostMapping("/{input-test-request-id}/input-test-details")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<InputTestDetailResponse> createOutsourcingInputTestEnrollment(
            @PathVariable(value = "input-test-request-id") @Parameter(description = "검사의뢰 id") Long inputTestRequestId,
            @RequestBody @Valid InputTestDetailRequest inputTestDetailRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        InputTestDetailResponse inputTestDetail = inputTestDetailService.createInputTestDetail(inputTestRequestId, inputTestDetailRequest, OUT_SOURCING);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + inputTestDetail.getId() + " from createOutsourcingInputTestEnrollment.");
        return new ResponseEntity<>(inputTestDetail, HttpStatus.OK);
    }

    // 검사정보 전체조회
    @GetMapping("/{input-test-request-id}/input-test-details")
    @ResponseBody
    @Operation(summary = "검사정보 전체조회", description = "")
    public ResponseEntity<List<InputTestDetailResponse>> getOutsourcingInputTestEnrollments(
            @PathVariable(value = "input-test-request-id") @Parameter(description = "검사의뢰 id") Long inputTestRequestId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<InputTestDetailResponse> inputTestDetails = inputTestDetailService.getInputTestDetails(inputTestRequestId, OUT_SOURCING);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getOutsourcingInputTestEnrollments.");
        return new ResponseEntity<>(inputTestDetails, HttpStatus.OK);
    }

    // 검사정보 수정
    @PatchMapping("/{input-test-request-id}/input-test-details/{input-test-detail-id}")
    @ResponseBody()
    @Operation(summary = "검사정보 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<InputTestDetailResponse> updateOutsourcingInputTestEnrollment(
            @PathVariable(value = "input-test-request-id") @Parameter(description = "검사의뢰 id") Long inputTestRequestId,
            @PathVariable(value = "input-test-detail-id") @Parameter(description = "검사정보 id") Long inputTestDetailId,
            @RequestBody @Valid InputTestDetailRequest inputTestDetailRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        InputTestDetailResponse inputTestDetail = inputTestDetailService.updateInputTestDetail(
                inputTestRequestId, inputTestDetailId, inputTestDetailRequest, OUT_SOURCING
        );
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + inputTestDetail.getId() + " from updateOutsourcingInputTestEnrollment.");
        return new ResponseEntity<>(inputTestDetail, HttpStatus.OK);
    }

    // 검사정보 삭제
    @DeleteMapping("/{input-test-request-id}/input-test-details/{input-test-detail-id}")
    @ResponseBody()
    @Operation(summary = "검사정보 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteOutsourcingInputTestEnrollment(
            @PathVariable(value = "input-test-request-id") @Parameter(description = "검사의뢰 id") Long inputTestRequestId,
            @PathVariable(value = "input-test-detail-id") @Parameter(description = "검사정보 id") Long inputTestDetailId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        inputTestDetailService.deleteInputTestDetail(inputTestRequestId, inputTestDetailId, OUT_SOURCING);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + inputTestDetailId + " from deleteOutsourcingInputTestEnrollment.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 검사성적서 파일
    @PutMapping(value = "/{input-test-request-id}/input-test-details/{input-test-detail-id}/test-report-files", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "검사성적서 파일 추가")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<InputTestDetailResponse> createTestReportFileToOutsourcingInputTestEnrollment(
            @PathVariable(value = "input-test-request-id") @Parameter(description = "검사의뢰 id") Long inputTestRequestId,
            @PathVariable(value = "input-test-detail-id") @Parameter(description = "검사정보 id") Long inputTestDetailId,
            @RequestPart(required = false) @Parameter(description = "검사성적서 파일") MultipartFile testReportFile,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, IOException, BadRequestException {
        InputTestDetailResponse inputTestDetail = inputTestDetailService.createTestReportFileToInputTestDetail(inputTestRequestId, inputTestDetailId, testReportFile, OUT_SOURCING);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + inputTestDetail.getId() + " from createTestReportFileToOutsourcingInputTestEnrollment.");
        return new ResponseEntity<>(inputTestDetail, HttpStatus.OK);
    }

    // COC 파일 추가
    @PutMapping(value = "/{input-test-request-id}/input-test-details/{input-test-detail-id}/coc-files", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "COC 파일 추가")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<InputTestDetailResponse> createCocFileToOutsourcingInputTestEnrollment(
            @PathVariable(value = "input-test-request-id") @Parameter(description = "검사의뢰 id") Long inputTestRequestId,
            @PathVariable(value = "input-test-detail-id") @Parameter(description = "검사정보 id") Long inputTestDetailId,
            @RequestPart(required = false) @Parameter(description = "COC 파일") MultipartFile cocFile,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, IOException, BadRequestException {
        InputTestDetailResponse inputTestDetail = inputTestDetailService.createCocFileToInputTestDetail(inputTestRequestId, inputTestDetailId, cocFile, OUT_SOURCING);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + inputTestDetail.getId() + " from createCocFileToOutsourcingInputTestEnrollment.");
        return new ResponseEntity<>(inputTestDetail, HttpStatus.OK);
    }

    // 파일 삭제
    @DeleteMapping("/{input-test-request-id}/input-test-details/{input-test-detail-id}/files")
    @ResponseBody()
    @Operation(summary = "검사정보 파일 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteTestReportFileToOutsourcingInputTestEnrollment(
            @PathVariable(value = "input-test-request-id") @Parameter(description = "검사의뢰 id") Long inputTestRequestId,
            @PathVariable(value = "input-test-detail-id") @Parameter(description = "검사정보 id") Long inputTestDetailId,
            @RequestParam(required = false) @Parameter(description = "검사성적서 파일") boolean testReportDeleteYn,
            @RequestParam(required = false) @Parameter(description = "coc 파일") boolean cocDeleteYn,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        inputTestDetailService.deleteTestReportFileAndCocFileToInputTestDetail(inputTestRequestId, inputTestDetailId, testReportDeleteYn, cocDeleteYn, OUT_SOURCING);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + inputTestDetailId + " from deleteTestReportFileToOutsourcingInputTestEnrollment.");
        return new ResponseEntity(NO_CONTENT);
    }
}
