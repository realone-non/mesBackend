package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.service.HeaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/headers")
@Api(tags = "header")
@RequiredArgsConstructor
public class HeaderController {

    @Autowired
    HeaderService headerService;

    // 헤더조회
    @GetMapping("/{controllerName}")
    @ResponseBody
    @ApiOperation(value = "헤더 조회")
    public ResponseEntity<List<HeaderResponse>> getHeaders(@PathVariable String controllerName) {
        return new ResponseEntity<>(headerService.getHeaders(controllerName), HttpStatus.OK);
    }

    // 헤더생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "헤더 생성")
    public ResponseEntity<HeaderResponse> createHeader(@RequestBody HeaderRequest headerRequest) {
        return new ResponseEntity<>(headerService.createHeader(headerRequest), HttpStatus.OK);
    }
}
