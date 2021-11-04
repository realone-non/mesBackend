package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CheckTypeRequest;
import com.mes.mesBackend.dto.response.CheckTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.CheckTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 점검유형 (일, 월, 분기 등등)
@RestController
@RequestMapping("/check-types")
@Api(tags = "check-type")
@RequiredArgsConstructor
public class CheckTypeController {

    @Autowired
    CheckTypeService checkTypeService;

    // 점검유형 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "점검유형 생성")
    public ResponseEntity<CheckTypeResponse> createCheckType(@RequestBody CheckTypeRequest checkTypeRequest) {
        return new ResponseEntity<>(checkTypeService.createCheckType(checkTypeRequest), HttpStatus.OK);
    }

    // 점검유형 단일조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "점검유형 단일 조회")
    public ResponseEntity<CheckTypeResponse> getCheckType(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(checkTypeService.getCheckType(id), HttpStatus.OK);
    }

    // 점검유형 전체 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "점검유형 전체 조회")
    public ResponseEntity<List<CheckTypeResponse>> getCheckTypes() {
        return new ResponseEntity<>(checkTypeService.getCheckTypes(), HttpStatus.OK);
    }

    // 점검유형 수정 api
    @PutMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "점검유형 수정")
    public ResponseEntity<CheckTypeResponse> updateCheckType(
            @PathVariable Long id,
            @RequestBody CheckTypeRequest checkTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(checkTypeService.updateCheckType(id, checkTypeRequest), HttpStatus.OK);
    }

    // 점검유형 삭제 api
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "점검유형 삭제")
    public ResponseEntity deleteCheckType(@PathVariable Long id) throws NotFoundException {
        checkTypeService.deleteCheckType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
