package com.mes.mesBackend.controller;


import com.mes.mesBackend.dto.request.UserRegistrationRequest;
import com.mes.mesBackend.dto.response.UserRegistrationResponse;
import com.mes.mesBackend.exception.BadRequestException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 18-3. 사용자 등록
@Tag(name = "user-registration", description = "18-3.사용자 등록 API")
@RequestMapping(value = "/user-registrations")
@RestController
@RequiredArgsConstructor
public class UserRegistrationController {
    private final UserService userService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);
    private CustomLogger cLogger;

    // 사용자 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "사용자 생성", description = "초기 password 는 userCode 와 동일함")
    @SecurityRequirement(name = AUTHORIZATION)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
            }
    )
    public ResponseEntity<UserRegistrationResponse> createUserRegistration(
            @RequestBody @Valid UserRegistrationRequest userRegistrationRequest
    ) throws NotFoundException, BadRequestException {
        UserRegistrationResponse userRegistration = userService.createUserRegistration(userRegistrationRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("created user. userId: " + userRegistration.getId() + ", input username: " + userRegistrationRequest.getUserCode() + ", korName: " + userRegistrationRequest.getKorName() + ". from createUserRegistration");
        return new ResponseEntity<>(userRegistration, OK);
    }

    // 사용자 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "사용자 단일 조회")
    @SecurityRequirement(name = AUTHORIZATION)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "401", description = "un authorized")
            }
    )
    public ResponseEntity<UserRegistrationResponse> getUserRegistration(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        UserRegistrationResponse userRegistration = userService.getUserRegistration(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + userRegistration.getId() + " from getUserRegistration.");
        return new ResponseEntity<>(userRegistration, OK);
    }

    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(사번), 이름, 공장(X)
    @GetMapping
    @ResponseBody()
    @Operation(summary = "사용자 리스트 검색 조회", description = "검색조건: 사용자 ID(사번), 이름, 공장(X)")
    @SecurityRequirement(name = AUTHORIZATION)
    public ResponseEntity<List<UserRegistrationResponse>> getUserRegistrations(
            @RequestParam(required = false) @Parameter(description = "사번") String userCode,
            @RequestParam(required = false) @Parameter(description = "이름") String korName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<UserRegistrationResponse> userRegistrationResponses = userService.getUserRegistrations(userCode, korName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getUserRegistrations.");
        return new ResponseEntity<>(userRegistrationResponses, OK);
    }

    // 사용자 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "사용자 수정")
    @SecurityRequirement(name = AUTHORIZATION)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "401", description = "un authorized")
            }
    )
    public ResponseEntity<UserRegistrationResponse> updateUserRegistration(
            @PathVariable Long id,
            @RequestBody @Valid UserRegistrationRequest userRegistrationRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, NoSuchAlgorithmException {
        UserRegistrationResponse updateUserRegistration = userService.updateUserRegistration(id, userRegistrationRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + updateUserRegistration.getId() + " from updateUserRegistration.");
        return new ResponseEntity<>(updateUserRegistration, OK);
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "사용자 삭제")
    @SecurityRequirement(name = AUTHORIZATION)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "401", description = "un authorized")
            }
    )
    public ResponseEntity deleteUserRegistration(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        userService.deleteUserRegistration(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteUserRegistration.");
        return new ResponseEntity(NO_CONTENT);
    }
}

