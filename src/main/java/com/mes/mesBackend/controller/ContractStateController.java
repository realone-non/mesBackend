package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.ContractItemStateResponse;
import com.mes.mesBackend.entity.enumeration.ContractType;
import com.mes.mesBackend.entity.enumeration.PeriodType;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ContractItemStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.time.LocalDate;
import java.util.List;

// 4-3. 수주 상태 조회
@RequestMapping(value = "/contract-item-states")
@Tag(name = "contract-item-states", description = "수주 상태 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class ContractStateController {
    private final ContractItemStateService contractItemStateService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ContractStateController.class);
    private CustomLogger cLogger;

    // 수주 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "수주 상태 조회", description = "검색조건: 거래처 명, 품번|품명, 수주번호, 담당자 명, 기간구분, 기간(수주날짜 or 납기날짜), 수주유형")
    public ResponseEntity<List<ContractItemStateResponse>> getContractItemStates(
            @RequestParam(required = false) @Parameter(description = "거래처 명") String clientName,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
            @RequestParam(required = false) @Parameter(description = "수주 번호") String contractNo,
            @RequestParam(required = false) @Parameter(description = "담당자 명") String userName,
            @RequestParam(required = false) @Parameter(description = "기간 구분") PeriodType periodType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "수주 유형 [DIFFUSION: 방산 , DOMESTIC: 국내, OVERSEAS: 해외 , ODM: ODM]") ContractType contractType,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ContractItemStateResponse> contractItemStates = contractItemStateService.getContractItemStates(clientName, itemNoAndItemName, contractNo, userName, periodType, fromDate, toDate, contractType);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getContractItemStates.");
        return new ResponseEntity<>(contractItemStates, HttpStatus.OK);
    }

}
