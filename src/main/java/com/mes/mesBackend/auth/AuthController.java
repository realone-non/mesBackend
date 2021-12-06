package com.mes.mesBackend.auth;

import com.mes.mesBackend.dto.request.UserCreateRequest;
import com.mes.mesBackend.dto.request.UserLogin;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;


@Tag(name = "auth", description = "인증 API")
@RequestMapping(value = "/auth")
@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private CustomLogger cLogger;

    // 직원(작업자) 생성
    @PostMapping("/signup")
    @ResponseBody
    @Operation(summary = "회원생성", description = "초기 password 는 userCode 와 동일함")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
            }
    )
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid UserCreateRequest userRequest
    ) throws NotFoundException, NoSuchAlgorithmException, BadRequestException {
        UserResponse user = userService.createUser(userRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info("created user. userId: " + user.getId() + ", input username: " + userRequest.getUserCode() + ", korName: " + userRequest.getKorName() + ". from createUser");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/signin")
    @ResponseBody
    @Operation(summary = "로그인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<TokenResponse> getLoginInfo(
            @RequestBody @Valid UserLogin userLogin
    ) throws NotFoundException, BadRequestException {
        TokenResponse login = userService.getLogin(userLogin);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info("login user. userCode: " + userLogin.getUserCode() + ". from getLoginInfo");
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @PatchMapping("/reissue")
    @ResponseBody
    @Operation(summary = "토큰 재발행")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "401", description = "un authorized"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<TokenResponse> updateRefreshToken(@RequestBody TokenRequest tokenRequestDto) throws CustomJwtException {
        TokenResponse reissue = userService.reissue(tokenRequestDto);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromToken(tokenRequestDto.accessToken) + " is token reissue. from updateRefreshToken.");
        return new ResponseEntity<>(reissue, HttpStatus.OK);
    }
}
