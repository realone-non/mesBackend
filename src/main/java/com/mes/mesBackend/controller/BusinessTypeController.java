package com.mes.mesBackend.controller;

import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.service.BusinessTypeService;
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
@RequestMapping(value = "/business-types")
@Api(tags = {"business-type"})
@RequiredArgsConstructor
public class BusinessTypeController {

    @Autowired
    BusinessTypeService businessTypeService;

    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "업태 생성")
    public ResponseEntity<BusinessType> createBusinessType(@RequestBody BusinessType businessType) {
        return new ResponseEntity<>(businessTypeService.createBusinessType(businessType), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "업태 조회")
    public ResponseEntity<BusinessType> getBusinessType(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(businessTypeService.getBusinessType(id), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "업태 리스트 조회")
    public ResponseEntity<Page<BusinessType>> getBusinessTypes(Pageable pageable) {
        return new ResponseEntity<>(businessTypeService.getBusinessTypes(pageable),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "업태 수정")
    public ResponseEntity<BusinessType> updateBusinessType(@PathVariable(value = "id") Long id,@RequestBody BusinessType businessType) {
        return new ResponseEntity<>(businessTypeService.updateBusinessTypes(id, businessType), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "업태 삭제")
    public ResponseEntity<Void> deleteBusinessType(@PathVariable(value = "id") Long id) {
        businessTypeService.deleteBusinessType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}