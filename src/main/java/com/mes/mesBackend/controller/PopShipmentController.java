package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.PopShipmentResponse;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PopShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RequestMapping("/pop-shipments")
@Tag(name = "pop-shipment", description = "[pop] 출하 API")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class PopShipmentController {
    private final PopShipmentService popShipmentService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PopShipmentController.class);
    private CustomLogger cLogger;

    // 출하 정보 목록 조회
//    @GetMapping
//    @ResponseBody
//    @Operation(summary = "(pop) 출하정보 목록", description = "")
//    public ResponseEntity<List<PopShipmentResponse>> getPopShipments(
//            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하일자 fromDate") LocalDate fromDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "출하일자 toDate") LocalDate toDate,
//            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
//    ) {
//        List<PopShipmentResponse> responses = popShipmentService.getPopShipments();
//        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
//        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopShipments.");
//        return new ResponseEntity<>(responses, OK);
//    }

    // 출하상태 COMPLETION 으로 변경

}
