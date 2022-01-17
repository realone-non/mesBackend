package com.mes.mesBackend.controller;

import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.service.InputTestPerformanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 14-4. 검사대기 현황
@RequestMapping(value = "/outsourcing-input-test-performances")
@Tag(name = "outsourcing-input-test-performance", description = "14-4. 검사대기 현황 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class PartInputTestScheduleController {
    private final InputTestPerformanceService inputTestPerformanceService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PartInputTestPerformanceController.class);
    private CustomLogger cLogger;
}
