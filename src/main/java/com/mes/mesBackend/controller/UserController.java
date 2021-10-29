package com.mes.mesBackend.controller;

import com.mes.mesBackend.entity.UserVo;
import com.mes.mesBackend.repository.UserVoRepository;
import com.mes.mesBackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@Api(tags = "user")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    UserVoRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Login")
    public ResponseEntity<UserVo> getLoginInfo(String nickName, String pass){
        UserVo user = userService.findByNickNameAndPassword(nickName, pass);
        if(ObjectUtils.isEmpty(user)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping
    @ResponseBody
    @ApiOperation(value = "유저 생성")
    public ResponseEntity<UserVo> createUser(@RequestBody UserVo user){
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "유저 조회")
    public ResponseEntity<UserVo> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "유저 수정", notes = "userName만 수정 가능")
    public ResponseEntity<UserVo> updateUser(@PathVariable Long id, @RequestBody UserVo user) {
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "유저 삭제")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "유저 목록 조회")
    public ResponseEntity<Page<UserVo>> getUsers(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(userService.getUsers(pageable), HttpStatus.OK);
    }
}
