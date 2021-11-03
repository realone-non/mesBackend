package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



// 3-3-1. 작업장 코드 등록
@RestController
@RequestMapping("/work-center-codes")
@Api(tags = "work-center-code")
@RequiredArgsConstructor
public class WorkCenterCodeController {

    @Autowired
    WorkCenterService workCenterService;

    //  코드 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "작업장 코드 생성")
    public ResponseEntity<CodeResponse> createWorkCenterCode(
            @RequestBody CodeRequest codeRequest
    ) {
        return new ResponseEntity<>(workCenterService.createWorkCenterCode(codeRequest), HttpStatus.OK);
    }

    // 코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업장 코드 조회")
    public ResponseEntity<CodeResponse> getWorkCenterCode(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterService.getWorkCenterCode(id), HttpStatus.OK);
    }

    // 코드 전체 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "작업장 코드 전체 조회")
    public ResponseEntity<List<CodeResponse>> getWorkCenterCodes() {
        return new ResponseEntity<>(workCenterService.getWorkCenterCodes(), HttpStatus.OK);
    }

    // 코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업장 코드 삭제")
    public ResponseEntity deleteWorkCenterCode(
            @PathVariable Long id
    ) throws NotFoundException, BadRequestException {
        workCenterService.deleteWorkCenterCode(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
