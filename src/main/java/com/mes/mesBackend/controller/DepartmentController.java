package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.DepartmentRequest;
import com.mes.mesBackend.dto.response.DepartmentResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.DepartmentService;
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

// 부서등록
@Tag(name = "department", description = "부서 API")
@RequestMapping(value = "/departments")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class DepartmentController {

    @Autowired 
    DepartmentService departmentService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    private CustomLogger cLogger;

    // 부서 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "부서 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<DepartmentResponse> createDepartment(
            @RequestBody @Valid DepartmentRequest departmentRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        DepartmentResponse department = departmentService.createDepartment(departmentRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + department.getId() + " from createDepartment.");
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    // 부서 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "부서 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<DepartmentResponse> getDepartment(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        DepartmentResponse department = departmentService.getDepartment(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + department.getId() + " from getDepartment.");
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    // 부서 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "부서 전체 조회")
    public ResponseEntity<List<DepartmentResponse>> getDepartments(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<DepartmentResponse> departments = departmentService.getDepartments();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getDepartments.");
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    // 부서 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "부서 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @RequestBody @Valid DepartmentRequest departmentRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        DepartmentResponse department = departmentService.updateDepartment(id, departmentRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + department.getId() + " from updateDepartment.");
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    // 부서 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "부서 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        departmentService.deleteDepartment(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteDepartment.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "부서 페이징 조회")
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
//    public ResponseEntity<Page<DepartmentResponse>> getDepartments(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(departmentService.getDepartments(pageable), HttpStatus.OK);
//    }
}
