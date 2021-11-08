package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@Api(tags = "user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    // 직원(작업자) 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "직원(작업자) 생성")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) throws NotFoundException {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.OK);
    }

    // 직원(작업자) 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "직원(작업자) 단일 조회")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    // 직원(작업자) 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "직원(작업자) 페이징 조회")
    public ResponseEntity<Page<UserResponse>> getUsers(Pageable pageable) {
        return new ResponseEntity<>(userService.getUsers(pageable), HttpStatus.OK);
    }

    // 직원(작업자) 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "직원(작업자) 수정")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable(value = "id") Long id,
            @RequestBody UserRequest userRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(userService.updateUser(id, userRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "직원(작업자) 삭제")
    public ResponseEntity deleteUser(@PathVariable(value = "id") Long id) throws NotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/login")
    @ResponseBody
    @ApiOperation(value = "로그인")
    public ResponseEntity<UserResponse> getLoginInfo(
            @RequestParam String userCode,
            @RequestParam String password
    ) throws NotFoundException {
        return new ResponseEntity<>(userService.getLogin(userCode, password), HttpStatus.OK);
    }
}
