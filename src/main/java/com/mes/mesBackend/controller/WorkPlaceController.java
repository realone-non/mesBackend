package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkPlaceRequest;
import com.mes.mesBackend.dto.response.WorkPlaceResponse;
import com.mes.mesBackend.service.WorkPlaceService;
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
@RequestMapping(value = "/work-places")
@Api(tags = "work-place")
@RequiredArgsConstructor
public class WorkPlaceController {

    @Autowired
    WorkPlaceService workPlaceService;

    // 사업장 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "사업장 생성")
    public ResponseEntity<WorkPlaceResponse> createWorkPlace(@RequestBody WorkPlaceRequest workPlaceRequest) {
        return new ResponseEntity<>(workPlaceService.createWorkPlace(workPlaceRequest), HttpStatus.OK);
    }

    // 사업장 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "사업장 단일 조회")
    public ResponseEntity<WorkPlaceResponse> getWorkPlace(@PathVariable Long id) {
        return new ResponseEntity<>(workPlaceService.getWorkPlace(id), HttpStatus.OK);
    }

    // 사업장 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "사업장 페이징 조회")
    public ResponseEntity<Page<WorkPlaceResponse>> getWorkPlaces(Pageable pageable) {
        return new ResponseEntity<>(workPlaceService.getWorkPlaces(pageable), HttpStatus.OK);
    }

    // 사업장 수정
    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "사업장 수정")
    public ResponseEntity<WorkPlaceResponse> updateWorkPlace(
            @PathVariable(value = "id") Long id,
            @RequestBody WorkPlaceRequest workPlaceRequest
    ) {
        return new ResponseEntity<>(workPlaceService.updateWorkPlace(id, workPlaceRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "사업장 삭제")
    public ResponseEntity deleteWorkPlace(@PathVariable(value = "id") Long id) {
        workPlaceService.deleteWorkPlace(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
