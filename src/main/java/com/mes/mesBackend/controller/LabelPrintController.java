package com.mes.mesBackend.controller;


import com.mes.mesBackend.dto.response.LabelPrintResponse;
import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.amazonaws.Response;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping(value = "/label-prints")
@Tag(name = "label-print", description = "라벨 프린트용 API")
@RestController
@RequiredArgsConstructor
public class LabelPrintController {
    @Autowired
    LogService logService;
    @Autowired
    LotMasterService lotMasterService;
    @Autowired
    WorkProcessService workProcessService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    PopShipmentService popShipmentService;
    @Autowired
    PurchaseInputService purchaseInputService;

    private Logger logger = LoggerFactory.getLogger(LabelPrintController.class);
    private CustomLogger cLogger;


    @Operation(summary = "공정, 설비로 LOT번호 조회")
    @GetMapping("/{work-process-id}/equipments/{equipment-id}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<LabelPrintResponse>> getPrints(
            @PathVariable(value = "work-process-id") @Parameter(description = "공정 id") Long workProcessId,
            @PathVariable(value = "equipment-id") @Parameter(description = "설비 id") Long equipmentId
    ) {
        List<LabelPrintResponse> responseList = lotMasterService.getPrints(workProcessId, equipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("Possible print list view from LabelPrintController getPrints");
        return new ResponseEntity<>(responseList, OK);
    }

    @Operation(summary = "공정 전체 조회")
    @GetMapping("/work-processes")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<WorkProcessResponse>> getWorkProcessList(){
        List<WorkProcessResponse> responseList = workProcessService.getWorkProcesses();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("WorkProcessList view from LabelPrintController getWorkProcessList");
        return new ResponseEntity<>(responseList, OK);
    }

    @Operation(summary = "공정으로 설비 조회")
    @GetMapping("/{work-process-id}/equipments")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<PopEquipmentResponse>> getEquipments(
            @PathVariable(value = "work-process-id") @Parameter(description = "공정 ID") Long workProcessId
    ) throws NotFoundException {
        List<PopEquipmentResponse> responseList = equipmentService.getEquipmentsByWorkProcess(workProcessId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("WorkProcessList view from LabelPrintController getWorkProcessList");
        return new ResponseEntity<>(responseList, OK);
    }

    // 출하 정보 목록 조회
    @GetMapping("/shipments")
    @ResponseBody
    @Operation(summary = "출하정보 목록", description = "검색조건: 검색시작날짜(fromDate) ~ 검색종료날짜(toDate), 거래처 명, 완료여부")
    public ResponseEntity<List<PopShipmentResponse>> getPopShipments(
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하일자 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하일자 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "거래처 명") String clientName,
            @RequestParam(required = false) @Parameter(description = "완료여부(빈값: 전부, true: COMPLETION 만 조회, false: SCHEDULE 만 조회)") Boolean completionYn
    ) {
        List<PopShipmentResponse> responses = popShipmentService.getPopShipments(fromDate, toDate, clientName, completionYn);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("ShipmentList viewed the list of from getPopShipments!");
        return new ResponseEntity<>(responses, OK);
    }

    @GetMapping("/purchase-inputs")
    @ResponseBody
    @Operation(summary = "금일 구매입고 목록", description = "")
    public ResponseEntity<List<LabelPrintResponse>> getPurchaseInputs() {
        List<LabelPrintResponse> responses = purchaseInputService.getTodayPurchaseInputs();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("purchaseInput viewed the list of from getPurchaseInputs!");
        return new ResponseEntity<>(responses, OK);
    }
}
