package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.ReceiptAndPaymentResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.MaterialWarehouseService;
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
import static org.springframework.http.HttpStatus.OK;

// 품목형태
@Tag(name = "material-receipt-and-payments", description = "부품 수불부 API")
@RequestMapping(value = "/material-receipt-and-payments")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class MaterialReceiptAndPaymentController{
    private final MaterialWarehouseService materialWarehouseService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ItemFormController.class);
    private CustomLogger cLogger;

    // 부품 수불부 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "부품 수불부 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<ReceiptAndPaymentResponse>> getReceiptAndPayments(
            @RequestParam(required = false) @Parameter(description = "창고 ID") Long warehouseId,
            @RequestParam(required = false) @Parameter(description = "품목 그룹 ID") Long itemAccountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "시작날짜") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "종료날짜") LocalDate toDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ReceiptAndPaymentResponse> responseList = materialWarehouseService.getReceiptAndPaymentList(warehouseId, itemAccountId, fromDate, toDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        return new ResponseEntity<>(responseList, OK);
    }
}
