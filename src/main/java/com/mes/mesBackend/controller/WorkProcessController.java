package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkProcessRequest;
import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkProcessService;
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

// 3-3-2. 작업 공정 등록
@Tag(name = "work-process", description = "작업 공정 API")
@RequestMapping("/work-processes")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class WorkProcessController {

    @Autowired
    WorkProcessService workProcessService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(WorkProcessController.class);
    private CustomLogger cLogger;

    // 작업공정 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "작업공정 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkProcessResponse> createWorkProcess(
            @RequestBody @Valid WorkProcessRequest workProcessRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkProcessResponse workProcess = workProcessService.createWorkProcess(workProcessRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + workProcess.getId() + " from createWorkProcess.");
        return new ResponseEntity<>(workProcess, HttpStatus.OK);
    }

    // 작업공정 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업공정 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<WorkProcessResponse> getWorkProcess(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkProcessResponse workProcess = workProcessService.getWorkProcess(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workProcess.getId() + " from getWorkProcess.");
        return new ResponseEntity<>(workProcess, HttpStatus.OK);
    }

    // 작업공정 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "작업공정 전체 조회")
    public ResponseEntity<List<WorkProcessResponse>> getWorkProcesses(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<WorkProcessResponse> workProcesses = workProcessService.getWorkProcesses();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkProcesses.");
        return new ResponseEntity<>(workProcesses, HttpStatus.OK);
    }

    // 작업공정 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업공정 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkProcessResponse> updateWorkProcess(
            @PathVariable Long id,
            @RequestBody @Valid WorkProcessRequest workProcessRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkProcessResponse workProcess = workProcessService.updateWorkProcess(id, workProcessRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + workProcess.getId() + " from updateWorkProcess.");
        return new ResponseEntity<>(workProcess, HttpStatus.OK);
    }

    // 작업공정 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업공정 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteWorkProcess(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        workProcessService.deleteWorkProcess(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteWorkProcess.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 작업공정 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "작업공정 페이징 조회")
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
//    public ResponseEntity<Page<WorkProcessResponse>> getWorkProcesses(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(workProcessService.getWorkProcesses(pageable), HttpStatus.OK);
//    }
}
