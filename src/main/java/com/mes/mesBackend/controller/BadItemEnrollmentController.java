package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.BadItemWorkOrderResponse;
import com.mes.mesBackend.dto.response.WorkOrderDetailBadItemResponse;
import com.mes.mesBackend.dto.response.WorkOrderDetailResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.BadItemEnrollmentService;
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

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 8-5. 불량 등록
@RequestMapping("/bad-item-enrollment-work-orders")
@Tag(name = "bad-item-enrollment", description = "8-5. 불량 등록 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class BadItemEnrollmentController {
    private final BadItemEnrollmentService badItemEnrollmentService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(BadItemEnrollmentController.class);
    private CustomLogger cLogger;

    // 작업지시 정보 리스트 조회, 검색조건: 작업장 id, 작업라인 id, 품목그룹 id, 제조오더번호, JOB NO, 작업기간 fromDate~toDate, 품번|품목
    @Operation(
            summary = "작업지시 정보 리스트 조회(지시상태 완료, 진행중만 조회)",
            description = "현재 완료, 진행중인 작업지시만 조회, 검색조건: 작업장 id, 작업라인 id, 품목그룹 id, 제조오더번호, JOB NO, 작업기간 fromDate~toDate, 품번|품목"
    )
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<BadItemWorkOrderResponse>> getWorkOrders(
            @RequestParam(required = false) @Parameter(description = "[보류]작업장 id") Long workCenterId,
            @RequestParam(required = false) @Parameter(description = "작업라인 id") Long workLineId,
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "제조오더번호") String produceOrderNo,
            @RequestParam(required = false) @Parameter(description = "JOB NO(작업지시번호)") String workOrderNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "작업기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "품번|품목") String itemNoAndItemName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<BadItemWorkOrderResponse> badItemWorkOrderResponses =
                badItemEnrollmentService.getWorkOrders(workCenterId, workLineId, itemGroupId, produceOrderNo, workOrderNo, fromDate, toDate, itemNoAndItemName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrders.");
        return new ResponseEntity<>(badItemWorkOrderResponses, OK);
    }

    // 작업지시 별 작업완료 상세 리스트
    @Operation(summary = "작업지시 별 작업완료 상세 리스트", description = "")
    @GetMapping("/{work-order-id}/details")
    @ResponseBody
    public ResponseEntity<List<WorkOrderDetailResponse>> getWorkOrderDetails(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<WorkOrderDetailResponse> responses = badItemEnrollmentService.getWorkOrderDetails(workOrderId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrderDetails.");
        return new ResponseEntity<>(responses, OK);
    }

    // 작업완료 상세 리스트 별 불량정보 조회
    @Operation(summary = "작업지시 별 불량정보 조회", description = "")
    @GetMapping("/{work-order-id}/details/{equipment-lot-id}")
    @ResponseBody
    public ResponseEntity<List<WorkOrderDetailBadItemResponse>> getBadItemEnrollments(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @PathVariable(value = "equipment-lot-id") @Parameter(description = "설비 lot id") Long equipmentLotId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<WorkOrderDetailBadItemResponse> responses = badItemEnrollmentService.getBadItemEnrollments(workOrderId, equipmentLotId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrderDetailBadItems.");
        return new ResponseEntity<>(responses, OK);
    }


    // 작업완료 상세 리스트 별 불량정보 생성
    @Operation(summary = "불량정보 생성")
    @PostMapping("/{work-order-id}/details/{equipment-lot-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkOrderDetailBadItemResponse> createBadItemEnrollment(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @PathVariable(value = "equipment-lot-id") @Parameter(description = "설비 lot id") Long equipmentLotId,
            @RequestParam @Parameter(description = "불량항목 id") Long badItemId,
            @RequestParam @Parameter(description = "불량수량") int badItemAmount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetailBadItemResponse responses = badItemEnrollmentService.createBadItemEnrollment(workOrderId, equipmentLotId, badItemId, badItemAmount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the  from createBadItemEnrollment.");
        return new ResponseEntity<>(responses, OK);
    }

    // 작업완료 상세 리스트 별 불량정보 수정
    @Operation(summary = "불량정보 수정")
    @PatchMapping("/{work-order-id}/details/{equipment-lot-id}/bad-item-enrollments/{bad-item-enrollment-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkOrderDetailBadItemResponse> updateBadItemEnrollment(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @PathVariable(value = "equipment-lot-id") @Parameter(description = "설비 LOT id") Long equipmentLotId,
            @PathVariable(value = "bad-item-enrollment-id") @Parameter(description = "불량 id") Long badItemEnrollmentId,
            @RequestParam @Parameter(description = "불량수량") int badItemAmount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetailBadItemResponse response = badItemEnrollmentService.updateBadItemEnrollment(workOrderId, equipmentLotId, badItemEnrollmentId, badItemAmount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + badItemEnrollmentId + " from updateBadItemEnrollment.");
        return new ResponseEntity<>(response, OK);
    }

    // 작업완료 상세 리스트 별 불량정보 삭제
    @DeleteMapping("/{work-order-id}/details/{equipment-lot-id}/bad-item-enrollments/{bad-item-enrollment-id}")
    @Operation(summary = "불량 정보 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteBadItemEnrollment(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @PathVariable(value = "equipment-lot-id") @Parameter(description = "설비 LOT id") Long equipmentLotId,
            @PathVariable(value = "bad-item-enrollment-id") @Parameter(description = "불량 id") Long badItemEnrollmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        badItemEnrollmentService.deleteBadItemEnrollment(workOrderId, equipmentLotId, badItemEnrollmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + badItemEnrollmentId + " from deleteBadItemEnrollment.");
        return new ResponseEntity(NO_CONTENT);
    }
}
