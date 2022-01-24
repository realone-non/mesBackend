package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.HeaderWarehouseResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.dto.response.MaterialStockReponse;
import com.mes.mesBackend.entity.Header;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.MaterialWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.List;
//11-4. 부품 재고현황 조회
@Tag(name = "material-stocks", description = "부품 재고현황 API")
@RequestMapping(value = "/material-stocks")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class MaterialStockController {

    private final MaterialWarehouseService materialWarehouseService;
    private final LogService logService;
    private Logger logger = LoggerFactory.getLogger(MaterialStockController.class);
    private CustomLogger cLogger;

    // 재고 전체 조회 / 검색조건 : 품목그룹, 품목계정, 품목, 창고
    @GetMapping
    @ResponseBody
    @Operation(summary = "재고 전체 조회", description = "검색조건: 품목그룹, 품목계정, 품목, 창고 ")
    public ResponseEntity<JSONArray> getMaterialStocks(
            @RequestParam(required = false) @Parameter(description = "품목 계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "품목 그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
            @RequestParam(required = false) @Parameter(description = "창고 id") Long warehouseId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        JSONArray responseList = materialWarehouseService.getMaterialStock(itemGroupId, itemAccountId, itemNo, itemName, warehouseId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of itemGroupId: " + itemGroupId
                + ", itemAccountId: " + itemGroupId + " from getItems.");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // 헤더 창고 목록 조회
    @GetMapping("/header-warehouses")
    @ResponseBody
    @Operation(summary = "헤더 창고 목록 조회")
    public ResponseEntity<JSONArray> getHeaderWareHouses(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        JSONArray responseList = materialWarehouseService.getHeaderWarehouse();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of headerWarehouse from getHeaderWareHouses");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
