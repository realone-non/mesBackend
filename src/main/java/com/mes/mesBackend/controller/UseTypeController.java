package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UseTypeRequest;
import com.mes.mesBackend.dto.response.UseTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.UseTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 용도유형
@RestController
@RequestMapping(value = "/use-types")
@Api(tags = "use-type")
@RequiredArgsConstructor
public class UseTypeController {
    @Autowired
    UseTypeService useTypeService;

    // 용도유형 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "용도유형 생성")
    public ResponseEntity<UseTypeResponse> createUseType(
            @RequestBody UseTypeRequest useTypeRequest
    ) {
        return new ResponseEntity<>(useTypeService.createUseType(useTypeRequest), HttpStatus.OK);
    }

    // 용도유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "용도유형 단일 조회")
    public ResponseEntity<UseTypeResponse> getUseType(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(useTypeService.getUseType(id), HttpStatus.OK);
    }

    // 용도유형 리스트 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "용도유형 리스트 조회")
    public ResponseEntity<List<UseTypeResponse>> getUseTypes() {
        return new ResponseEntity<>(useTypeService.getUseTypes(), HttpStatus.OK);
    }

    // 용도유형 수정
    @PutMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "용도유형 수정")
    public ResponseEntity<UseTypeResponse> updateUseType(
            @PathVariable Long id,
            @RequestBody UseTypeRequest useTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(useTypeService.updateUseType(id, useTypeRequest), HttpStatus.OK);
    }

    // 용도유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "용도유형 삭제")
    public ResponseEntity deleteUseType(
            @PathVariable Long id
    ) throws NotFoundException {
        useTypeService.deleteUseType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
