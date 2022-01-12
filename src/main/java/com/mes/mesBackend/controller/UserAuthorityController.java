package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.UserAuthorityResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.UserAuthorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 18-2. 권한등록
@RequestMapping(value = "/user-authorities")
@Tag(name = "user-authority", description = "18-2. 권한등록 API")
@RestController
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class UserAuthorityController {
    private final UserAuthorityService userAuthorityService;
    private final LogService logService;
    private Logger logger = LoggerFactory.getLogger(UserAuthorityController.class);
    private CustomLogger cLogger;

    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(userCode), 이름
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "사용자 리스트 검색 조회",
            description = "검색조건: 사용자 ID(userCode), 이름"
    )
    public ResponseEntity<List<UserAuthorityResponse>> getUserAuthorities(
            @RequestParam(required = false) @Parameter(description = "사용자 ID(userCode)") String userCode,
            @RequestParam(required = false) @Parameter(description = "담당자 id") String userName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<UserAuthorityResponse> userAuthorityResponses = userAuthorityService.getUserAuthorities(userCode, userName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getUserAuthorities.");
        return new ResponseEntity<>(userAuthorityResponses, HttpStatus.OK);
    }

    // 다른기능들은 as 기간에 구현 예정
}
