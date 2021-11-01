package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WareHouseRequest;
import com.mes.mesBackend.dto.response.WareHouseResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WareHouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/ware-houses")
@Api(tags = "ware-house")
@RequiredArgsConstructor
public class WareHouseController {

    @Autowired
    WareHouseService wareHouseService;

    // 창고 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "창고 생성")
    public ResponseEntity<WareHouseResponse> createWareHouse(
            @RequestBody WareHouseRequest wareHouseRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(wareHouseService.createWareHouse(wareHouseRequest), HttpStatus.OK);
    }

    // 창고 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "창고 단일 조회")
    public ResponseEntity<WareHouseResponse> getWareHouse(@PathVariable(value = "id") Long id) throws NotFoundException {
        return new ResponseEntity<>(wareHouseService.getWareHouse(id), HttpStatus.OK);
    }

    // 창고 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "창고 페이징 조회")
    public ResponseEntity<Page<WareHouseResponse>> getWareHouses(Pageable pageable) {
        return new ResponseEntity<>(wareHouseService.getWareHouses(pageable), HttpStatus.OK);
    }

    // 창고 수정
    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "창고 수정")
    public ResponseEntity<WareHouseResponse> updateWareHouse(
            @PathVariable(value = "id") Long id,
            @RequestBody WareHouseRequest wareHouseRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(wareHouseService.updateWareHouse(id, wareHouseRequest), HttpStatus.OK);
    }

    // 창고 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "창고 삭제")
    public ResponseEntity<Void> deleteWareHouse(@PathVariable(value = "id") Long id) throws NotFoundException {
        wareHouseService.deleteWareHouse(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
