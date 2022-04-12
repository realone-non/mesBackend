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

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;


@Tag(name = "auth", description = "인증 API")
@RequestMapping(value = "/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
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
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("created user. userId: " + user.getId() + ", input username: " + userRequest.getUserCode() + ", korName: " + userRequest.getKorName() + ". from createUser");
        return new ResponseEntity<>(user, OK);
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
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("login user. userCode: " + userLogin.getUserCode() + ". from getLoginInfo");
        return new ResponseEntity<>(login, OK);
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
    public ResponseEntity<TokenResponse> updateRefreshToken(@RequestBody TokenRequest tokenRequestDto) throws CustomJwtException, NotFoundException {
        TokenResponse reissue = userService.reissue(tokenRequestDto);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromToken(tokenRequestDto.accessToken) + " is token reissue. from updateRefreshToken.");
        return new ResponseEntity<>(reissue, OK);
    }

    // 비밀번호 초기화
    @PatchMapping("/reset-password")
    @ResponseBody
    @Operation(summary = "비밀번호 초기화", description = "초기화 된 password 는 userCode 와 동일")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity resetPassword(@RequestParam String email) throws NotFoundException {
        userService.resetPassword(email);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("user email " + email + " is reset password . from resetPassword.");
        return new ResponseEntity<>(OK);
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    @ResponseBody
    @Operation(summary = "비밀번호 변경", description = "변경 비밀번호 5자 이상 입력")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    @SecurityRequirement(name = AUTHORIZATION)
    public ResponseEntity updatePassword(
            @RequestBody @Valid UserCreateRequest.password password,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        userService.updatePassword(userCode, password);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info( userCode + " is modified the password. from updateBadItem.");
        return new ResponseEntity<>(OK);
    }
}
