package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.FactoryRequest;
import com.mes.mesBackend.dto.response.FactoryResponse;
import com.mes.mesBackend.service.FactoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<FactoryResponse> createFactory(@RequestBody FactoryRequest factoryRequest) {
        return new ResponseEntity<>(factoryService.createFactory(factoryRequest), HttpStatus.OK);
    }

    // 공장 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "공장 조회")
    public ResponseEntity<FactoryResponse> getFactory(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(factoryService.getFactory(id), HttpStatus.OK);
    }

    // 공장 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "공장 페이징 조회")
    public ResponseEntity<Page<FactoryResponse>> getFactories(Pageable pageable) {
        return new ResponseEntity<>(factoryService.getFactories(pageable), HttpStatus.OK);
    }

    // 공장 수정
    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "공장 수정")
    public ResponseEntity<FactoryResponse> updateFactory(
            @PathVariable Long id,
            @RequestBody FactoryRequest factoryRequest
    ) {
        return new ResponseEntity<>(factoryService.updateFactory(id, factoryRequest), HttpStatus.OK);
    }

    // 공장 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "공장 삭제")
    public ResponseEntity<Void> deleteFactory(@PathVariable Long id) {
        factoryService.deleteFactory(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
