package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;

//@Tag(name = "work-process", description = "작업 공정 API")
//@RequestMapping("/pop-work-processes")
//@RestController
@RequiredArgsConstructor
public class popWorkProcessController {
    private final WorkProcessService workProcessService;
    private final Logger logger = LoggerFactory.getLogger(WorkProcessController.class);
    private CustomLogger cLogger;

    // 작업공정 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "(pop)작업공정 전체 조회")
    public ResponseEntity<List<WorkProcessResponse>> getPopWorkProcesses() {
        List<WorkProcessResponse> workProcesses = workProcessService.getWorkProcesses();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info( "viewed the list of from getPopWorkProcesses.");
        return new ResponseEntity<>(workProcesses, HttpStatus.OK);
    }
}
