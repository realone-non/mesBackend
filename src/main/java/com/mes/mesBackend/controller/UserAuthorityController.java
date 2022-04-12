package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.BadItemRequest;
import com.mes.mesBackend.dto.response.BadItemResponse;
import com.mes.mesBackend.dto.response.UserAuthorityResponse;
import com.mes.mesBackend.entity.enumeration.UserType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.UserAuthorityService;
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

// 18-2. 권한등록
@RequestMapping(value = "/user-authorities")
@Tag(name = "user-authority", description = "18-2. 권한등록 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class UserAuthorityController {
    private final UserAuthorityService userAuthorityService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(UserAuthorityController.class);
    private CustomLogger cLogger;

    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(userCode), 이름
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "사용자 리스트 검색 조회(권한 NEW 는 제외)",
            description = "검색조건: 사용자 ID(userCode), 이름"
    )
    public ResponseEntity<List<UserAuthorityResponse>> getUserAuthorities(
            @RequestParam(required = false) @Parameter(description = "사용자 ID(userCode)") String userCode,
            @RequestParam(required = false) @Parameter(description = "담당자 id") String userName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<UserAuthorityResponse> userAuthorityResponses = userAuthorityService.getUserAuthorities(userCode, userName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getUserAuthorities.");
        return new ResponseEntity<>(userAuthorityResponses, OK);
    }

    @Operation(summary = "유저 권한 생성", description = "권한 NEW 로는 생성 할 수 없음")
    @PostMapping("/{user-id}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UserAuthorityResponse> createUserAuthority(
            @PathVariable(value = "user-id") @Parameter(description = "직원 고유아이디") Long userId,
            @RequestParam @Parameter(description = "권한") UserType userType,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        UserAuthorityResponse response = userAuthorityService.createUserAuthority(userId, userType);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + userId + " from createUserAuthority.");
        return new ResponseEntity<>(response, OK);
    }

    @DeleteMapping("/{user-id}")
    @ResponseBody()
    @Operation(summary = "유저 권한 삭제", description = "권한이 NEW 로 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteUserAuthorityBadItem(
            @PathVariable(value = "user-id") Long userId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        userAuthorityService.deleteUserAuthority(userId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + userId + " from deleteUserAuthority.");
        return new ResponseEntity(NO_CONTENT);
    }
}
