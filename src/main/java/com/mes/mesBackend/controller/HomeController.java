package com.mes.mesBackend.controller;

import com.mes.mesBackend.entity.UserVo;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/home")
@Api(tags = {"로그인 메뉴"})
@RequiredArgsConstructor
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody()
    @ApiOperation(value = "Login")
    public ResponseEntity<Optional<UserVo>> GetLoginInfo(String id, String pass){
        Optional<UserVo> user = userService.Find(id, pass);
        if(ObjectUtils.isEmpty(user)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<Optional<UserVo>>(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody()
    @ApiOperation(value = "회원가입")
    public ResponseEntity<UserVo> SetUserInfo(UserVo user){
        return new ResponseEntity<UserVo>(userService.Save(user), HttpStatus.OK);
    }
}
