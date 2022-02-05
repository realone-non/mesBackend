package com.mes.mesBackend.controller;

import com.amazonaws.Response;
import com.mes.mesBackend.dto.response.EquipmentResponse;
import com.mes.mesBackend.dto.response.LabelPrintResponse;
import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.EquipmentService;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.WorkProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping(value = "/label-prints")
@Tag(name = "label-print", description = "라벨 프린트용 API")
@RestController
public class LabelPrintController {
    @Autowired
    LogService logService;
    @Autowired
    LotMasterService lotMasterService;
    @Autowired
    WorkProcessService workProcessService;
    @Autowired
    EquipmentService equipmentService;

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
    ) throws NotFoundException{
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
}
