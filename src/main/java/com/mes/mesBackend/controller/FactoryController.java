package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.FactoryRequest;
import com.mes.mesBackend.dto.response.FactoryResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.FactoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// 공장 등록
@RestController
@RequestMapping(value = "/factories")
@Api(tags = "factory")
@RequiredArgsConstructor
public class FactoryController {

    @Autowired FactoryService factoryService;

    // 공장 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "공장 생성")
    public ResponseEntity<FactoryResponse> createFactory(
            @RequestBody @Valid FactoryRequest factoryRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(factoryService.createFactory(factoryRequest), HttpStatus.OK);
    }

    // 공장 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "공장 조회")
    public ResponseEntity<FactoryResponse> getFactory(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(factoryService.getFactory(id), HttpStatus.OK);
    }

    // 공장 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "공장 페이징 조회")
    public ResponseEntity<Page<FactoryResponse>> getFactories(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(factoryService.getFactories(pageable), HttpStatus.OK);
    }

    // 공장 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "공장 수정")
    public ResponseEntity<FactoryResponse> updateFactory(
            @PathVariable Long id,
            @RequestBody @Valid FactoryRequest factoryRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(factoryService.updateFactory(id, factoryRequest), HttpStatus.OK);
    }

    // 공장 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "공장 삭제")
    public ResponseEntity<Void> deleteFactory(@PathVariable Long id) throws NotFoundException {
        factoryService.deleteFactory(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
