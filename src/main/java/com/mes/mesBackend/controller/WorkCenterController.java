package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkCenterRequest;
import com.mes.mesBackend.dto.response.WorkCenterResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 3-3-1. 작업장 등록
@Tag(name = "work-center", description = "작업장 API")
@RequestMapping("/work-centers")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class WorkCenterController {
    private final WorkCenterService workCenterService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(WorkCenterController.class);
    private CustomLogger cLogger;

    // 작업장 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "작업장 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkCenterResponse> createWorkCenter(
            @RequestBody @Valid WorkCenterRequest workCenterRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkCenterResponse workCenter = workCenterService.createWorkCenter(workCenterRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + workCenter.getId() + " from createWorkCenter.");
        return new ResponseEntity<>(workCenter, OK);
    }

    // 작업장 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업장 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<WorkCenterResponse> getWorkCenter(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkCenterResponse workCenter = workCenterService.getWorkCenter(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workCenter.getId() + " from getWorkCenter.");
        return new ResponseEntity<>(workCenter, OK);
    }

    // 작업장 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "작업장 전체 조회")
    public ResponseEntity<List<WorkCenterResponse>> getWorkCenters(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<WorkCenterResponse> workCenters = workCenterService.getWorkCenters();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getWorkCenters.");
        return new ResponseEntity<>(workCenters, OK);
    }

    // 작업장 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업장 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkCenterResponse> updateWorkCenter(
            @PathVariable Long id,
            @RequestBody @Valid WorkCenterRequest workCenterRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkCenterResponse workCenter = workCenterService.updateWorkCenter(id, workCenterRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + workCenter.getId() + " from updateWorkCenter.");
        return new ResponseEntity<>(workCenter, OK);
    }

    // 작업장 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업장 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteWorkCenter(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        workCenterService.deleteWorkCenter(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteWorkCenter.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 작업장 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "작업장 페이징 조회")
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
//    public ResponseEntity<Page<WorkCenterResponse>> getWorkCenters(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(workCenterService.getWorkCenters(pageable), OK);
//    }
}
