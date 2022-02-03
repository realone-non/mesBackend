package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PopService;
import com.mes.mesBackend.service.WorkProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

// pop
@RequestMapping("/pop")
@Tag(name = "pop", description = "pop API")
@RestController
@RequiredArgsConstructor
public class PopController {
    private final PopService popService;
    private final WorkProcessService workProcessService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PopController.class);
    private CustomLogger cLogger;

    // TODO: 공정 조회할 때 검색조건(재사용) 추가해야됨
    @GetMapping("/work-processes")
    @ResponseBody
    @Operation(summary = "(pop) 작업공정 전체 조회", description = "검색조건: 재사용 공정 여부")
    public ResponseEntity<List<WorkProcessResponse>> getPopWorkProcesses(
            @RequestParam(required = false) @Parameter(description = "재사용 공정(true: 재사용공정)") Boolean recycleYn
    ) {
        List<WorkProcessResponse> workProcesses = popService.getPopWorkProcesses(recycleYn);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info( "viewed the list of from getPopWorkProcesses.");
        return new ResponseEntity<>(workProcesses, OK);
    }

    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/equipments")
    @ResponseBody
    @Operation(summary = "(pop) 설비 목록 조회", description = "작업공정에 대한 설비 목록")
    public ResponseEntity<List<PopEquipmentResponse>> getPopEquipments(
            @RequestParam @Parameter(description = "작업공정 id") Long workProcessId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopEquipmentResponse> equipments = popService.getPopEquipments(workProcessId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopWorkOrders.");
        return new ResponseEntity<>(equipments, OK);
    }

    // 작업지시 정보 리스트 api, 조건: 작업자, 작업공정
    // 작업지시 목록(공정)
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/work-orders")
    @ResponseBody
    @Operation(
            summary = "(pop) 작업지시 정보",
            description = "조건: 작업공정 id, 날짜(당일)"
    )
    public ResponseEntity<List<PopWorkOrderResponse>> getPopWorkOrders(
            @RequestParam @Parameter(description = "작업공정 id") Long workProcessId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopWorkOrderResponse> popWorkOrderResponses = popService.getPopWorkOrders(workProcessId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopWorkOrders.");
        return new ResponseEntity<>(popWorkOrderResponses, OK);
    }

    // 작업지시 상태 변경
    @SecurityRequirement(name = AUTHORIZATION)
    @PostMapping("/work-orders/{work-order-id}")
    @ResponseBody
    @Operation(summary = "(pop) 작업완료 수량 입력", description = "")
    public ResponseEntity<Long> createWorkOrder(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestParam @Parameter(description = "품목 id") Long itemId,
            @RequestParam @Parameter(description = "수량") int productAmount,
            @RequestParam @Parameter(description = "설비 id") Long equipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        Long lotId = popService.createCreateWorkOrder(workOrderId, itemId, userCode, productAmount, equipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from getPopWorkOrders.");
        return new ResponseEntity<>(lotId, OK);
    }

    // 사용한 원자재 등록
    // 해당 품목(반제품)에 대한 원자재, 부자재 정보 가져와야함
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/bom-detail-items")
    @ResponseBody
    @Operation(summary = "[미구현] (pop) 사용한 레시피 조회", description = "사용한 원부자재 리스트 조회")
    public ResponseEntity<List<PopBomDetailItemResponse>> getPopBomDetailItems(
            @RequestParam @Parameter(description = "lot id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopBomDetailItemResponse> popBomDetailItems = popService.getPopBomDetailItems(lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopBomDetailItems.");
        return new ResponseEntity<>(popBomDetailItems, OK);
    }
}
