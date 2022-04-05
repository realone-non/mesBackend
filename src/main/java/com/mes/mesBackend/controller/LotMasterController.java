package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.dto.response.PurchaseStatusCheckResponse;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.LotMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

// 7-1. LOT 마스터 조회
@RequestMapping(value = "/lot-masters")
@Tag(name = "lot-master", description = "LOT 마스터 조회 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class LotMasterController {
    private final LotMasterService lotMasterService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(LotMasterController.class);
    private CustomLogger cLogger;


    // LOT 마스터 조회
    // 검색조건: 품목그룹 id, LOT 번호, 품번|품명, 창고 id, 등록유형, 재고유무, LOT 유형, 검사중여부, 품목계정
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "LOT 마스터 리스트 조회",
            description = "검색조건: 품목그룹 id, LOT 번호, 품번|품명, 창고 id, 등록유형, 재고유무, LOT 유형 id, 검사중여부"
    )
    public ResponseEntity<List<LotMasterResponse>> getLotMasters(
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "LOT 번호") String lotNo,
            @RequestParam(required = false) @Parameter(description = "품명|품목") String itemNoAndItemName,
            @RequestParam(required = false) @Parameter(description = "창고 id") Long wareHouseId,
            @RequestParam(required = false)
            @Parameter(description = "등록유형 [불량: ERROR, 구매입고: PURCHASE_INPUT, 생산: PRODUCTION, 분할: SPLIT, 외주입고: OUTSOURCING_INPUT, 재사용: RECYCLE]") EnrollmentType enrollmentType,
            @RequestParam(required = false) @Parameter(description = "재고유무") Boolean stockYn,
            @RequestParam(required = false) @Parameter(description = "LOT 유형 id") Long lotTypeId,
            @RequestParam(required = false) @Parameter(description = "검사중 여부") Boolean testingYn,
            @RequestParam(required = false) @Parameter(description = "공정구분") WorkProcessDivision workProcessDivision,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<LotMasterResponse> lotMasters =
                lotMasterService.getLotMasters(itemGroupId, lotNo, itemNoAndItemName, wareHouseId, enrollmentType, stockYn, lotTypeId, testingYn, workProcessDivision);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getLotMasters.");
        return new ResponseEntity<>(lotMasters, OK);
    }

    //테스트용 당일 재고 생성
    @GetMapping("/stock-creates")
    @ResponseBody
    @Operation(
            summary = "테스트용 당일 재고 생성"
    )
    public ResponseEntity getStocks(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        lotMasterService.getItemStock();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getLotMasters.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
