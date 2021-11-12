package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WareHouseTypeRequest;
import com.mes.mesBackend.dto.response.WareHouseTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WareHouseTypeService;
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

@RestController
@RequestMapping(value = "/ware-house-types")
@Api(tags = "ware-house-type")
@RequiredArgsConstructor
public class WareHouseTypeController {
    @Autowired
    WareHouseTypeService wareHouseTypeService;

    // 창고유형 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "창고유형 생성")
    public ResponseEntity<WareHouseTypeResponse> createWareHouseType(
            @RequestBody @Valid WareHouseTypeRequest wareHouseTypeRequest
    ) {
        return new ResponseEntity<>(wareHouseTypeService.createWareHouseType(wareHouseTypeRequest), HttpStatus.OK);
    }

    // 창고유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "창고유형 단일 조회")
    public ResponseEntity<WareHouseTypeResponse> getWareHouseType(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(wareHouseTypeService.getWareHouseType(id), HttpStatus.OK);
    }

    // 창고유형 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "창고유형 페이징 조회")
    public ResponseEntity<Page<WareHouseTypeResponse>> getWareHouseTypes(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(wareHouseTypeService.getWareHouseTypes(pageable), HttpStatus.OK);
    }

    // 창고유형 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "창고유형 수정")
    public ResponseEntity<WareHouseTypeResponse> updateWareHouseType(
            @PathVariable Long id,
            @RequestBody @Valid WareHouseTypeRequest wareHouseTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(wareHouseTypeService.updateWareHouseType(id, wareHouseTypeRequest), HttpStatus.OK);
    }

    // 창고유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "창고유형 삭제")
    public ResponseEntity<Void> deleteWareHouseType(@PathVariable Long id) throws NotFoundException {
        wareHouseTypeService.deleteWareHouseType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
