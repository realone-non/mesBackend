package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.PopRecycleResponse;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PopRecycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/pop/recycles")
@Tag(name = "pop-recycle", description = "[pop] 8. 재사용")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PopRecycleController {
    private final PopRecycleService popRecycleService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PopRecycleController.class);
    private CustomLogger cLogger;

    //재사용 목록 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "(pop) 재사용 목록 조회", description = "검색 조건: 공정타입")
    public ResponseEntity<List<PopRecycleResponse>> getUseRecycles(
            @RequestParam @Parameter(description = "공정 id") Long workProcessId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
       List<PopRecycleResponse> responseList = popRecycleService.getRecycles(workProcessId);
       cLogger = new MongoLogger(logger, "mongoTemplate");
       cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getUseRecycles");
       return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
