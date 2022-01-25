package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.ShortageReponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.MaterialWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//11-5.Shortage
@Tag(name = "shortage", description = "11-5.Shortage")
@RequestMapping(value = "/shortages")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class ShortageController {
    private final MaterialWarehouseService materialWarehouseService;
    private final LogService logService;

    private Logger logger = LoggerFactory.getLogger(ShortageController.class);
    private CustomLogger cLogger;

    //Shortage 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "Shortage 조회")
    public ResponseEntity<List<ShortageReponse>> getShortages(
            @RequestParam(required = false) @Parameter(description = "품목그룹 ID") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품번품명") String itemNoAndName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "기준일자") LocalDate stdDate,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ShortageReponse> reponseList = materialWarehouseService.getShortage(itemGroupId, itemNoAndName, stdDate);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getDeadlines.");
        return new ResponseEntity<>(deadlines, HttpStatus.OK);
}
