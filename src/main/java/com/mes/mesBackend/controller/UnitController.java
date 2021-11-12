package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UnitRequest;
import com.mes.mesBackend.dto.response.UnitResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.UnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/units")
@Api(tags = "unit")
@RequiredArgsConstructor
public class UnitController {

    @Autowired
    UnitService unitService;

    // 단위 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "단위 생성")
    public ResponseEntity<UnitResponse> createUnit(
            @RequestBody @Valid UnitRequest unitRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(unitService.createUnit(unitRequest), HttpStatus.OK);
    }

    // 단위 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "단위 단일 조회")
    public ResponseEntity<UnitResponse> getUnit(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(unitService.getUnit(id), HttpStatus.OK);
    }

    // 단위 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "단위 페이징 조회")
    public ResponseEntity<Page<UnitResponse>> getUnits(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(unitService.getUnits(pageable), HttpStatus.OK);
    }

    // 단위 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "단위 수정")
    public ResponseEntity<UnitResponse> updateUnit(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid UnitRequest unitRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(unitService.updateUnit(id, unitRequest), HttpStatus.OK);
    }

    // 단위 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "단위 삭제")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) throws NotFoundException {
        unitService.deleteUnit(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
