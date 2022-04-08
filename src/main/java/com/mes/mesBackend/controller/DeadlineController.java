package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.DeadlineResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.DeadlineService;
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
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 18-5. 마감일자
@Tag(name = "deadline", description = "18-5.마감일자 API")
@RequestMapping(value = "/deadlines")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class DeadlineController {
    private final DeadlineService deadlineService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(DeadlineController.class);
    private CustomLogger cLogger;

    // 마감일자 생성
    @PostMapping("{contract-id}")
    @ResponseBody
    @Operation(summary = "수주 마감일자 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<DeadlineResponse> createDeadline(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @RequestParam @Parameter(description = "수주 마감일자 yyyy-MM-dd") @DateTimeFormat(iso = DATE) LocalDate deadlineDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        DeadlineResponse deadline = deadlineService.createDeadline(contractId, deadlineDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + contractId + " from createDeadline.");
        return new ResponseEntity<>(deadline, OK);
    }

    // 마감일자 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "수주 마감일자 리스트 조회", description = "마감일자 등록 된 수주만 조회")
    public ResponseEntity<List<DeadlineResponse>> getDeadlines(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<DeadlineResponse> deadlines = deadlineService.getDeadlines();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getDeadlines.");
        return new ResponseEntity<>(deadlines, OK);
    }

    // 마감일자 삭제
    @DeleteMapping("/{contract-id}")
    @ResponseBody
    @Operation(summary = "수주 마감일자 삭제", description = "수주에 마감일자만 없어짐")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteDeadline(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        deadlineService.deleteDeadline(contractId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + contractId + " from deleteDeadline.");
        return new ResponseEntity(NO_CONTENT);
    }
}
