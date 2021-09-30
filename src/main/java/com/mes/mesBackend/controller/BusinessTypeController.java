package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.service.BusinessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<BusinessTypeResponse> createBusinessType(
            @RequestBody BusinessTypeRequest businessTypeRequest
    ) {
        return new ResponseEntity<>(businessTypeService.createBusinessType(businessTypeRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "업태 조회")
    public ResponseEntity<BusinessTypeResponse> getBusinessType(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(businessTypeService.getBusinessType(id), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "업태 리스트 조회")
    public ResponseEntity<List<BusinessTypeResponse>> getBusinessTypes() {
        return new ResponseEntity<>(businessTypeService.getBusinessTypes(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "업태 수정")
    public ResponseEntity<BusinessTypeResponse> updateBusinessType(
            @PathVariable(value = "id") Long id,
            @RequestBody BusinessTypeRequest businessTypeRequest
    ) {
        return new ResponseEntity<>(businessTypeService.updateBusinessType(id, businessTypeRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "업태 삭제")
    public ResponseEntity<Void> deleteBusinessType(@PathVariable(value = "id") Long id) {
        businessTypeService.deleteBusinessType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}