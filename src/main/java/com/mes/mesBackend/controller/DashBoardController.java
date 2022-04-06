package com.mes.mesBackend.controller;

import com.google.gson.JsonArray;
import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
import com.mes.mesBackend.dto.response.WorkProcessStatusResponse;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.DashBoardService;
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

// 대시보드
@Tag(name = "dash-board", description = "대시보드 API")
@RequestMapping(value = "/dash-boards")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class DashBoardController {
    private final DashBoardService dashBoardService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ClientTypeController.class);
    private CustomLogger cLogger;

    @Operation(
            summary = "생산현황, 수주현황, 출하현황, 출하완료 갯수 조회",
            description = "생산현황: 현재 진행중인 제조오더 목록 갯수\t\n" +
                    "수주현황: 납기일자 오늘 이후인거 갯수 \t\n" +
                    "출하현황: 출하일자 오늘인 목록 갯수 \t\n" +
                    "출하완료: 출하일자가 오늘이면서 완료된 목록 갯수"
    )
    @GetMapping("/operation-status")
    @ResponseBody
    public ResponseEntity<OperationStatusResponse> getOperationStatus(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        OperationStatusResponse response = dashBoardService.getOperationStatus();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getOperationStatus.");
        return new ResponseEntity<>(response, OK);
    }

    @Operation(
            summary = "작업 공정 별 정보",
            description = "원료혼합: MATERIAL_MIXING \t\n 충진: FILLING \t\n 캡조립(융착): CAP_ASSEMBLY \t\n 라벨링: LABELING \t\n 포장: PACKAGING"
    )
    @GetMapping("/work-process-status")
    @ResponseBody
    public ResponseEntity<WorkProcessStatusResponse> getWorkProcessStatus(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        WorkProcessStatusResponse response = dashBoardService.getWorkProcessStatus();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkProcessStatus.");
        return new ResponseEntity<>(response, OK);
    }

    @Operation(
            summary = "품목계정 별 재고현황 정보",
            description = "RAW_MATERIAL: 원자재 \t\n SUB_MATERIAL: 부자재 \t\n HALF_PRODUCT: 반제품 \t\n PRODUCT: 완제품 \t\n NONE: 미분류"
    )
    @GetMapping("/item-inventory-status")
    @ResponseBody
    public ResponseEntity<List<ItemInventoryStatusResponse>> getItemInventoryStatusResponse(
            @RequestParam @Parameter(description = "") GoodsType goodsType,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ItemInventoryStatusResponse> responses = dashBoardService.getItemInventoryStatusResponse(goodsType);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemInventoryStatusResponse.");
        return new ResponseEntity<>(responses, OK);
    }

    @Operation(
            summary = "매출관련현황 - 수주",
            description = "품목 별(최대 5개) 주 단위 수주수량 합계"
    )
    @GetMapping("/contract-sales-related-status")
    @ResponseBody
    public ResponseEntity<JsonArray> getContractSaleRelatedStatus(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        JsonArray responses = dashBoardService.getContractSaleRelatedStatus();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getContractSaleRelatedStatus.");
        return new ResponseEntity<>(responses, OK);
    }

    @Operation(
            summary = "매출관련현황 - 제품 생산",
            description = "품목 별(최대 5개) 주 단위 제품 생산 수량 합계"
    )
    @GetMapping("/product-sales-related-status")
    @ResponseBody
    public ResponseEntity<JsonArray> getProductSaleRelatedStatus(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        JsonArray responses = dashBoardService.getProductSaleRelatedStatus();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getProductSaleRelatedStatus.");
        return new ResponseEntity<>(responses, OK);
    }

    @Operation(
            summary = "매출관련현황 - 제품출고",
            description = "품목 별(최대 5개) 출고 수량 합계"
    )
    @GetMapping("/shipment-sales-related-status")
    @ResponseBody
    public ResponseEntity<JsonArray> getShipmentSaleRelatedStatus(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        JsonArray responses = dashBoardService.getShipmentSaleRelatedStatus();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getShipmentSaleRelatedStatus.");
        return new ResponseEntity<>(responses, OK);
    }
}
