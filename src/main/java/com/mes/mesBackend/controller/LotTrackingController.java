package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.LotTrackingResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.LotTrackService;
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

// 7-2. LOT Tracking
@RequestMapping(value = "/lot-trackings")
@Tag(name = "lot-tracking", description = "7-2. LOT Tracking API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class LotTrackingController {
    private final LotTrackService lotTrackService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(LotTrackingController.class);
    private CustomLogger cLogger;

    // LOT Tracking
    // 검색조건: LOT 번호, 추적유형, 품명|품번
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "LOT Tracking",
            description = "검색조건: LOT 번호(필수값), 추적유형(필수값), 품명|품번"
    )
    public ResponseEntity<List<LotTrackingResponse>> getLotTrackings(
            @RequestParam @Parameter(description = "LOT 번호(필수값)") String lotNo,
            @RequestParam @Parameter(description = "추적유형(필수값) true: 정방향, false: 역방향") boolean trackingType,
            @RequestParam(required = false) @Parameter(description = "품명|품목") String itemNoAndItemName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        List<LotTrackingResponse> lotTrackingResponses =
                lotTrackService.getTrackings(lotNo, trackingType, itemNoAndItemName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getTrackings.");
        return new ResponseEntity<>(lotTrackingResponses, OK);
    }
}
