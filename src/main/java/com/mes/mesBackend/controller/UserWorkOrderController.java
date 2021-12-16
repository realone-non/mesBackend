package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// 8-2. 작업자 투입 수정
@RequestMapping("/work-order-users")
@Tag(name = "work-order-user", description = "작업자 투입 API")
@RestController
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class UserWorkOrderController {

//    private final WorkOrderUserService workOrderUserService;
    private final LogService logService;

    private Logger logger = LoggerFactory.getLogger(UserWorkOrderController.class);
    private CustomLogger cLogger;

    // 작업자 투입 리스트 검색 조회, 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
//    @GetMapping
//    @ResponseBody
//    @Operation(
//            summary = "작업자 투입 리스트 검색 조회",
//            description = "검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호"
//    )
//    public ResponseEntity<List<WorkOrderUserResponse>> getWorkOrderUsers(
//            @RequestParam(required = false) @Parameter(description = "작업장 id") Long workProcessId,
//            @RequestParam(required = false) @Parameter(description = "작업라인 id") Long workLineId,
//            @RequestParam(required = false) @Parameter(description = "제조오더번호") String produceOrderNo,
//            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
//            @RequestParam(required = false) @Parameter(description = "지시상태") OrderState orderState,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "작업기간 fromDate") LocalDate fromDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "작업기간 toDate") LocalDate toDate,
//            @RequestParam(required = false) @Parameter(description = "수주번호") String contractNo,
//            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
//    ) {
//        List<WorkOrderProduceOrderResponse> produceOrders = workOrderUserService.getWorkOrderUsers(itemGroupId, itemNoAndName, contractNo, produceOrderNo, fromDate, toDate, instructionStatus);
//        cLogger = new MongoLogger(logger, "mongoTemplate");
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkOrderUsers.");
//        return new ResponseEntity<>(produceOrders, HttpStatus.OK);
//    }
    // 작업자 투입 단일 조회
    // 작업자 투입 수정
}
