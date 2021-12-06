package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UserUpdateRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.UserService;
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
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Tag(name = "user", description = "직원(작업자) API")
@RequestMapping(value = "/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private CustomLogger cLogger;

    // 직원(작업자) 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "직원(작업자) 단일 조회")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "401", description = "un authorized")
            }
    )
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        UserResponse user = userService.getUser(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + user.getId() + " from getUser.");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 직원(작업자) 전체 조회 검색조건: 부서, 사번, 이름
    @GetMapping
    @ResponseBody()
    @Operation(summary = "직원(작업자) 전체 조회", description = "검색조건: 부서, 사번, 이름")
    @SecurityRequirement(name = "Authorization")
    @ApiResponse(responseCode = "401", description = "un authorized")
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(required = false) @Parameter(description = "부서 id") Long departmentId,
            @RequestParam(required = false) @Parameter(description = "사번") String userCode,
            @RequestParam(required = false) @Parameter(description = "이름") String korName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<UserResponse> users = userService.getUsers(departmentId, userCode, korName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of departmentId: " + departmentId + " from getUsers.");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 직원(작업자) 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "직원(작업자) 수정")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "401", description = "un authorized")
            }
    )
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest userRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, NoSuchAlgorithmException {
        UserResponse user = userService.updateUser(id, userRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + user.getId() + " from updateUser.");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "직원(작업자) 삭제")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "401", description = "un authorized")
            }
    )
    public ResponseEntity deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        userService.deleteUser(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteUser.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 직원(작업자) 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "직원(작업자) 페이징 조회")
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
//    public ResponseEntity<Page<UserResponse>> getUsers(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(userService.getUsers(pageable), HttpStatus.OK);
//    }
}
