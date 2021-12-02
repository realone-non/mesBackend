package com.mes.mesBackend.auth;

import com.mes.mesBackend.dto.request.UserLogin;
import com.mes.mesBackend.dto.request.UserCreateRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.OK);
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
        return new ResponseEntity<>(userService.getLogin(userLogin), HttpStatus.OK);
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
        return new ResponseEntity<>(userService.reissue(tokenRequestDto), HttpStatus.OK);
    }
}
