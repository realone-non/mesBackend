package com.mes.mesBackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 15-4. 검사대기 현황
@RequestMapping(value = "/part-input-test-performances")
@Tag(name = "part-input-test-performance", description = "15-4. 검사대기 현황 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequiredArgsConstructor
public class OutsourcingInputTestScheduleController {
}
