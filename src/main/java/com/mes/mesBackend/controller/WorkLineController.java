package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkLineRequest;
import com.mes.mesBackend.dto.response.WorkLineResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkLineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 3-3-3. 작업라인 등록
@Tag(name = "work-line", description = "작업라인 API")
@RequestMapping("/work-lines")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class WorkLineController {
    @Autowired
    WorkLineService workLineService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(WorkLineController.class);
    private CustomLogger cLogger;

    // 작업라인 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "작업라인 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkLineResponse> createWorkLine(
            @RequestBody @Valid WorkLineRequest workLineRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkLineResponse workLine = workLineService.createWorkLine(workLineRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + workLine.getId() + " from createWorkLine.");
        return new ResponseEntity<>(workLine, HttpStatus.OK);
    }

    // 작업라인 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업라인 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<WorkLineResponse> getWorkLine(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkLineResponse workLine = workLineService.getWorkLine(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workLine.getId() + " from getWorkLine.");
        return new ResponseEntity<>(workLine, HttpStatus.OK);
    }

    // 작업라인 전체 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "작업라인 전체 조회")
    public ResponseEntity<List<WorkLineResponse>> getWorkLines(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<WorkLineResponse> workLines = workLineService.getWorkLines();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkLines.");
        return new ResponseEntity<>(workLines, HttpStatus.OK);
    }

    // 작업라인 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업라인 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkLineResponse> updateWorkLine(
            @PathVariable Long id,
            @RequestBody @Valid WorkLineRequest workLineRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkLineResponse workLine = workLineService.updateWorkLine(id, workLineRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + workLine.getId() + " from updateWorkLine.");
        return new ResponseEntity<>(workLine, HttpStatus.OK);
    }

    // 작업라인 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업라인 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteWorkLine(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        workLineService.deleteWorkLine(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteWorkLine.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 작업라인 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "작업라인 페이징 조회")
//    @Parameters(
//            value = {
//                    @Parameter(
//                            name = "page", description = "0 부터 시작되는 페이지 (0..N)",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "0")
//                    ),
//                    @Parameter(
//                            name = "size", description = "페이지의 사이즈",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "20")
//                    ),
//                    @Parameter(
//                            name = "sort", in = ParameterIn.QUERY,
//                            description = "정렬할 대상과 정렬 방식, 데이터 형식: property(,asc|desc). + 디폴트 정렬순서는 오름차순, 다중정렬 가능",
//                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,desc"))
//                    )
//            }
//    )
//    public ResponseEntity<Page<WorkLineResponse>> getWorkLines(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(workLineService.getWorkLines(pageable), HttpStatus.OK);
//    }
}
